package org.jwebppy.portal.iv.xfree.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.utils.ImageLoadUtils;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.xfree.service.XfreeEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/xfree")
public class XfreeEditorController extends IvGeneralController
{
	@Value("${xfree.upload.path}")
	private String XFREE_UPLOAD_PATH;

	@Autowired
	private XfreeEditorService xfreeUploadService;

	@PostMapping("/image/upload")
	public String imageUpload(Model model, MultipartHttpServletRequest request)
	{
		try
		{
			model.addAttribute("xfreeUpload", xfreeUploadService.upload(request));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/upload")
	public String upload(Model model, HttpServletRequest request)
	{
		try
		{
			model.addAttribute("xfreeUpload", xfreeUploadService.upload(request));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/image/viewer")
	public void viewer(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException
	{
		ImageLoadUtils.load(XFREE_UPLOAD_PATH + File.separator + fileName, response);
	}
}
