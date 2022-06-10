package org.jwebppy.platform.core.security.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper
{
	@Value("${platform.service}")
	private String PLATFORM_SERVICE;

	public boolean isDoobizUser(String username, String password)
	{
		String KEY = "Infracore";
		String IV = "Doosan";

		String protocol = "http";
		String host = "10.249.27.18";//EPQ AP
		int port = 50000;
		String path = "/irj/servlet/prt/portal/prtroot/doobiz.portal.auth.DoobizAuthenticationComponent";

		if (CmStringUtils.equals(PLATFORM_SERVICE, "PRD"))
		{
			host = "10.249.16.182";//EPP AP2
		}

		BufferedReader bufferedReader = null;

        try
		{
        	path += "?token=" + URLEncoder.encode(new StringEncrypter(KEY, IV).encrypt(CmStringUtils.upperCase(username) + ":" + password + ":" + System.currentTimeMillis()), "UTF-8");

        	System.err.println("Doobiz Login - protocol: " + protocol + ", host:" + host + ", port:" + port + ", path:" + path);

        	URL url = new URL(protocol, host, port, path);
        	HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        	conn.setRequestMethod("GET");

        	System.err.println("Doobiz Login - try to connect");

        	conn.connect();

        	System.err.println("Doobiz Login - connection success");

			bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String readLine = null;

			while ((readLine = bufferedReader.readLine()) != null)
			{
				response.append(readLine); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
			}

			bufferedReader.close();
			bufferedReader = null;

			System.err.println("Doobiz Login Result:" + response.toString());

			if (CmStringUtils.equals(response.toString(), PlatformCommonVo.SUCCESS))
			{
				return true;
			}
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
        finally
        {
        	if (bufferedReader != null)
        	{
        		try
        		{
					bufferedReader.close();
				}
        		catch (IOException e)
        		{
					e.printStackTrace();
				}

        		bufferedReader = null;
        	}
		}

		return false;
	}
}
