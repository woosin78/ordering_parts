package org.jwebppy.portal.iv.eu.common.file_upload.dto;

import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileUploadDto extends GeneralDto
{
	private Integer fuSeq;
	private String target;
	private Integer tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private Long fileSize;
	private String storagePath;
	private String fgDelete;
	private String type;
	private String changeFileSize;
	private String fgFileStatus;

	private List<String> fgFileStatuss;	//I:추가,D삭제,N변경없음
	private List<Integer> fuSeqs;
	private List<String> fileNames;
	private List<MultipartFile> userFiles;
	private List<Long> sizes;

	private String fileStoragePath;	// 파일업로드 경로
	private Long fileLimitSize;		// 파일업로드시 한계  파일 사이즈

	private Long totalFileSize;	// sum fileSize
	private Long sumFileSize;	// sum fileSize

	private String bbsCorp;
	private String userCorp;

	public FileUploadDto() {}

	public Long getTotalFileSize()
	{
		long totalSize = 0;

		if(userFiles != null)
		{
			for(int i=0; i<userFiles.size(); i++)
			{
//				if("I".equals(fgFileStatuss.get(i))){
//					totalSize += userFiles.get(i).getSize();
//				}

				totalSize += sizes.get(i);
			}
		}
		totalFileSize = totalSize;

		return totalFileSize;
	}
}
