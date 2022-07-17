package org.jwebppy.portal.iv.common.service;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.jwebppy.portal.iv.common.dto.XfreeUploadDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class XfreeUploadService extends IvGeneralService
{
	@Value("${xfree.file.path}")
	private String XFREE_FILE_PATH;

	public XfreeUploadDto upload(MultipartHttpServletRequest request) throws Exception
	{
		MultipartFile multiPartFile = request.getFile("FILE_PATH");

		String originFileName = multiPartFile.getOriginalFilename();

		File dir = new File(XFREE_FILE_PATH);

		if (!dir.exists())
		{
			dir.mkdirs();
		}

		multiPartFile.transferTo(new File(XFREE_FILE_PATH + File.separator + getUsername() + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(originFileName)));

		return new XfreeUploadDto();
	}
}
