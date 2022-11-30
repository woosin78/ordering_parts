package org.jwebppy.portal.common.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadUtils
{
	public void downloadByRfc(HttpServletRequest request, HttpServletResponse response, List<Map<String, byte[]>> dataList, String key, String fileName)
	{
		BufferedOutputStream bufferOutputStream = null;

		try
		{
			String client = request.getHeader("User-Agent");

			if (client.indexOf("MSIE") != -1)
			{
				response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
			}
			else if (client.indexOf("Edge") != -1)
			{
				response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
			}
			else
			{
			    // 한글 파일명 처리
				response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
			}

			response.setContentType("application/octet-stream; charset=UTF-8");
			response.setHeader("Content-Transfer", "binary");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");//$.fileDownload callback 처리를 위해 추가되어야 함

			bufferOutputStream = new BufferedOutputStream (response.getOutputStream());

			for (Map<String, byte[]> dataMap: dataList)
			{
				bufferOutputStream.write(dataMap.get(key));
			}

			bufferOutputStream.flush();
			bufferOutputStream.close();
			bufferOutputStream = null;
		}
		catch (IOException e)
		{
			try
			{
				if (bufferOutputStream != null)
				{
					bufferOutputStream.close();
					bufferOutputStream = null;
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
