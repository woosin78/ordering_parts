package org.jwebppy.portal.iv.download.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.download.dto.EpDownloadFileHistoryDto;
import org.jwebppy.portal.iv.download.service.EpDownloadFileHistoryService;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.jwebppy.portal.iv.upload.service.EpUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/download")
public class EpDownloadFileController extends IvGeneralController
{
	@Value("${file.upload.rootPath}")
	private String ROOT_PATH;

	@Autowired
	private EpDownloadFileHistoryService downloadFileHistoryService;

	@Autowired
	private EpUploadFileService uploadFileService;

	@Autowired
	private EpUploadFileListService uploadFileListService;

	@RequestMapping
	@ResponseBody
	public void download(@RequestParam("key") String key, HttpServletResponse httpServletResponse) throws Exception
	{
		String[] decodedKey = CmStringUtils.split(AES256Cipher.getInstance().decode(key), IvCommonVo.DELIMITER);

		if (decodedKey.length != 2)
		{
			throw new Exception("Invalid download information.");
		}

		if (Duration.between(LocalDateTime.parse(decodedKey[0], DateTimeFormatter.ofPattern(CmDateFormatUtils.getDateTimeFormat())), LocalDateTime.now()).toMinutes() > 30)
		{
			throw new Exception("Exceeded the download available time. Please refresh this page and try to download again.");
		}

		EpUploadFileListDto uploadFileList = uploadFileListService.getUploadFileList(decodedKey[1]);
		EpUploadFileDto uploadFile = uploadFileService.getUploadFile(uploadFileList.getUfSeq());

		/*
		EpDownloadFileHistoryDto downloadFileHistory = new EpDownloadFileHistoryDto();
		downloadFileHistory.setUfSeq(uploadFile.getUfSeq());
		downloadFileHistory.setUflSeq(uploadFileList.getUflSeq());
		downloadFileHistory.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		downloadFileHistory.setOriginName(uploadFileList.getOriginName() + "." + uploadFileList.getExtension());
		downloadFileHistory.setSavedName(uploadFileList.getSavedName() + "." + uploadFileList.getExtension());
		*/

		EpDownloadFileHistoryDto downloadFileHistory = EpDownloadFileHistoryDto.builder()
				.ufSeq(uploadFile.getUfSeq())
				.uflSeq(uploadFileList.getUflSeq())
				.uSeq(UserAuthenticationUtils.getUserDetails().getUSeq())
				.originName(uploadFileList.getOriginName() + "." + uploadFileList.getExtension())
				.savedName(uploadFileList.getSavedName() + "." + uploadFileList.getExtension())
				.build();

		downloadFileHistoryService.create(downloadFileHistory);

		File file = Paths.get(ROOT_PATH, uploadFile.getPath(), uploadFileList.getSavedName()).toFile();

		if (!file.exists())
		{
			throw new Exception("There is no file.");
		}

		httpServletResponse.reset();

		if (CmStringUtils.equals(uploadFileList.getExtension(), "xlsx"))
		{
			httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
		}
		else
		{
			httpServletResponse.setContentType(new MimetypesFileTypeMap().getContentType(file));
		}

		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferOutputStream = null;

		try
		{
			httpServletResponse.reset();
			httpServletResponse.setHeader("Content-Disposition","attachment; filename=\"" +  new String (uploadFileList.getFullOriginName().getBytes("UTF-8"), "ISO-8859-1") + "\";");
			httpServletResponse.setHeader("Content-Length", "" + file.length());
			httpServletResponse.setHeader("Content-Transfer-Encoding", "binary");
			httpServletResponse.setHeader("Cache-Control", "no-cache");
			httpServletResponse.setHeader("Expires", "-1");
			httpServletResponse.setHeader("Set-Cookie", "fileDownload=true; path=/");//ajax $.filedownload 의 event (successCallback 등) 이 동작히기 위해 필요

			bufferOutputStream = new BufferedOutputStream (httpServletResponse.getOutputStream());

			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

			byte buffer[] = new byte[4096];
			int len = 0;

			while ((len = bufferedInputStream.read(buffer)) > 0)
			{
				bufferOutputStream.write(buffer, 0, len);
			}

			bufferOutputStream.flush();
			bufferOutputStream.close();
			bufferOutputStream = null;

			bufferedInputStream.close();
			bufferedInputStream = null;
		}
		catch (IOException e)
		{
			try
			{
				if (bufferOutputStream != null)
				{
					bufferOutputStream.close();
					bufferOutputStream = null;
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

			try
			{
				if (bufferedInputStream != null)
				{
					bufferedInputStream.close();
					bufferedInputStream = null;
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
