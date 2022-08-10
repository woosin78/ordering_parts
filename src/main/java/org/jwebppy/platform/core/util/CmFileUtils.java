package org.jwebppy.platform.core.util;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class CmFileUtils extends FileUtils
{
	public static BufferedImage readImage(File file) throws IOException
	{
		InputStream stream = new FileInputStream(file);
		//Iterator<ImageReader> imageReaders = ImageIO.getImageReadersBySuffix("jpg");
		Iterator<ImageReader> imageReaders = ImageIO.getImageReadersBySuffix(FilenameUtils.getExtension(file.getName()));
		ImageReader imageReader = imageReaders.next();
		ImageInputStream iis = ImageIO.createImageInputStream(stream);
		imageReader.setInput(iis, true, true);
		Raster raster = imageReader.readRaster(0, null);
		int w = raster.getWidth();
		int h = raster.getHeight();

		BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int rgb[] = new int[3];
		int pixel[] = new int[3];
		for (int x = 0; x < w; x++)
		{
			for (int y = 0; y < h; y++)
			{
				raster.getPixel(x, y, pixel);
				int Y = pixel[0];
				int CR = pixel[1];
				int CB = pixel[2];
				toRGB(Y, CB, CR, rgb);
				int r = rgb[0];
				int g = rgb[1];
				int b = rgb[2];
				int bgr = ((b & 0xFF) << 16) | ((g & 0xFF) << 8) | (r & 0xFF);
				result.setRGB(x, y, bgr);
			}
		}
		return result;
	}

	// Based on http://www.equasys.de/colorconversion.html
	private static void toRGB(int y, int cb, int cr, int rgb[])
	{
		float Y = y / 255.0f;
		float Cb = (cb - 128) / 255.0f;
		float Cr = (cr - 128) / 255.0f;

		float R = Y + 1.4f * Cr;
		float G = Y - 0.343f * Cb - 0.711f * Cr;
		float B = Y + 1.765f * Cb;

		R = Math.min(1.0f, Math.max(0.0f, R));
		G = Math.min(1.0f, Math.max(0.0f, G));
		B = Math.min(1.0f, Math.max(0.0f, B));

		int r = (int) (R * 255);
		int g = (int) (G * 255);
		int b = (int) (B * 255);

		rgb[0] = r;
		rgb[1] = g;
		rgb[2] = b;
	}

	/** Read the given binary file, and return its contents as a byte array. */
	public static String readBinaryFileContent(String aInputFileName)
	{
		File file = new File(aInputFileName);
		String fileContent = "";
		byte[] result = null;
		try
		{
			InputStream input = new BufferedInputStream(new FileInputStream(file));
			result = readAndClose(input);
			if (result != null && result.length > 0)
			{
				fileContent = new String(result);
			}
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		return fileContent;
	}

	/**
	 * Read an input stream, and return it as a byte array.
	 */
	private static byte[] readAndClose(InputStream aInput)
	{
		byte[] bucket = new byte[32 * 1024];
		ByteArrayOutputStream result = null;
		try
		{
			try
			{
				result = new ByteArrayOutputStream(bucket.length);
				int bytesRead = 0;
				while (bytesRead != -1)
				{
					// aInput.read() returns -1, 0, or more :
					bytesRead = aInput.read(bucket);
					if (bytesRead > 0)
					{
						result.write(bucket, 0, bytesRead);
					}
				}
			}
			finally
			{
				aInput.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return result.toByteArray();
	}
}
