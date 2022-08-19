package org.jwebppy.portal.iv.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.util.CmFileUtils;

public class ImageLoadUtils
{
	public static void load(String filePath, HttpServletResponse response) throws IOException
	{
		File file = Paths.get(filePath).toFile();
		String contentType = Files.probeContentType(Paths.get(filePath));

		if (!contentType.startsWith("image"))
		{
			throw new IOException("This is not an image file.");
		}

		BufferedImage bufferedImage = null;

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
	        catch (IOException e1)
	        {
	        	throw e1;
	        }
		}

		OutputStream outputStream = null;

		try
		{
			response.setContentType(contentType);

			outputStream = response.getOutputStream();
			ImageIO.write(bufferedImage, FilenameUtils.getExtension(filePath), outputStream);

			outputStream.close();
			outputStream = null;
		}
		catch (IOException e)
		{
			if (outputStream != null)
			{
				try { outputStream.close(); outputStream = null; } catch (IOException e1) { outputStream = null; throw e1; }
			}
		}
	}
}
