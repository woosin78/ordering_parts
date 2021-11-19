package org.jwebppy.platform.mgmt.download.web;

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

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.common.web.MgmtGeneralController;
import org.jwebppy.platform.mgmt.download.dto.DownloadFileHistoryDto;
import org.jwebppy.platform.mgmt.download.service.DownloadFileHistoryService;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileDto;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload.service.UploadFileListService;
import org.jwebppy.platform.mgmt.upload.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/download")
public class DownloadFileController extends MgmtGeneralController
{
	@Value("${file.upload.rootPath}")
	private String ROOT_PATH;

	@Autowired
	private DownloadFileHistoryService downloadFileHistoryService;

	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private UploadFileListService uploadFileListService;

	@RequestMapping
	@ResponseBody
	public void download(@RequestParam("key") String key, HttpServletResponse httpServletResponse) throws Exception
	{
		String[] decodedKey = CmStringUtils.split(AES256Cipher.getInstance().decode(key), PlatformConfigVo.DELIMITER);

		if (decodedKey.length != 2)
		{
			throw new Exception("Invalid download information.");
		}

		if (Duration.between(LocalDateTime.parse(decodedKey[0], DateTimeFormatter.ofPattern(PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)), LocalDateTime.now()).toMinutes() > 30)
		{
			throw new Exception("Exceeded the download available time. Please refresh this page and try to download again.");
		}

		UploadFileListDto uploadFileList = uploadFileListService.getUploadFileList(CmNumberUtils.toInt(decodedKey[1]));
		UploadFileDto uploadFile = uploadFileService.getUploadFile(uploadFileList.getUfSeq());

		DownloadFileHistoryDto downloadFileHistory = new DownloadFileHistoryDto();
		downloadFileHistory.setUfSeq(uploadFile.getUfSeq());
		downloadFileHistory.setUflSeq(uploadFileList.getUflSeq());
		downloadFileHistory.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		downloadFileHistory.setOriginName(uploadFileList.getOriginName() + "." + uploadFileList.getExtension());
		downloadFileHistory.setSavedName(uploadFileList.getSavedName() + "." + uploadFileList.getExtension());

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
