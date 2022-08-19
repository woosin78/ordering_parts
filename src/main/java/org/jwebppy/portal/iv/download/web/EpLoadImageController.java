package org.jwebppy.portal.iv.download.web;

import java.io.File;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.utils.ImageLoadUtils;
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
@RequestMapping(IvCommonVo.REQUEST_PATH + "/image")
public class EpLoadImageController extends IvGeneralController
{
	@Value("${file.upload.rootPath}")
	private String ROOT_PATH;

	@Autowired
	private EpDownloadFileHistoryService downloadFileHistoryService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private EpUploadFileService uploadFileService;

	@Autowired
	private EpUploadFileListService uploadFileListService;

	@RequestMapping("/load")
	@ResponseBody
	public void image(@RequestParam("ulfSeq") String ulfSeq, HttpServletResponse httpServletResponse) throws Exception
	{
		EpUploadFileListDto uploadFileList = uploadFileListService.getUploadFileList(ulfSeq);

		if (ObjectUtils.isEmpty(uploadFileList))
		{
			throw new Exception(i18nMessageSource.getMessage("PTL_M_FILE_NOT_FOUND"));
		}

		EpUploadFileDto uploadFile = uploadFileService.getUploadFile(uploadFileList.getUfSeq());

		File file = Paths.get(ROOT_PATH, uploadFile.getPath(), uploadFileList.getSavedName()).toFile();

		if (!file.exists())
		{
			throw new Exception(i18nMessageSource.getMessage("PTL_M_FILE_NOT_FOUND"));
		}

		File imageFile = Paths.get(ROOT_PATH, uploadFile.getPath(), uploadFileList.getFullSavedName()).toFile();

		if (!imageFile.exists())
		{
			FileUtils.copyFile(file, imageFile);
		}

		EpDownloadFileHistoryDto downloadFileHistory = new EpDownloadFileHistoryDto();
		downloadFileHistory.setUfSeq(uploadFile.getUfSeq());
		downloadFileHistory.setUflSeq(uploadFileList.getUflSeq());
		downloadFileHistory.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		downloadFileHistory.setOriginName(uploadFileList.getOriginName() + "." + uploadFileList.getExtension());
		downloadFileHistory.setSavedName(uploadFileList.getSavedName() + "." + uploadFileList.getExtension());

		downloadFileHistoryService.create(downloadFileHistory);

		ImageLoadUtils.load(imageFile.getAbsolutePath(), httpServletResponse);
	}
}
