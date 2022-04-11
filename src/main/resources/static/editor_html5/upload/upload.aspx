<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="upload.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>TSE UPLOAD</title>
		<!--script src="../../config/crossdomain.js" type="text/javascript" language="javascript"></script-->
		<script type="text/javascript">
			window.onload = function()//window.onload : 페이지가 로드되고 난 후에 발생하는 이벤트 <body onload="">태그와 동일한 기능을 수행
			{
				var strContentType = document.getElementById("divContentType").value;//.innerHTML->html내의 모든 요소를 가져올 수 잇다. 
				var strImagePath = document.getElementById("divImagePath").value;/*strContentType = sContentType
																					   strImagePath = sUploadedPath */
				

				if (strImagePath != undefined && strImagePath != "")//strImagePath가 존재하고 또한 값이 있다면
				{
					var strLocation = location.href;//페이지 값이 반환 ex)document.write(location.href); -> http://localhost/testcode.asp 반환
					strLocation = strLocation.replace("/uploaddot", "");//닷넷폴더는 한수준 아래에 있으니깐..
					var index = strLocation.lastIndexOf("/");//그러므로 위 반환값의 /의 마지막위치 뒤쪽에 상대경로값을 붙여주면 된다.

					if (index > 0) {
						strLocation = strLocation.substring(0, index+1) + document.getElementById("divImagePath").value;
					} else {
						strLocation = parent.tseBasePath + "/upload/" + document.getElementById("divImagePath").value;
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
					alert("에러발생");
				}
			};
		</script>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        <input type="hidden" id="divContentType"  runat="server" />
        <input type="hidden" id="divImagePath" runat="server" />
        <br />
    </div>
    </form>
</body>
</html>
