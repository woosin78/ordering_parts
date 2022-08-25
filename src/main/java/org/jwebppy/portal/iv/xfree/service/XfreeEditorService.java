package org.jwebppy.portal.iv.xfree.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.xfree.dto.XfreeUploadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class XfreeEditorService extends IvGeneralService
{
	@Value("${xfree.upload.path}")
	private String XFREE_UPLOAD_PATH;

	@Value("${xfree.image.viewer}")
	private String XFREE_IMAGE_VIEWER_PATH;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	public XfreeUploadDto upload(MultipartHttpServletRequest request) throws Exception
	{
		MultipartFile multiPartFile = request.getFile("FILE_PATH");

		String contentType = multiPartFile.getContentType();

		if (!CmStringUtils.startsWithIgnoreCase(contentType, "image"))
		{
			throw new InvalidContentTypeException();
		}

		String originFileName = multiPartFile.getOriginalFilename();

		File dir = new File(XFREE_UPLOAD_PATH);

		if (!dir.exists())
		{
			dir.mkdirs();
		}

		String savedFileName = getUsername() + "_" + System.nanoTime() + "." + FilenameUtils.getExtension(originFileName);

		multiPartFile.transferTo(new File(XFREE_UPLOAD_PATH + File.separator + savedFileName));

		XfreeUploadDto xfreeUpload = XfreeUploadDto.builder()
				.contentType(request.getParameter("content_type"))
				.dialogType(request.getParameter("dialog_type"))
				.savedFileName(savedFileName)
				.viewerPath(XFREE_IMAGE_VIEWER_PATH)
				.build();

		return xfreeUpload;
	}

	public XfreeUploadDto upload(HttpServletRequest request)
	{
		String extention = request.getParameter("file_extension");
		String contents = request.getParameter("clip_contents");
		String rootId = request.getParameter("xfe_root_id");

		if (CmStringUtils.isEmpty(extention))
		{
			return XfreeUploadDto.builder().error(i18nMessageSource.getMessage("HQP_M_NOT_ACCEPTABLE_FILE_FORMAT")).build();
		}

		if (CmStringUtils.startsWith(contents, "data:"))
		{
			return XfreeUploadDto.builder().error(i18nMessageSource.getMessage("HQP_M_ONLY_ALLOW_TO_UPLOAD_IMAGE")).build();
		}

		File dir = new File(XFREE_UPLOAD_PATH);

		if (!dir.exists())
		{
			dir.mkdirs();
		}

		String savedFileName = getUsername() + "_" + System.nanoTime() + "." + extention;

		FileOutputStream fileOuputStream = null;

		try
		{
			fileOuputStream = new FileOutputStream(XFREE_UPLOAD_PATH + File.separator + savedFileName);
			fileOuputStream.write(decode(contents));
			fileOuputStream.close();
			fileOuputStream = null;
		}
		catch (IOException e)
		{
			return XfreeUploadDto.builder().error(i18nMessageSource.getMessage("HQP_M_ONLY_ALLOW_TO_UPLOAD_IMAGE")).build();
		}
		finally
		{
			if (fileOuputStream != null)
			{
				try
				{
					fileOuputStream.close(); fileOuputStream = null;
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}

		XfreeUploadDto xfreeUpload = XfreeUploadDto.builder()
				.contentType(request.getParameter("content_type"))
				.dialogType(request.getParameter("dialog_type"))
				.savedFileName(savedFileName)
				.viewerPath(XFREE_IMAGE_VIEWER_PATH)
				.rootId(rootId)
				.build();

		return xfreeUpload;
	}

	private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private static final int[] IA = new int[256];
	static {
		Arrays.fill(IA, -1);
		for (int i = 0, iS = CA.length; i < iS; i++)
			IA[CA[i]] = i;
		IA['='] = 0;
	}

	/** Decodes a BASE64 encoded <code>String</code>. All illegal characters will be ignored and can handle both strings with
	 * and without line separators.<br>
	 * <b>Note!</b> It can be up to about 2x the speed to call <code>decode(str.toCharArray())</code> instead. That
	 * will create a temporary array though. This version will use <code>str.charAt(i)</code> to iterate the string.
	 * @param str The source string. <code>null</code> or length 0 will return an empty array.
	 * @return The decoded array of bytes. May be of length 0. Will be <code>null</code> if the legal characters
	 * (including '=') isn't divideable by 4.  (I.e. definitely corrupted).
	 * @version 2.2
	 * @author Mikael Grev
	 *         Date: 2004-aug-02
	 *         Time: 11:31:11
	 */
	public final static byte[] decode(String str)
	{
		// Check special case
		int sLen = str != null ? str.length() : 0;
		if (sLen == 0)
			return new byte[0];

		// Count illegal characters (including '\r', '\n') to know what size the returned array will be,
		// so we don't have to reallocate & copy it later.
		int sepCnt = 0; // Number of separator characters. (Actually illegal characters, but that's a bonus...)
		for (int i = 0; i < sLen; i++)  // If input is "pure" (I.e. no line separators or illegal chars) base64 this loop can be commented out.
			if (IA[str.charAt(i)] < 0)
				sepCnt++;

		// Check so that legal chars (including '=') are evenly divideable by 4 as specified in RFC 2045.
		if ((sLen - sepCnt) % 4 != 0)
			return null;

		// Count '=' at end
		int pad = 0;
		for (int i = sLen; i > 1 && IA[str.charAt(--i)] <= 0;)
			if (str.charAt(i) == '=')
				pad++;

		int len = ((sLen - sepCnt) * 6 >> 3) - pad;

		byte[] dArr = new byte[len];       // Preallocate byte[] of exact length

		for (int s = 0, d = 0; d < len;) {
			// Assemble three bytes into an int from four "valid" characters.
			int i = 0;
			for (int j = 0; j < 4; j++) {   // j only increased if a valid char was found.
				int c = IA[str.charAt(s++)];
				if (c >= 0)
				    i |= c << (18 - j * 6);
				else
					j--;
			}
			// Add the bytes
			dArr[d++] = (byte) (i >> 16);
			if (d < len) {
				dArr[d++]= (byte) (i >> 8);
				if (d < len)
					dArr[d++] = (byte) i;
			}
		}
		return dArr;
	}
}
