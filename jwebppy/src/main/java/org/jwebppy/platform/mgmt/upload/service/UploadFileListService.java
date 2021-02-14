package org.jwebppy.platform.mgmt.upload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileDto;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileListEntity;
import org.jwebppy.platform.mgmt.upload.mapper.UploadFileListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileListService extends GeneralService
{
    @Value("${file.upload.rootPath}")
    private String rootPath;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UploadFileListMapper uploadFileListMapper;

	public int save(Integer ufSeq, Integer tSeq, List<MultipartFile> files) throws IOException
	{
		UploadFileDto uploadFile = uploadFileService.getUploadFile(ufSeq);

		if (uploadFile != null)
		{
			if (isExceedMaxUploadSize(files, uploadFile.getMaxFileSize()))
			{
				throw new MaxUploadSizeExceededException(uploadFile.getMaxFileSize());
			}

			String path = uploadFile.getPath();

			FileUtils.forceMkdir(new File(rootPath + File.separator + path));

			for (MultipartFile file: files)
			{
				String originFilename = file.getOriginalFilename();

				if (CmStringUtils.isEmpty(originFilename))
				{
					continue;
				}

				String savedName = uploadFile.getUfSeq().toString() + "_" + System.nanoTime();

				file.transferTo(new File(rootPath + File.separator + path + File.separator + savedName));

				UploadFileListDto uploadFileList = new UploadFileListDto();
				uploadFileList.setUfSeq(ufSeq);
				uploadFileList.setTSeq(tSeq);
				uploadFileList.setOriginName(FilenameUtils.getBaseName(originFilename));
				uploadFileList.setSavedName(FilenameUtils.getBaseName(savedName));
				uploadFileList.setExtension(FilenameUtils.getExtension(originFilename).toLowerCase());
				uploadFileList.setSize(file.getSize());
				uploadFileList.setFgDelete(PlatformCommonVo.NO);

				uploadFileListMapper.insert(CmModelMapperUtils.map(uploadFileList, UploadFileListEntity.class));
			}

			return 1;
		}

		return 0;
	}

	public boolean isExceedMaxUploadSize(List<MultipartFile> files, long limit)
	{
		if (CollectionUtils.isNotEmpty(files) && files.size() > 0)
		{
			long amount = 0;

			for (MultipartFile file: files)
			{
				amount += file.getSize();
			}

			if (amount > limit)
			{
				return true;
			}
		}

		return false;
	}

	@Transactional
	public int delete(int uflSeq)
	{
		UploadFileListEntity uploadfileList = new UploadFileListEntity();
		uploadfileList.setUflSeq(uflSeq);

		return uploadFileListMapper.delete(uploadfileList);
	}

	@Transactional
	public int delete(List<Integer> uflSeqs)
	{
		if (CollectionUtils.isNotEmpty(uflSeqs))
		{
			for (int uflSeq: uflSeqs)
			{
				delete(uflSeq);
			}
		}

		return 1;
	}

	public UploadFileListDto getUploadFileList(int uflSeq)
	{
		return CmModelMapperUtils.map(uploadFileListMapper.findUploadFileList(uflSeq), UploadFileListDto.class);
	}

	public List<UploadFileListDto> getUploadFileLists(int ufSeq, int tSeq)
	{
		UploadFileListDto uploadFileList = new UploadFileListDto();
		uploadFileList.setUfSeq(ufSeq);
		uploadFileList.setTSeq(tSeq);

		return CmModelMapperUtils.mapAll(uploadFileListMapper.findUploadFileLists(uploadFileList), UploadFileListDto.class);
	}
}
