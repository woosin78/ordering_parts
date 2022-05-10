package org.jwebppy.portal.iv.eu.common.file_upload.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadDto;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileUploadEntity;

@Mapper
public interface EuFileUploadMapper
{
	public List<FileUploadEntity> findFileUploads(FileUploadDto fileUpload);	//BBS Seq
	public FileUploadEntity findFileUpload(int fuSeq);							//FileUpload Seq	
	
	public int insertFileUpload(FileUploadEntity fileUploadEntity);
	public int deleteEpFileUpload(FileUploadEntity fileUploadEntity);
		
	public int deleteFileUploadImageMerchandize(FileUploadEntity fileUploadEntity);	// 하나의 상품 시퀀스에 엮인 모든 파일 정보 삭제(머천다이즈 모듈에서 호출)
	
	public FileUploadEntity findFuSeq(FileUploadDto fileUpload);	
}
