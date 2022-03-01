癤�
<% @LANGUAGE='VBSCRIPT' CODEPAGE='65001' %>
<% 
Dim sErrorLog
Dim sUploadedPath, sContentType
Dim sToday, sDir
Dim sHour, sMinute, sSecond, sFileName
Dim obFso, sExtension, sAbsolutePath, index
Dim theForm, theField

sUploadedPath = ""
'sToday = CDate(Date) 'yyyy-mm-dd
'sToday = Left(sToday, 4) + Mid(sToday, 6, 2) + Right(sToday, 2) 'yyyymmdd
sToday = CStr(Year(date)) + right("00" & CStr(Month(date)), 2) + right("00" &CStr(Day(date)), 2)

sDir = Server.mappath(".") & "\" & sToday

Set obFso = Server.CreateObject("Scripting.FileSystemObject")
If Not obFso.FolderExists(sDir) Then
	obFso.CreateFolder(sDir)
End If

Set theFile = Server.CreateObject("TFUpload.Form")

theFile.SetLicenseKey("xmzOzKGjC7zLnXFKvrszAWHS+vpG7CYoqwCF4uIKVmSfNfmO+9BEH6fCZ29cKEDGr3+9awfux6TLigX13UBZkEDwQZMTLhwo38AgMCk4YLPAf2hnVYNnnzdjqg9BDkZHKizTbOAckXsweAdHtAKNVExVZw8Km0PwTZYDECwlMQU=")

theFile.Init

Set oListForm = theFile.Item(1)
Set oItem = oListForm.Item(1)

sContentType = oItem.ContentType
sExtension = oItem.FileExt

' ContentType ���곕씪 sContentType 蹂�닔��媛���옣.
If Instr(LCase(sContentType), "flash") > 0 Then
	sContentType = "flash"
ElseIf Instr(LCase(sContentType), "image") > 0 Then
	sContentType = "image"
End If

If  sContentType = "flash" AND StrComp(LCase(sExtension), "swf") = 0 OR _
	sContentType = "image" AND (  		_
		StrComp(LCase(sExtension), "jpg") = 0 OR        _
		StrComp(LCase(sExtension), "jpeg") = 0 OR       _
		StrComp(LCase(sExtension), "gif") = 0 OR        _
		StrComp(LCase(sExtension), "png") = 0           _
	) Then

	sHour = hour(Now)
	If len(sHour) = 1 Then sHour = "0" & sHour
	sMinute = minute(Now)
	If len(sMinute) = 1 Then sMinute = "0" & sMinute
	sSecond = second(Now)
	If len(sSecond) = 1 Then sSecond = "0" & sSecond

	If sContentType = "flash" Then
		sFileName = "FLA_" & sHour & sMinute & sSecond
	Else
		sFileName = "IMG_" & sHour & sMinute & sSecond
	End If

	index = 1
	sUploadedPath = sToday & "\" & sFileName & "." & sExtension
	sAbsolutePath = Server.mappath(".") & "\" & sUploadedPath
	While obFso.FileExists(sAbsolutePath)
		If sContentType = "flash" Then
			sFileName = "FLA_" & sHour & sMinute & sSecond & "_" & cstr(index)
		Else
			sFileName = "IMG_" & sHour & sMinute & sSecond & "_" & cstr(index)
		End If
		sUploadedPath = sToday & "\" & sFileName & "." & sExtension
		sAbsolutePath = Server.mappath(".") & "\" & sUploadedPath
		index = index + 1
	Wend

	nSaveIndex = oItem.FileSavePath(sAbsolutePath)
	
	oItem.Save 1
	sUploadedPath = Replace(sUploadedPath, "\", "/")
Else
	sErrorLog = "Server Error : Invalid file extension"
End If
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>XFE UPLOAD</title>
		<!--script src="../config/crossdomain.js" type="text/javascript" language="javascript"></script-->
		<script type="text/javascript">
	
			window.onload = function()
			{
			
				var strContentType = document.getElementById("divContentType").innerHTML;
				var strImagePath = document.getElementById("divImagePath").innerHTML;

				if (strImagePath != undefined && strImagePath != "") {
				
					var strLocation = location.href;
					var index = strLocation.lastIndexOf("/");

					if (index > 0) {
						strLocation = strLocation.substring(0, index+1) + "<%=sUploadedPath%>";
					} else {						
						console.log('upload path error.');
					}

					if (strContentType == "flash") {
						
						if(parent.insertFlash) {
							parent.insertFlash.setFlash(strLocation);
						} else {
							parent.parent.insertFlash.setFlash(strLocation);
						}
						
						
					} else if (strContentType == "image") {
						
						if(parent.insertImage) {				
							parent.insertImage.setImage(strLocation);
						} else {
							parent.parent.insertImage.setImage(strLocation);
						}
					}
					
				} else {
				
					alert("<%=sErrorLog%>");
				}
			};
		</script>
	</head>
	<body>
		<div id="divContentType"><%=sContentType%></div>
		<div id="divImagePath"><%=sUploadedPath%></div>
		<div><%=sErrorLog%></div>
		<div id="imageWidth"><%=oItem.imageWidth%></div>
		<div id="imageHeight"><%=oItem.imageHeight%></div>	
		<div id="fileLength"><%=nFileLength%></div>	
	</body>
</html>
