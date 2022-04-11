package org.jwebppy.portal.iv.upload.dto;

import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.Formatter;
import org.jwebppy.portal.common.PortalConfigVo;
import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpUploadFileListDto extends IvGeneralDto
{
	private static final long serialVersionUID = 7896823726474538055L;

	private String uflSeq;
	private String ufSeq;
	private String tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private long fileSize;

	public String getFullOriginName()
	{
		return originName + "." + extension;
	}

	public String getDownloadKey()
	{
		try
		{
			return AES256Cipher.getInstance().encode(CmDateFormatUtils.now() + PortalConfigVo.DELIMITER + uflSeq);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public String getDisplayFileSize()
	{
		return Formatter.getDisplayFileSize(fileSize);
	}
}
