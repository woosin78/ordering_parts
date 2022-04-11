<html>
	<head>
		<title>XFE UPLOAD</title>
<!--script src="../config/crossdomain.js" type="text/javascript" language="javascript"></script-->
<?php
    /*
      This is an upload script for SWFUpload that attempts to properly handle uploaded files
      in a secure way.

      Notes:

      SWFUpload doesn't send a MIME-TYPE. In my opinion this is ok since MIME-TYPE is no better than
      file extension and is probably worse because it can vary from OS to OS and browser to browser (for the same file).
      The best thing to do is content sniff the file but this can be resource intensive, is difficult, and can still be fooled or inaccurate.
      Accepting uploads can never be 100% secure.

      You can't guarantee that SWFUpload is really the source of the upload.  A malicious user
      will probably be uploading from a tool that sends invalid or false metadata about the file.
      The script should properly handle this.

      The script should not over-write existing files.

      The script should strip away invalid characters from the file name or reject the file.

      The script should not allow files to be saved that could then be executed on the webserver (such as .php files).
      To keep things simple we will use an extension whitelist for allowed file extensions.  Which files should be allowed
      depends on your server configuration. The extension white-list is _not_ tied your SWFUpload file_types setting

      For better security uploaded files should be stored outside the webserver's document root.  Downloaded files
      should be accessed via a download script that proxies from the file system to the webserver.  This prevents
      users from executing malicious uploaded files.  It also gives the developer control over the outgoing mime-type,
      access restrictions, etc.  This, however, is outside the scope of this script.

      SWFUpload sends each file as a separate POST rather than several files in a single post. This is a better
      method in my opinions since it better handles file size limits, e.g., if post_max_size is 100 MB and I post two 60 MB files then
      the post would fail (2x60MB = 120MB). In SWFupload each 60 MB is posted as separate post and we stay within the limits. This
      also simplifies the upload script since we only have to handle a single file.

      The script should properly handle situations where the post was too large or the posted file is larger than
      our defined max.  These values are not tied to your SWFUpload file_size_limit setting.
    */

    // Code for Session Cookie workaround
    if (isset($_POST["PHPSESSID"])) {
      session_id($_POST["PHPSESSID"]);
    } else if (isset($_GET["PHPSESSID"])) {
      session_id($_GET["PHPSESSID"]);
    }

    session_start();

        
    $sContents = $_POST["clip_contents"];
    $sImagePath = "";
    $sDate = date("Ymd");
    $sTime = date("His");
    $sErrorLog = "";
	$file_name = "";
    $file_extension = $_POST["file_extension"];
	$root_id = $_POST["xfe_root_id"];
	
	
	
	$decoded = base64_decode($sContents);//$BB6B>8l8@B,;n(B - testing
	//$input_encoding = 'iso-2022-jp';
	//$sContentType = iconv($input_encoding, 'UTF-8', $input);
	
	// Settings
	//$save_path = getcwd() . "/uploads/";				// The path were we will save the file (getcwd() may not be reliable and should be tested in your environment)
	$save_path = getcwd() . "/" . $sDate . "/";
	if (!file_exists($save_path)) {
	  mkdir($sDate);
	}
	
	
	$iTmpIndex = 1;
    $file_name = "IMG_" . $sTime . "." . $file_extension;
	
	
    while (file_exists($save_path . $file_name)) {      
      $file_name = "IMG_" . $sTime . "_" . (string)$iTmpIndex . "." . $file_extension;
	  $iTmpIndex += 1;      
    }
	
	$file_path = $save_path . $file_name;
	
	$sImagePath = "/" . $sDate . "/" . $file_name;
	
	file_put_contents($file_path, $decoded);
	


  /* Handles the error output. This error message will be sent to the uploadSuccess event handler.  The event handler
     will have to check for any error messages and react as needed. */
    function HandleError($message) {
      echo $message . "<br/>";
    }
?>

		<script type="text/javascript">
			window.onload = function () {
				
				//document.domain = 'tagfree.com';
                
				
				var strImagePath = document.getElementById("divImagePath").innerHTML;
				var root_id = "<?php echo $root_id ?>";				                        
				
				if (strImagePath != undefined && strImagePath != "") {
					var strLocation = location.href;
					var index = strLocation.lastIndexOf("/");
					
					if (index > 0) {
						strLocation = strLocation.substring(0, index+1) + "<?php echo $sDate . "/" . $file_name ?>";
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
				    	
						if(parent.xfeBrowserFlag.isIE()) {
				    	
					        if(parent.xfeBrowserFlag.getBrowserVersion() < 11) {
					            range = parent._xfe_object[root_id].xfeStackObject.xfeDocument.selection.createRange();    
					        } else {
					        	
					        	if(parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().rangeCount > 0) {
					            	range = parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().getRangeAt(0);
				            	} else {
				            		range = parent._xfe_object[root_id].xfeStackObject.xfeSavedLastRange;
				            	}
					        }			                        
					        
					    } else {
					    	
					        if(parent._xfe_object[root_id].xfeStackObject.xfeDocument.getSelection) {
					        
					        	if(parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().rangeCount > 0) {				                 
									range = parent._xfe_object[root_id].xfeRangeHandler.getDesignDocumentSelection().getRangeAt(0);
								} else {
				            		range = parent._xfe_object[root_id].xfeStackObject.xfeSavedLastRange;
				            	}
					        }               
					    }				    	
				    }			    				
					
					parent._xfe_object[root_id].xfeContentsHandler.insertHTML(str, range);
							
					//parent._xfe_object[root_id].xfeStackObject.xfeDropRange = null;						  
				  	
				} else {
					alert("Server Error : <?php echo $sErrorLog ?>");
				}
				    
			};
		</script>
	
	</head>
	<body>		
		<div id="divImagePath"><?php echo $sImagePath ?></div>			    
	</body>
</html>
