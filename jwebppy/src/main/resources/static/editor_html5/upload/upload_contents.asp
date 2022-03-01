癤�
<% @LANGUAGE='VBSCRIPT' CODEPAGE='65001' %>
<% 
Dim sErrorLog
Dim sUploadedPath
Dim sToday, sDir
Dim sHour, sMinute, sSecond, sFileName
Dim obFso, sExtension, sAbsolutePath, index
Dim sContents, sRootId, Base64Decode


Dim length

' request 데이터 길이
length = Request.ServerVariables("CONTENT_LENGTH")


sContents = request.Form("clip_contents")
sRootId = request.Form("xfe_root_id")
sExtension = request.Form("file_extension") 


sUploadedPath = ""

sToday = CStr(Year(date)) + right("00" & CStr(Month(date)), 2) + right("00" &CStr(Day(date)), 2)

sDir = Server.mappath(".") & "\" & sToday

Set obFso = Server.CreateObject("Scripting.FileSystemObject")
If Not obFso.FolderExists(sDir) Then
	obFso.CreateFolder(sDir)
End If





sHour = hour(Now)

If len(sHour) = 1 Then sHour = "0" & sHour
sMinute = minute(Now)
If len(sMinute) = 1 Then sMinute = "0" & sMinute
sSecond = second(Now)
If len(sSecond) = 1 Then sSecond = "0" & sSecond


sFileName = "IMG_" & sHour & sMinute & sSecond


index = 1
sUploadedPath = sToday & "\" & sFileName & "." & sExtension

sAbsolutePath = Server.mappath(".") & "\" & sUploadedPath

While obFso.FileExists(sAbsolutePath)

	sFileName = "IMG_" & sHour & sMinute & sSecond & "_" & cstr(index)

	sUploadedPath = sToday & "\" & sFileName & "." & sExtension
	
	sAbsolutePath = Server.mappath(".") & "\" & sUploadedPath
	
	index = index + 1
	
Wend



sUploadedPath = Replace(sUploadedPath, "\", "/")

	

	Set tmpDoc = Server.CreateObject("MSXML2.DomDocument")
	Set nodeB64 = tmpDoc.CreateElement("b64")
    nodeB64.DataType = "bin.base64" ' stores binary as base64 string
    'nodeB64.Text = Mid(base64String, InStr(base64String, ",") + 1) ' append data text (all data after the comma)
    nodeB64.Text = sContents


	Const adTypeBinary = 1
	Const adSaveCreateOverWrite = 2
	  
	'Create Stream object
	Dim BinaryStream
	Set BinaryStream = CreateObject("ADODB.Stream")
	  
	  
	BinaryStream.Open
	  
	'Specify stream type - we want To save binary data.
	BinaryStream.Type = adTypeBinary
	  
	'Open the stream And write binary data To the object	
	BinaryStream.Write nodeB64.NodeTypedValue
	
	  
	BinaryStream.SaveToFile sAbsolutePath, adSaveCreateOverWrite
      
    BinaryStream.Close
    set BinaryStream = Nothing
    




%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>XFE UPLOAD</title>
		<!--script src="../config/crossdomain.js" type="text/javascript" language="javascript"></script-->
		<script type="text/javascript">
	
			window.onload = function()
			{
							
				var strImagePath = document.getElementById("divImagePath").innerHTML;
				var root_id = document.getElementById('divRootId').innerHTML;

			

				if (strImagePath != undefined && strImagePath != "") {
				
					var strLocation = location.href;
					var index = strLocation.lastIndexOf("/");

					if (index > 0) {
						strLocation = strLocation.substring(0, index+1) + "<%=sUploadedPath%>";
					} else {
						strLocation = parent.tseBasePath + "/upload/" + "<%=sUploadedPath%>";
					}
					
					
					var str = '<img src="' + strLocation + '">';
										
					var range = null;
				    
				    if(parent._xfe_object[root_id].xfeStackObject.xfeFocusoutRange) {
				    	range = parent._xfe_object[root_id].xfeStackObject.xfeFocusoutRange;
				    } else {
					    
					    if(parent.xfeBrowserFlag.isIE()) {
					    
					        if(parent.xfeBrowserFlag.getBrowserVersion() < 11) {
					            range = parent._xfe_object[root_id].xfeStackObject.xfeDocument.selection.createRange();    
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
					
				} else {
					
				}
			};
		</script>
	</head>
	<body>		
		<div id="divImagePath"><%=sUploadedPath%></div>		
	</body>
</html>
