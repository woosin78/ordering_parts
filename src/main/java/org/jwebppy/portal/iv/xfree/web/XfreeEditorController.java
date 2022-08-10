package org.jwebppy.portal.iv.xfree.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.util.CmFileUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
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
	public void viewer(@RequestParam("fileName") String fileName, HttpServletResponse response) throws Exception
	{
		String filePath = XFREE_UPLOAD_PATH + File.separator + fileName;

		BufferedImage bufferedImage = null;
		File file = new File(filePath);

		String extension = FilenameUtils.getExtension(filePath);
	    String mimeType = Files.probeContentType(Paths.get(filePath));
	    response.setContentType(mimeType);

		try
		{
			bufferedImage = ImageIO.read(file);
		}
		catch (Exception e)
		{
	        // 일부 JPG파일을 제대로 읽지 못하는 오류에 대한 처리(Java에서 인식 가능한 Standard format 으로 변환하여 인식 오류를 줄임)
	        // Caused by: Numbers of source Raster bands and source color space components do not match
	        try
	        {
	        	bufferedImage = CmFileUtils.readImage(file);
	        }
	        catch (Exception e1)
	        {
	        	e1.printStackTrace();
	        }
		}

		OutputStream outputStream = response.getOutputStream();
		ImageIO.write(bufferedImage, extension, outputStream);

		outputStream.close();
		outputStream = null;
	}
}
