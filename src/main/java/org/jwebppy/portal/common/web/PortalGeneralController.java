package org.jwebppy.portal.common.web;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.GeneralController;

public class PortalGeneralController extends GeneralController
{
	protected final String[] MANAGER_AUTHORITIES = {"DP_IVDO_PARTS_MANAGER", "DP_IVEX_PARTS_MANAGER", "DP_UKDO_PARTS_MANAGER", "DP_EUDO_PARTS_MANAGER"};//각법인 내부사용자

	@Override
	protected Integer getUSeq()
	{
		return UserAuthenticationUtils.getUSeq();
	}

	@Override
	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}

	public void downloadByRfc(HttpServletRequest request, HttpServletResponse response, DataList dataList, String key, String fileName)
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

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				bufferOutputStream.write((byte[])dataMap.get(key));
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

	public boolean isManager()
	{
		return UserAuthenticationUtils.hasRole(MANAGER_AUTHORITIES);
	}
}
