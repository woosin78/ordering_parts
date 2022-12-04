package org.jwebppy.platform.mgmt.upload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileDto;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileListEntity;
import org.jwebppy.platform.mgmt.upload.mapper.UploadFileListMapper;
import org.jwebppy.platform.mgmt.upload.mapper.UploadFileListObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UploadFileListService extends GeneralService
{
    @Value("${file.upload.rootPath}")
    private String FILE_UPLOAD_ROOT_PATH;

    @Value("${file.upload.invalidExtension}")
    private String invalidExtension;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UploadFileListMapper uploadFileListMapper;

	public String[] save(String ufSeq, String tseq, List<MultipartFile> multipartFiles) throws IOException
	{
		UploadFileDto uploadFile = uploadFileService.getUploadFile(ufSeq);

		if (uploadFile != null)
		{
			if (isExceedMaxUploadSize(multipartFiles, uploadFile.getMaxFileSize()))
			{
				throw new MaxUploadSizeExceededException(uploadFile.getMaxFileSize());
			}

			String validCheckResult = checkValidExtension(multipartFiles, uploadFile);

			if (validCheckResult != null)
			{
				throw new InvalidFileNameException(validCheckResult, "[" + validCheckResult + "] Invalid File Extension");
			}

			String path = uploadFile.getPath();

			FileUtils.forceMkdir(new File(FILE_UPLOAD_ROOT_PATH + File.separator + path));
			String[] uflSeqs = new String[multipartFiles.size()];
			int index = 0;

			for (MultipartFile multipartFile: multipartFiles)
			{
				String originNname = multipartFile.getOriginalFilename();

				if (CmStringUtils.isEmpty(originNname))
				{
					continue;
				}

				String savedName = uploadFile.getUfSeq().toString() + "_" + System.nanoTime();

				multipartFile.transferTo(new File(FILE_UPLOAD_ROOT_PATH + File.separator + path + File.separator + savedName));

				uflSeqs[index++] = save(ufSeq, tseq, originNname, savedName, multipartFile.getResource().getFile().getAbsolutePath());
			}

			return uflSeqs;
		}

		return null;
	}

	public String save(String ufSeq, String tseq, String originName, String savedName, String path) throws IOException
	{
		File file = new File(path + File.separator + savedName);
		String uflSeq = UidGenerateUtils.generate();

		UploadFileListDto uploadFileList = new UploadFileListDto();
		uploadFileList.setUflSeq(uflSeq);
		uploadFileList.setUfSeq(ufSeq);
		uploadFileList.setTseq(tseq);
		uploadFileList.setOriginName(FilenameUtils.getBaseName(originName));
		uploadFileList.setSavedName(FilenameUtils.getBaseName(savedName));
		uploadFileList.setExtension(FilenameUtils.getExtension(originName).toLowerCase());
		uploadFileList.setFileSize(file.length());

		if (uploadFileListMapper.insert(CmModelMapperUtils.mapToEntity(UploadFileListObjectMapper.INSTANCE, uploadFileList)) > 0)
		{
			return uflSeq;
		}

		return null;
	}

	public boolean isExceedMaxUploadSize(List<MultipartFile> multipartFiles, long limit)
	{
		if (CollectionUtils.isNotEmpty(multipartFiles) && multipartFiles.size() > 0)
		{
			long amount = 0;

			for (MultipartFile multipartFile: multipartFiles)
			{
				amount += multipartFile.getSize();
			}

			if (amount > limit)
			{
				return true;
			}
		}

		return false;
	}

	public String checkValidExtension(List<MultipartFile> multipartFiles, UploadFileDto uploadFile)
	{
		if (CollectionUtils.isNotEmpty(multipartFiles))
		{
			String exExtension = CmStringUtils.defaultIfEmpty(invalidExtension, uploadFile.getExExtension());

			if (CmStringUtils.isNotEmpty(exExtension))
			{
				for (MultipartFile multipartFile: multipartFiles)
				{
					String originName = multipartFile.getOriginalFilename();

					if (CmStringUtils.isEmpty(originName))
					{
						continue;
					}

					String extension = FilenameUtils.getExtension(originName);

					if (CmStringUtils.containsIgnoreCase(exExtension, extension))
					{
						return originName;
					}
				}
			}

			String inExtension = uploadFile.getInExtension();

			if (CmStringUtils.isNotEmpty(inExtension))
			{
				for (MultipartFile multipartFile: multipartFiles)
				{
					String originName = multipartFile.getOriginalFilename();

					if (CmStringUtils.isEmpty(originName))
					{
						continue;
					}

					String extension = FilenameUtils.getExtension(originName);

					if (!CmStringUtils.containsIgnoreCase(inExtension, extension))
					{
						return originName;
					}
				}
			}
		}

		return null;
	}

	public int delete(String uflSeq)
	{
		UploadFileListEntity uploadfileList = new UploadFileListEntity();
		uploadfileList.setUflSeq(uflSeq);

		return uploadFileListMapper.delete(uploadfileList);
	}

	public int delete(List<String> uflSeqs)
	{
		if (CollectionUtils.isNotEmpty(uflSeqs))
		{
			for (String uflSeq: uflSeqs)
			{
				delete(uflSeq);
			}
		}

		return 1;
	}

	public UploadFileListDto getUploadFileList(String uflSeq)
	{
		return CmModelMapperUtils.mapToDto(UploadFileListObjectMapper.INSTANCE, uploadFileListMapper.findUploadFileList(uflSeq));
	}

	public List<UploadFileListDto> getUploadFileLists(String ufSeq, String tseq)
	{
		UploadFileListDto uploadFileList = new UploadFileListDto();
		uploadFileList.setUfSeq(ufSeq);
		uploadFileList.setTseq(tseq);

		return CmModelMapperUtils.mapToDto(UploadFileListObjectMapper.INSTANCE, uploadFileListMapper.findUploadFileLists(uploadFileList));
	}
}
