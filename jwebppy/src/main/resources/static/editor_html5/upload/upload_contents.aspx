<%@ Page Language="C#" AutoEventWireup="true" CodeFile="upload_contents.aspx.cs" Inherits="XFEditor_upload_upload_contents" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>제목 없음</title>
    <script type="text/javascript">
	
		window.onload = function()
		{
						
			var strImagePath = document.getElementById("divImagePath").value;
			var root_id = document.getElementById('divRootId').value;
			
			if (strImagePath != undefined && strImagePath != "") {
				
				var str = '<img src="' + strImagePath + '">';
									
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
				
			}

		
		};
	</script>
		
		
</head>
<body>
    <form id="form1" runat="server">
    
    <input type="hidden" id="divImagePath" runat="server"/>
    <input type="hidden" id="divRootId" runat="server"/>
  
    </form>
</body>
</html>
