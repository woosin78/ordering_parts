package org.jwebppy.portal.iv.eu.common.file_upload.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.util.CmFileUtils;
import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/portal/corp/eu/common/file_load")
public class EuFileLoadController extends PartsGeneralController
{

	@RequestMapping("/image_viewer")
	public void imageViewer(@RequestParam(value = "filePath", required = true) String filePath, HttpServletResponse response) throws Exception
	{
		BufferedImage bi = null;
		File imgFile = new File(filePath);

		String fileExt = FilenameUtils.getExtension(filePath);
	    String fileMimeType = Files.probeContentType(Paths.get(filePath));
	    response.setContentType(fileMimeType);

		try
		{
	        bi = ImageIO.read(imgFile);
		} catch (Exception e1)
		{
	        // 일부 JPG파일을 제대로 읽지 못하는 오류에 대한 처리(Java에서 인식 가능한 Standard format 으로 변환하여 인식 오류를 줄임)
	        // Caused by: Numbers of source Raster bands and source color space components do not match
	        try
	        {
	        	bi = CmFileUtils.readImage(imgFile);
	        } catch (Exception e2)
	        {
	        	e2.printStackTrace();
	        }
		}

		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, fileExt, out);
		out.close();
	}

}
