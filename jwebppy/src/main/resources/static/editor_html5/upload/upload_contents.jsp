
<%@page contentType = "text/html;charset=utf-8" %>
<%@page import = "java.io.*, java.util.*" %>
<%!
	public String usf_replace(String src, String oldstr, String newstr)
	{
		if (src == null) return null;
		StringBuffer dest = new StringBuffer("");
		try
		{
			int  len = oldstr.length();
			int  srclen = src.length();
			int  pos = 0;
			int  oldpos = 0;

			while ((pos = src.indexOf(oldstr, oldpos)) >= 0)
			{
				dest.append(src.substring(oldpos, pos));
				dest.append(newstr);
				oldpos = pos + len;
			}

			if (oldpos < srclen)
				dest.append(src.substring(oldpos, srclen));
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return dest.toString();
	}

	public boolean FileExists(String sPath) throws Exception
	{
		File file = new File(sPath);
		if (file.exists())
			return true;
		else
			return false;
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
	
	 
	/**
	 * 파일의 확장자를 체크하여 필터링된 확장자를 포함한 파일인 경우에 true를 리턴한다.
	 * @param extension
	 * */
	public static boolean checkWhiteList(String extension) {
	
	    String ext = extension.substring(extension.lastIndexOf(".") + 1, extension.length());
	    
	    final String[] WHITE_EXTENSION = { "jpg", "jpeg", "gif", "png", "mp4", "swf", "bmp" };
	
	    int len = WHITE_EXTENSION.length;
	    
	    for (int i = 0; i < len; i++) {
	    	
	        if (ext.equalsIgnoreCase(WHITE_EXTENSION[i])) {
	            return true; 
	        }
	    }
	    
	    return false;
	}
	 
	private String cleanXSS(String val) {
		
		val = val.replaceAll("<", "&lt;").replaceAll(">", "&gt;");		  
		val = val.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");		  
		val = val.replaceAll("'", "&#39;");        		  
		val = val.replaceAll("eval\\((.*)\\)", ""); 		  
		val = val.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");		  
		val = val.replaceAll("script", "");         
		  
		return val;
	}
	
	 
	public static String concatenate2(String arg1, String arg2) {
		
		String res = "";
			
		res = res.concat(arg1);
		res = res.concat(arg2);		
		
		return res;
	} 
	
	public static String concatenate3(String arg1, String arg2, String arg3) {
		
		String res = "";
			
		res = res.concat(arg1);
		res = res.concat(arg2);
		res = res.concat(arg3);
		
		return res;
	}
	
	
	public static String concatenate5(String arg1, String arg2, String arg3, String arg4, String arg5) {
		
		String res = "";
			
		res = res.concat(arg1);
		res = res.concat(arg2);
		res = res.concat(arg3);
		res = res.concat(arg4);
		res = res.concat(arg5);
		
		return res;
	}	 
	
%>
<%
	// 파일 용량 제한 : JSP 단에서는 2^31-1 bytes로 (integer max, 2147483647 bytes == 2GB - 1 byte)
	int sizeLimit = (2 * 1024 * 1024 * 1024 - 1);

	// 업로드 폴더 경로 : 날짜별로 폴더 생성
	String sDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
	String savePath = getServletContext().getRealPath(request.getServletPath());
	int pos = -1;
	if ((pos = savePath.lastIndexOf("/")) > 0)
	{
		savePath = savePath.substring(0, pos+1) + sDate;
	}
	else if ((pos = savePath.lastIndexOf("\\")) > 0)
	{
		savePath = savePath.substring(0, pos+1) + sDate;
	}

	String saveRelativePath = request.getRequestURI();
	pos = saveRelativePath.lastIndexOf("/");
	saveRelativePath = saveRelativePath.substring(0, pos+1) + sDate;
	
	
	// 업로드 폴더 생성
	File dir = new File(savePath);
	dir.mkdir();

	// Windows file system인 경우 MultipartRequest에서 인식할 수 있는 경로 스타일로 바꿔줘야..
	if ((pos = savePath.lastIndexOf("/")) == -1)
	{
		if ((pos = savePath.indexOf(":\\")) > 0)
		{
			// TOMCAT에서는 드라이브 명 ("D:\\") 굳이 제거하지 않아도 잘 인식하는 듯..
			// 나중에 혹시나 싶어서 코멘트 남겨 둠..
			// savePath = "/" + savePath.substring(pos+2, savePath.length());
			savePath = usf_replace(savePath, "\\", "/");
		}
	}

	
	// content type
	// 2009.12.22 bhkwon : multipart/form-data로 POST한 경우,
	//                     일반 request.getParameter() 메소드로는 인자 값을 받아올 수 없다.
	//                     ==> cos(com.oreilly.servlet)의 MultipartRequest 객체를 사용하면 된다.
	String sBase64 = request.getParameter("clip_contents");
	String sRootId = request.getParameter("xfe_root_id");
	//String sExtension = "." + request.getParameter("file_extension");
	String sExtension = request.getParameter("file_extension");
	String sFileNameHead = "IMG_";
	
	String sUploadedPath = "";	
	String saveFilePath = "";
		

	if(sRootId != null && !sRootId.equals("")) {
		
		sRootId = cleanXSS(sRootId);
	}

	
	if(sExtension != null && !sExtension.equals("")) {
		
		sExtension = cleanXSS(sExtension);
		
		sExtension = concatenate2(".", sExtension);
	}
	
	
	if(checkWhiteList(sExtension)) {
	
		byte[] imageByte;
	
		// 파일 이름 규칙 적용
		String sTime = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
		String sNewFName = concatenate3(sFileNameHead, sTime, sExtension);
		
		int index = 1;
		while (FileExists(savePath + "/" + sNewFName) && index < 100)
		{
			//sNewFName = sFileNameHead + sTime + "_" + index + sExtension;
			
			sNewFName = concatenate5(sFileNameHead, sTime, "_", String.valueOf(index), sExtension);
			
			index += 1;
		}
		
		//String sUploadedPath = saveRelativePath + "/" + sNewFName;	
		//String saveFilePath = sDate + "/" + sNewFName;
		
		sUploadedPath = concatenate3(saveRelativePath, "/", sNewFName);	
		saveFilePath = concatenate3(sDate, "/", sNewFName);
		
		
		
		imageByte = decode(sBase64);
		
		
		FileOutputStream fileOuputStream = new FileOutputStream(concatenate3(savePath, "/", sNewFName)); 
		fileOuputStream.write(imageByte);
		fileOuputStream.close();
	}		
	
	
	/*
	try {
		
		 
	    in = new FileInputStream(new File(savePath + "/" + sNewFName));
	    fos = new FileOutputStream(sUploadedPath);
	    bis = new BufferedInputStream(in);
	 
	    bos = new BufferedOutputStream(fos);
	    
	    int len = 0;
	    
	    while ((len = bis.read(imageByte)) >= 0) {
	        bos.write(imageByte, 0, len);
	    }
	    
    } catch (Exception e) {
        LOG.info(e.toString());
    } finally {
        try {
     
            bos.close();
            bis.close();
            fos.close();
            in.close();
        } catch (Exception e) {
        	
        	
        }
    }
	
	*/
	
	
	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<!--script src="../config/crossdomain.js" type="text/javascript" language="javascript"></script-->
		<title>XFE UPLOAD</title>
		<script type="text/javascript">
			window.onload = function()
			{
				//var strContentType = document.getElementById("divContentType").innerHTML;
				var strImagePath = document.getElementById("divImagePath").innerHTML;
				var root_id = "<%=sRootId%>";
				
				if (strImagePath != undefined && strImagePath != "")
				{
					var path = "<%=saveFilePath%>";
					var strLocation = location.href;
					var index = strLocation.lastIndexOf("/");
					
					if (index > 0) {
						strLocation = strLocation.substring(0, index+1) + path;
					} else {
						console.log('upload path error.');
						//strLocation = parent.tseBasePath + "/upload/" + "<?php echo $sDate . "/" . $file_name ?>";
					}				    				
					
					/**
					 * 파일 명을 img 태그의 alt 에 넣어준다. 
					 */
					var file_name = strLocation.substring(strLocation.lastIndexOf('/') + 1);	
					
					var str = '<img src="' + strLocation + '" alt="' + file_name + '">';
												
					
					var range = null;
					
					if(parent._xfe_object[root_id].xfeStackObject.xfeFocusoutRange) {
				    	range = parent._xfe_object[root_id].xfeStackObject.xfeFocusoutRange;
				    } else {
				    				    
					    if(parent.xfeBrowserFlag.isIE()){
					    	
					        if(parent.xfeBrowserFlag.getBrowserVersion() < 11) {
					        	
					        	if(parent._xfe_object[root_id].xfeStackObject.xfeDocument.selection) {
				        			range = parent._xfe_object[root_id].xfeStackObject.xfeDocument.selection.createRange();	
					        	} else {
					        		range = parent._xfe_object[root_id].xfeStackObject.xfeSavedLastRange;
					        	}
					            
					        } else {
					            //range = parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().getRangeAt(0);
					            
					            if(parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().rangeCount > 0) {
					            	range = parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().getRangeAt(0);
				            	} else {
				            		range = parent._xfe_object[root_id].xfeStackObject.xfeSavedLastRange;
				            	}
					        }			                        
					        
					    } else {
					    	
					        if(parent._xfe_object[root_id].xfeStackObject.xfeDocument.getSelection) {				                 
					             //range = parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().getRangeAt(0);
					             
					             if(parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().rangeCount > 0) {
					            	range = parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().getRangeAt(0);
				            	} else {
				            		range = parent._xfe_object[root_id].xfeStackObject.xfeSavedLastRange;
				            	}
					        }               
					    }				
				    }
					
				    parent._xfe_object[root_id].xfeContentsHandler.insertHTML(str, range);
					
				}
		
			};
		</script>
	</head>
	<body>		
		<div id="divImagePath"><%=sUploadedPath%></div>				
	</body>
</html>
