package org.jwebppy.portal.iv.eu.common.file_upload.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileDownloadHistoryDto;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadDto;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadEditorDto;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileDownloadHistoryService;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileUploadEditorService;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileUploadService;
import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/portal/corp/eu/common/file_upload")
public class EuFileUploadController extends PartsGeneralController
{
	@Autowired
	private EuFileUploadService fileUploadService;

	@Autowired
	private EuFileDownloadHistoryService fileDownloadHistoryService;

	@Autowired
	private EuFileUploadEditorService fileUploadEditorService;

	@GetMapping("/file_down")
	public void fileDown(@RequestParam("fuSeq") int fuSeq, HttpServletRequest request, HttpServletResponse response)
	{
		try {
			fileUploadService.fileDown(fuSeq, request, response);

            FileDownloadHistoryDto fileDownloadHistoryDto = new FileDownloadHistoryDto();
            fileDownloadHistoryDto.setFuSeq(fuSeq);
            fileDownloadHistoryService.insertFileDownloadHistory(fileDownloadHistoryDto);
		} catch (Exception e) {
        	e.printStackTrace();
        }
	}

	@PostMapping("/check_file")
	@ResponseBody
	public Object checkFile(Model model, @ModelAttribute("fileUpload") FileUploadDto inFileUpload
									   , @RequestParam(value="fuiId", required=false) String fuiId
									   )
	{
		FileUploadDto outFileUpload = fileUploadService.checkFileInfo(inFileUpload, fuiId);
		return outFileUpload;
	}

	@PostMapping("/editor_upload")
	public String editorUpload(Model model, MultipartHttpServletRequest request)
	{
		try {
			FileUploadEditorDto fileUploadEditor = fileUploadEditorService.getFileUploadEditor(request);
			model.addAttribute("fileUploadEditor", fileUploadEditor);
		}catch(Exception e){
			e.printStackTrace();
		}

		return "/portal/corp/eu/common/file_upload/editor_upload";
	}

	@PostMapping("/editor_upload_contents")
	public String editorUploadContents(Model model, HttpServletRequest request)
	{
		try {
			FileUploadEditorDto fileUploadEditor = fileUploadEditorService.getFileUploadContentsEditor(request);
			model.addAttribute("fileUploadEditor", fileUploadEditor);
		}catch(Exception e){
			e.printStackTrace();
		}

		return "/portal/corp/eu/common/file_upload/editor_upload_contents";
	}
}
