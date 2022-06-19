package org.jwebppy.platform.mgmt.upload.dto;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmDateFormatUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UploadFileListDto extends GeneralDto
{
	private static final long serialVersionUID = 7896823726474538055L;

	private String uflSeq;
	private String ufSeq;
	private String tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private long fileSize;
	private String fgDelete;

	public String getFullOriginName()
	{
		return originName + "." + extension;
	}

	public String getDownloadKey()
	{
		try
		{
			return AES256Cipher.getInstance().encode(CmDateFormatUtils.now() + PlatformConfigVo.DELIMITER + uflSeq);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
