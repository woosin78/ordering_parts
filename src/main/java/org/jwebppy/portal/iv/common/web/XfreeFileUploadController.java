package org.jwebppy.portal.iv.common.web;

import javax.servlet.http.HttpServletRequest;

import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.service.XfreeUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/xfree")
public class XfreeFileUploadController extends IvGeneralController
{
	@Autowired
	private XfreeUploadService xfreeUploadService;

	@PostMapping("/editor_upload")
	public String editorUpload(Model model, MultipartHttpServletRequest request)
	{
		try
		{
			model.addAttribute("fileUploadEditor", xfreeUploadService.upload(request));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/editor_upload_contents")
	public String editorUploadContents(Model model, HttpServletRequest request)
	{
		try
		{
			//FileUploadEditorDto fileUploadEditor = fileUploadEditorService.getFileUploadContentsEditor(request);
			//model.addAttribute("fileUploadEditor", fileUploadEditor);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return DEFAULT_VIEW_URL;
	}
}
