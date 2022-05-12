package org.jwebppy.portal.iv.eu.common.file_upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadDto;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadEditorDto;
import org.jwebppy.portal.iv.eu.common.service.EuGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class EuFileUploadEditorService extends EuGeneralService
{
	@Autowired
	Environment env;

	public FileUploadEditorDto getFileUploadEditor(MultipartHttpServletRequest request) throws Exception {
		FileUploadDto fileUploadDto = new FileUploadDto();
		FileUploadEditorDto fileUploadEditorDto = new FileUploadEditorDto();

		String sClassPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
		String rootPath = env.getProperty("file.upload.rootPath");	// C:\

		String editorSavePath = env.getProperty("xfree.img.tempPath");	//C:\\Project_Ordering\\biz_portal\\target\\classes\\static\\portal\\editor_img\\
		String editorCopyPath = env.getProperty("xfree.img.path");		//C:\\Project_Ordering\\biz_portal\\src\\main\\resources\\static\\portal\\editor_img\\
		String editorBasePath = env.getProperty("xfree.img.webPath");	//  /portal/editor_img/

		editorSavePath = sClassPath+editorSavePath;
		editorCopyPath = rootPath+editorCopyPath;

		String sContentType = request.getParameter("content_type");
		String sUploadedPath = "";
		String sDialogType = request.getParameter("dialog_type");
		String sErrorLog = "";

		fileUploadDto = this.fileTransfer(request.getFile("FILE_PATH"), editorSavePath);
		this.fileCopy(editorSavePath, editorCopyPath, fileUploadDto.getSavedName());

		sUploadedPath = fileUploadDto.getSavedName();
		fileUploadEditorDto.setContentType(sContentType);
		fileUploadEditorDto.setUploadedPath(sUploadedPath);
		fileUploadEditorDto.setDialogType(sDialogType);
		fileUploadEditorDto.setErrorLog(sErrorLog);
		fileUploadEditorDto.setBasePath(editorBasePath);

		return fileUploadEditorDto;
	}

	//1,첨부파일 등록
	public FileUploadDto fileTransfer(MultipartFile userFile, String editorSavePath)  throws IOException
	{
		FileUploadDto fileUploadDto = null;
		String fileFullName = null;					//C:\Users\jungho.min\Documents\연지동.txt
		String fileName = null;						//연지동
		String fileNameExtension = null;
        File destinationFile = null;
        String fileUrl = editorSavePath;			//"D://temp//";

		fileFullName = userFile.getOriginalFilename();
		fileName = FilenameUtils.getBaseName(userFile.getOriginalFilename());
        fileNameExtension = FilenameUtils.getExtension(fileFullName).toLowerCase();
        fileName = fileName + "." + fileNameExtension;
    	String sDate = CmDateFormatUtils.now(EuCommonVo.DEFAULT_DATE_YYYYMMDD_FORMAT);
    	String sTime = CmDateFormatUtils.now(EuCommonVo.DEFAULT_TIME_HHMMSS_FORMAT);

		//String sFileNameHead = BbsCommonVo.IMG;
    	String sFileNameHead = "IMG_";
    	
		// 파일 이름 규칙 적용
		String destinationFileName = sFileNameHead+sDate+sTime+"."+fileNameExtension;

        do {
            destinationFile = new File(fileUrl+ destinationFileName);
        } while (destinationFile.exists());

        destinationFile.getParentFile().mkdirs();
        userFile.transferTo(destinationFile);

        fileUploadDto = new FileUploadDto();
        fileUploadDto.setOriginName(fileName);				// 실제파일
        fileUploadDto.setSavedName(destinationFileName);	// 저장할 파일
        fileUploadDto.setExtension(fileNameExtension);
        fileUploadDto.setFileSize(destinationFile.length());
        fileUploadDto.setStoragePath(fileUrl);
        fileUploadDto.setFgDelete("N");

        return fileUploadDto;
	}

	//1,에디터 이미지 파일  copy
	public void fileCopy(String editorSavePath, String editorCopyPath, String fileName) {
		//원본 파일경로
		String oriFilePath = editorSavePath+fileName;
		//복사될 파일경로
		String copyFilePath = editorCopyPath+fileName;

		//복사될 파일경로 생성
        File copyFileMk = new File(editorCopyPath);
		if (!copyFileMk.exists())
		{
			copyFileMk.mkdirs();
		}

        //파일객체생성
        File oriFile = new File(oriFilePath);
        //복사파일객체생성
        File copyFile = new File(copyFilePath);

        try {

            FileInputStream fis = new FileInputStream(oriFile); //읽을파일
            FileOutputStream fos = new FileOutputStream(copyFile); //복사할파일

            int fileByte = 0;
            // fis.read()가 -1 이면 파일을 다 읽은것
            while((fileByte = fis.read()) != -1) {
                fos.write(fileByte);
            }
            //자원사용종료
            fis.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public FileUploadEditorDto getFileUploadContentsEditor(HttpServletRequest request) throws Exception {
    	String sDate = CmDateFormatUtils.now(EuCommonVo.DEFAULT_DATE_YYYYMMDD_FORMAT);
    	String sTime = CmDateFormatUtils.now(EuCommonVo.DEFAULT_TIME_HHMMSS_FORMAT);
		//String sFileNameHead = BbsCommonVo.IMG;
    	String sFileNameHead = "IMG_";

		String sClassPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
		String rootPath = env.getProperty("file.upload.rootPath");	// C:\

		String editorSavePath = env.getProperty("xfree.img.tempPath");	//C:\\Project_Ordering\\biz_portal\\target\\classes\\static\\portal\\editor_img\\
		String editorCopyPath = env.getProperty("xfree.img.path");		//C:\\Project_Ordering\\biz_portal\\src\\main\\resources\\static\\portal\\editor_img\\
		String editorBasePath = env.getProperty("xfree.img.webPath");	//  /portal/editor_img/

		editorSavePath = sClassPath+editorSavePath;
		editorCopyPath = rootPath+editorCopyPath;

		// 업로드 폴더 생성
		File dir = new File(editorSavePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		String sExtension = request.getParameter("file_extension");
		String sBase64 = request.getParameter("clip_contents");
		String sRootId = request.getParameter("xfe_root_id");

		String sUploadedPath = "";
		String saveFilePath = "";

		byte[] imageByte;

		// 파일 이름 규칙 적용
		String sNewFName = sFileNameHead+sDate+sTime+"."+sExtension;

		sUploadedPath = editorSavePath+sNewFName;
		saveFilePath = sNewFName;
		imageByte = decode(sBase64);

		FileOutputStream fileOuputStream = new FileOutputStream(sUploadedPath);
		fileOuputStream.write(imageByte);
		fileOuputStream.close();

		this.fileCopy(editorSavePath, editorCopyPath, sNewFName);

		FileUploadEditorDto fileUploadEditorDto = new FileUploadEditorDto();
		fileUploadEditorDto.setUploadedPath(sUploadedPath);
		fileUploadEditorDto.setRootId(sRootId);
		fileUploadEditorDto.setSaveFilePath(saveFilePath);
		fileUploadEditorDto.setBasePath(editorBasePath);

		return fileUploadEditorDto;
	}

	private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private static final int[] IA = new int[256];
	static {
		Arrays.fill(IA, -1);
		for (int i = 0, iS = CA.length; i < iS; i++)
			IA[CA[i]] = i;
		IA['='] = 0;
	}

	/** Decodes a BASE64 encoded <code>String</code>. All illegal characters will be ignored and can handle both strings with
	 * and without line separators.<br>
	 * <b>Note!</b> It can be up to about 2x the speed to call <code>decode(str.toCharArray())</code> instead. That
	 * will create a temporary array though. This version will use <code>str.charAt(i)</code> to iterate the string.
	 * @param str The source string. <code>null</code> or length 0 will return an empty array.
	 * @return The decoded array of bytes. May be of length 0. Will be <code>null</code> if the legal characters
	 * (including '=') isn't divideable by 4.  (I.e. definitely corrupted).
	 * @version 2.2
	 * @author Mikael Grev
	 *         Date: 2004-aug-02
	 *         Time: 11:31:11
	 */
	public final static byte[] decode(String str)
	{
		// Check special case
		int sLen = str != null ? str.length() : 0;
		if (sLen == 0)
			return new byte[0];

		// Count illegal characters (including '\r', '\n') to know what size the returned array will be,
		// so we don't have to reallocate & copy it later.
		int sepCnt = 0; // Number of separator characters. (Actually illegal characters, but that's a bonus...)
		for (int i = 0; i < sLen; i++)  // If input is "pure" (I.e. no line separators or illegal chars) base64 this loop can be commented out.
			if (IA[str.charAt(i)] < 0)
				sepCnt++;

		// Check so that legal chars (including '=') are evenly divideable by 4 as specified in RFC 2045.
		if ((sLen - sepCnt) % 4 != 0)
			return null;

		// Count '=' at end
		int pad = 0;
		for (int i = sLen; i > 1 && IA[str.charAt(--i)] <= 0;)
			if (str.charAt(i) == '=')
				pad++;

		int len = ((sLen - sepCnt) * 6 >> 3) - pad;

		byte[] dArr = new byte[len];       // Preallocate byte[] of exact length

		for (int s = 0, d = 0; d < len;) {
			// Assemble three bytes into an int from four "valid" characters.
			int i = 0;
			for (int j = 0; j < 4; j++) {   // j only increased if a valid char was found.
				int c = IA[str.charAt(s++)];
				if (c >= 0)
				    i |= c << (18 - j * 6);
				else
					j--;
			}
			// Add the bytes
			dArr[d++] = (byte) (i >> 16);
			if (d < len) {
				dArr[d++]= (byte) (i >> 8);
				if (d < len)
					dArr[d++] = (byte) i;
			}
		}
		return dArr;
	}

}