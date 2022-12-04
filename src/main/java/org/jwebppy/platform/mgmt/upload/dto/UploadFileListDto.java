package org.jwebppy.platform.mgmt.upload.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class UploadFileListDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 7896823726474538055L;

	private String uflSeq;
	private String ufSeq;
	private String tseq;
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
			String now = CmDateFormatUtils.defaultZonedFormat(LocalDateTime.now(), MgmtCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);

			return AES256Cipher.getInstance().encode(now + PlatformConfigVo.DELIMITER + uflSeq);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
