package org.jwebppy.portal.iv.upload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileListEntity;
import org.jwebppy.portal.iv.upload.mapper.EpUploadFileListMapper;
import org.jwebppy.portal.iv.upload.mapper.EpUploadFileListObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class EpUploadFileListService extends IvGeneralService
{
    @Value("${file.upload.rootPath}")
    private String rootPath;

    @Value("${file.upload.invalidExtension}")
    private String invalidExtension;

    @Autowired
    private EpUploadFileService uploadFileService;

    @Autowired
    private EpUploadFileListMapper uploadFileListMapper;

	public int save(String ufSeq, String tSeq, List<MultipartFile> multipartFiles) throws IOException
	{
		EpUploadFileDto uploadFile = uploadFileService.getUploadFile(ufSeq);

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

			FileUtils.forceMkdir(new File(rootPath + File.separator + path));

			for (MultipartFile multipartFile: multipartFiles)
			{
				String originFilename = multipartFile.getOriginalFilename();

				if (CmStringUtils.isEmpty(originFilename))
				{
					continue;
				}

				String savedName = uploadFile.getUfSeq().toString() + "_" + System.nanoTime();

				multipartFile.transferTo(new File(rootPath + File.separator + path + File.separator + savedName));

				/*
				EpUploadFileListDto uploadFileList = new EpUploadFileListDto();
				uploadFileList.setUflSeq(UidGenerateUtils.generate());
				uploadFileList.setUfSeq(ufSeq);
				uploadFileList.setTSeq(tSeq);
				uploadFileList.setOriginName(FilenameUtils.getBaseName(originFilename));
				uploadFileList.setSavedName(FilenameUtils.getBaseName(savedName));
				uploadFileList.setExtension(FilenameUtils.getExtension(originFilename).toLowerCase());
				uploadFileList.setFileSize(multipartFile.getSize());
				uploadFileList.setFgDelete(PlatformCommonVo.NO);
				*/

				EpUploadFileListDto uploadFileList = EpUploadFileListDto.builder()
						.uflSeq(UidGenerateUtils.generate())
						.ufSeq(ufSeq)
						.tSeq(tSeq)
						.originName(FilenameUtils.getBaseName(originFilename))
						.savedName(FilenameUtils.getBaseName(savedName))
						.extension(FilenameUtils.getExtension(originFilename).toLowerCase())
						.fileSize(multipartFile.getSize())
						.fgDelete(PlatformCommonVo.NO)
						.build();

				uploadFileListMapper.insert(CmModelMapperUtils.mapToEntity(EpUploadFileListObjectMapper.INSTANCE, uploadFileList));
			}

			return 1;
		}

		return 0;
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

	public String checkValidExtension(List<MultipartFile> multipartFiles, EpUploadFileDto uploadFile)
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
		EpUploadFileListEntity uploadfileList = new EpUploadFileListEntity();
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

	public EpUploadFileListDto getUploadFileList(String uflSeq)
	{
		return CmModelMapperUtils.mapToDto(EpUploadFileListObjectMapper.INSTANCE, uploadFileListMapper.findUploadFileList(uflSeq));
	}

	public List<EpUploadFileListDto> getUploadFileLists(String ufSeq, String tSeq)
	{
		/*
		EpUploadFileListDto uploadFileList = new EpUploadFileListDto();
		uploadFileList.setUfSeq(ufSeq);
		uploadFileList.setTSeq(tSeq);
		*/

		EpUploadFileListDto uploadFileList = EpUploadFileListDto.builder()
				.ufSeq(ufSeq)
				.tSeq(tSeq)
				.build();

		return CmModelMapperUtils.mapToDto(EpUploadFileListObjectMapper.INSTANCE, uploadFileListMapper.findUploadFileLists(uploadFileList));
	}
}
