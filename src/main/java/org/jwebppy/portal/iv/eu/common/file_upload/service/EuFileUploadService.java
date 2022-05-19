package org.jwebppy.portal.iv.eu.common.file_upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadDto;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadInfoDto;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileUploadEntity;
import org.jwebppy.portal.iv.eu.common.file_upload.mapper.EuFileUploadMapper;
import org.jwebppy.portal.iv.eu.common.service.EuGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EuFileUploadService extends EuGeneralService
{
	@Autowired
	private EuFileUploadInfoService fileUploadInfoService;

	@Autowired
	private EuFileUploadMapper fileUploadMapper;

	/*
	 * @Autowired Environment env;
	 */
	
	@Autowired
	private Environment environment;

	//파일등록
	public int fileInsert(FileUploadDto fileUpload) throws IOException
	{
		FileUploadDto fileUploadDto = new FileUploadDto();
		List<String> fgFileStatuss = fileUpload.getFgFileStatuss();

		for(int i=0; i<fgFileStatuss.size(); i++) {
			//1,첨부파일 등록
			if("I".equals(fgFileStatuss.get(i))) {
				fileUploadDto = this.fileTransfer(fileUpload.getUserFiles().get(i), fileUpload.getFileStoragePath());

				//2,ep_file_upload 등록
	            fileUploadDto.setTSeq(fileUpload.getTSeq()); //bcSeq
	            fileUploadDto.setTarget(fileUpload.getTarget()); //target : BBS
	            fileUploadDto.setType(fileUpload.getType());
	            this.insertEpFileUpload(fileUploadDto);
			}
		}
		return 1;
	}

	//1,첨부파일 등록
	public FileUploadDto fileTransfer(MultipartFile userFile, String storagePath)  throws IOException
	{
		FileUploadDto fileUploadDto = null;
		String fileFullName = null;					//C:\Users\jungho.min\Documents\연지동.txt
		String fileName = null;						//연지동
		String fileNameExtension = null;
        File destinationFile = null;
        String destinationFileName = null;
        String fileUrl = storagePath;								// FILE_STORAGE\\EU\\NOTICE\\
		//String rootPath = env.getProperty("file.upload.rootPath");	// C:\\
        String rootPath = environment.getProperty("file.upload.rootPath");
		fileUrl = rootPath+fileUrl;		// C:\\FILE_STORAGE\\EU\\NOTICE\\

		fileFullName = userFile.getOriginalFilename();
		fileName = FilenameUtils.getBaseName(userFile.getOriginalFilename());
        fileNameExtension = FilenameUtils.getExtension(fileFullName).toLowerCase();
        fileName = fileName + "." + fileNameExtension;

        do {
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileNameExtension;
            destinationFile = new File(fileUrl+ destinationFileName);
        } while (destinationFile.exists());

        destinationFile.getParentFile().mkdirs();
        userFile.transferTo(destinationFile);

        fileUploadDto = new FileUploadDto();
        fileUploadDto.setOriginName(fileName);				// 실제파일
        fileUploadDto.setSavedName(destinationFileName);	// 저장할 파일
        fileUploadDto.setExtension(fileNameExtension);
        fileUploadDto.setFileSize(destinationFile.length());
        fileUploadDto.setStoragePath(storagePath);
        fileUploadDto.setFgDelete("N");

        return fileUploadDto;
	}

	public FileUploadEntity insertFileUpload(FileUploadDto fileUploadDto) {
		FileUploadEntity fileUploadEntity = CmModelMapperUtils.map(fileUploadDto, FileUploadEntity.class);
		fileUploadMapper.insertFileUpload(fileUploadEntity);
		return fileUploadEntity;
	}

	public FileUploadEntity getFuSeq(FileUploadDto fileUploadDto) {
		FileUploadEntity fileUploadEntity = fileUploadMapper.findFuSeq(fileUploadDto);
		return fileUploadEntity;
	}

	//2,ep_file_upload 등록
	public int insertEpFileUpload(FileUploadDto fileUploadDto) {
		return fileUploadMapper.insertFileUpload(CmModelMapperUtils.map(fileUploadDto, FileUploadEntity.class));
	}

	//파일삭제(물리적파일삭제)
	public int fileDelete(FileUploadDto fileUpload) throws IOException
	{
		int rtn = 0;
		List<String> fgFileStatuss = fileUpload.getFgFileStatuss();
		List<Integer> fuSeqs = fileUpload.getFuSeqs();
		FileUploadDto fileUploadDto = new FileUploadDto();

		for(int i=0; i<fgFileStatuss.size(); i++) {
			rtn = 0;
			//1,첨부파일 삭제
			if("D".equals(fgFileStatuss.get(i)) && fuSeqs.get(i) != null) {
//				rtn = this.realFileDelete(fuSeqs.get(i));
				//물리 파일 삭제 주석처리
				rtn = 1;
			}

			//2,ep_file_upload 삭제
			if(rtn > 0 && ("D".equals(fgFileStatuss.get(i)) && fuSeqs.get(i) != null)) {
				fileUploadDto.setFuSeq(fuSeqs.get(i));
				fileUploadDto.setBbsCorp(fileUpload.getBbsCorp());
				fileUploadDto.setUserCorp(fileUpload.getUserCorp());
				fileUploadDto.setModDate(fileUpload.getModDate());
				fileUploadDto.setModUsername(fileUpload.getModUsername());
				this.deleteFileUpload(fileUploadDto);
			}
		}

		return 1;
	}

	public int realFileDelete(int fuSeq) {
		int rtn;
		FileUploadDto fileUploadDto = this.getFileUpload(fuSeq);
		//String rootPath = env.getProperty("file.upload.rootPath");
		String rootPath = environment.getProperty("file.upload.rootPath");
		String filePath = fileUploadDto.getStoragePath() + fileUploadDto.getSavedName(); 	// C:/tmp/test.txt
		filePath = rootPath+filePath;
		boolean delYn = true;
		File file = new File(filePath);
		if(file.exists()) {
			delYn = file.delete();
			if(delYn){
				rtn = 1;	// "File Delete Success" //성공
			}else{
				rtn = -1;	// "File Delete Fail" //실패
			}
		}else{
			rtn = -1;		// "File Not Found" //미존재
		}
		return rtn;
	}

	//2,ep_file_upload 삭제
	public int deleteFileUpload(FileUploadDto fileUploadDto) {
		return fileUploadMapper.deleteEpFileUpload(CmModelMapperUtils.map(fileUploadDto, FileUploadEntity.class));
	}

	public List<FileUploadDto> getFileUploadList(FileUploadDto fileUpload)
	{
		String changeFileSize = "";
		List<FileUploadEntity> fileUploads = fileUploadMapper.findFileUploads(fileUpload);
		//String rootPath = env.getProperty("file.upload.rootPath");
		String rootPath = environment.getProperty("file.upload.rootPath");
		String storagePath = "";
		for(int i=0; i<fileUploads.size(); i++) {
			storagePath = rootPath + fileUploads.get(i).getStoragePath();
			changeFileSize = this.changeFileSize(String.valueOf(fileUploads.get(i).getFileSize()));
			fileUploads.get(i).setChangeFileSize(changeFileSize);
			fileUploads.get(i).setStoragePath(storagePath);
		}

		return CmModelMapperUtils.mapAll(fileUploads, FileUploadDto.class);
	}

	public FileUploadDto getFileUpload(int fuSeq)
	{
		return CmModelMapperUtils.map(fileUploadMapper.findFileUpload(fuSeq), FileUploadDto.class);
	}

	//파일 업로드 조회
	public List<Integer> getFileUploadFuSeqs(FileUploadDto fileUpload)
	{
		List<Integer> listFuSeq = new ArrayList<>();
		List<FileUploadEntity> fileUploads = fileUploadMapper.findFileUploads(fileUpload);

		for(int i=0; i<fileUploads.size(); i++) {
			listFuSeq.add(i, fileUploads.get(i).getFuSeq());
		}

		return listFuSeq;
	}


	//파일 다운로드
	public void fileDown(int fuSeq, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
       	request.setCharacterEncoding("UTF-8");
        FileUploadDto fileUploadDto = this.getFileUpload(fuSeq);
		//String rootPath = env.getProperty("file.upload.rootPath");	// C:\\
        String rootPath = environment.getProperty("file.upload.rootPath");

        //파일 업로드된 경로
        try{
            String fileUrl = rootPath+fileUploadDto.getStoragePath();
            fileUrl += "/";
            String savePath = fileUrl;
            String fileName = fileUploadDto.getSavedName();

            //실제 내보낼 파일명
            String oriFileName = fileUploadDto.getOriginName();
            InputStream in = null;
            OutputStream os = null;
            File file = null;
            boolean skip = false;
            String client = "";

            //파일을 읽어 스트림에 담기
            try{
                file = new File(savePath, fileName);
                in = new FileInputStream(file);
            } catch (FileNotFoundException fe) {
                skip = true;
            }

            client = request.getHeader("User-Agent");

            //파일 다운로드 헤더 지정
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Description", "JSP Generated Data");

            if (!skip) {
                // IE
                if (client.indexOf("MSIE") != -1) {
                    response.setHeader("Content-Disposition", "attachment; filename=\""
                            + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
                    // IE 11 이상.
                } else if (client.indexOf("Trident") != -1) {
                    response.setHeader("Content-Disposition", "attachment; filename=\""
                            + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
                } else {
                    // 한글 파일명 처리
                    response.setHeader("Content-Disposition",
                            "attachment; filename=\"" + new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
                    response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
                }
                response.setHeader("Content-Length", "" + file.length());
                os = response.getOutputStream();
                byte b[] = new byte[(int) file.length()];
                int leng = 0;
                while ((leng = in.read(b)) > 0) {
                    os.write(b, 0, leng);
                }
            } else {
    			response.setContentType("text/html; charset=UTF-8");
    			PrintWriter out = response.getWriter();
    			out.println("<script>alert('파일을 찾을 수 없습니다.'); history.go(-1);</script>");
    			out.flush();
            }
            in.close();
            os.close();

        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

	//파일 사이즈 단위 변경
	public String changeFileSize(String size) {
		String strChangeSize = new String();
		double changeSize = 0;
		long fileSize = 0;
		String[] strFileSize = new String[3];

		fileSize = Long.parseLong(size);
		for(int x=0; (fileSize / (double)1024 ) > 0; x++, fileSize /= (double)1024) {
			changeSize = fileSize;
			strFileSize[x] = String.valueOf(changeSize);
		}

		if(strFileSize[2] != null) {
			strChangeSize = strFileSize[2]+"MB";
		}else{
			if(strFileSize[1] != null) {
				strChangeSize = strFileSize[1]+"KB";
			}else{
				strChangeSize = strFileSize[0]+"Byte";
			}
		}

		return strChangeSize;
	}

	//파일 확장자 체크
	//파일사이즈 체크
	public FileUploadDto checkFileInfo(FileUploadDto inFileUpload, String fuiId)
	{
		FileUploadInfoDto fileUploadInfo = fileUploadInfoService.getFileUploadInfo(fuiId);
		List<String> fgFileStatuss = inFileUpload.getFgFileStatuss();
		List<MultipartFile> userFiles = inFileUpload.getUserFiles();

		String fileFullName = null;					//C:\Users\jungho.min\Documents\연지동.txt
		String fileName = null;
		String fileNameExtension = null;
		//String checkFileExtension = env.getProperty("file.upload.invalidExtension");	//html,jsp,exe,com,js
		String checkFileExtension = environment.getProperty("file.upload.invalidExtension");
		String fileAllowExtension = fileUploadInfo.getFileAllowExt();
		FileUploadDto fileUploadDto = new FileUploadDto();
		int k = 0;

		List<String> listExtension = new ArrayList<>();
		String[] aryExtension = checkFileExtension.split(";");		//보안상 허용 안되는 확장자
		String[] aryAllowExtension = fileAllowExtension.split(",");	//허용되는 확장자
		long totalFileSize = 0L;

		if(inFileUpload.getFuSeqs() != null && fgFileStatuss.size() > 0) {
			for(int i=0; i<userFiles.size(); i++) {
				if("I".equals(fgFileStatuss.get(i))){
					fileFullName = userFiles.get(i).getOriginalFilename();
					fileName = FilenameUtils.getBaseName(userFiles.get(i).getOriginalFilename());
					fileNameExtension = FilenameUtils.getExtension(fileFullName).toLowerCase();

					for(int j=0; j<aryExtension.length; j++) {
						if(fileNameExtension.equals(aryExtension[j].trim().toLowerCase())) {
							listExtension.add(k, fileName+"."+fileNameExtension);
							k++;
						}
					}
					totalFileSize += userFiles.get(i).getSize();
				}
			}

			int sw = 1;
			if(k == 0) {
				for(int i=0; i<userFiles.size(); i++) {
					if("I".equals(fgFileStatuss.get(i))){
						sw = 1;
						fileFullName = userFiles.get(i).getOriginalFilename();
						fileName = FilenameUtils.getBaseName(userFiles.get(i).getOriginalFilename());
						fileNameExtension = FilenameUtils.getExtension(fileFullName).toLowerCase();

						for(int j=0; j<aryAllowExtension.length; j++) {
							if(fileNameExtension.equals(aryAllowExtension[j].trim().toLowerCase())) {
								sw = 2;
							}
						}

						if(sw == 1) {
							listExtension.add(k, fileName+"."+fileNameExtension);
							k++;
						}
					}
				}
			}
		}

		fileUploadDto.setSumFileSize(totalFileSize);	//파일사이즈 합과 한계 파일 사이즈 비교
		fileUploadDto.setFileNames(listExtension);		//허용 안되는 확장자 목록
		fileUploadDto.setFileStoragePath(fileUploadInfo.getFileStoragePath());	// 파일 저장 경로
		fileUploadDto.setFileLimitSize(fileUploadInfo.getFileLimitSize());		// 한게 파일 사이즈
		return fileUploadDto;
	}


	// 머천다이즈 mall 의 메인 파일 변경 시. 기존의 fileDelete 메서드 처리방식과 달라서 예외 케이스로 분리함.
	public int fileDeleteMallMain(FileUploadDto fileUpload) throws IOException
	{
		return fileUploadMapper.deleteEpFileUpload(CmModelMapperUtils.map(fileUpload, FileUploadEntity.class));
	}
}
