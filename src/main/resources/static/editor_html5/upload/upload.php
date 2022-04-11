<html>
  <head>
    <title>XFE UPLOAD</title>

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

    // String for the IMG
    $sContentType = $_POST["content_type"]; //"image", "flash"
    
    // XFE 2.5 추가
    $dialogType = $_POST["dialog_type"];
	
	
	
    $sImagePath = "";
    $sDate = date("Ymd");
    $sTime = date("His");
    $sErrorLog = "";
	

    // Check post_max_size (http://us3.php.net/manual/en/features.file-upload.php#73762)
    $POST_MAX_SIZE = ini_get('post_max_size');
    $unit = strtoupper(substr($POST_MAX_SIZE, -1));
    $multiplier = ($unit == 'M' ? 1048576 : ($unit == 'K' ? 1024 : ($unit == 'G' ? 1073741824 : 1)));

    if ((int)$_SERVER['CONTENT_LENGTH'] > $multiplier*(int)$POST_MAX_SIZE && $POST_MAX_SIZE)
    {
      header("HTTP/1.1 500 Internal Server Error"); // This will trigger an uploadError event in SWFUpload
      $sErrorLog = "POST exceeded maximum allowed size.";
    }
    else if ($sContentType != "image" && $sContentType != "flash")
    {
      $sErrorLog = "Unknwon content type : " . $sContentType;
    }
    else
    {
      // Settings
      //$save_path = getcwd() . "/uploads/";				// The path were we will save the file (getcwd() may not be reliable and should be tested in your environment)
      $save_path = getcwd() . "/" . $sDate . "/";
      if (!file_exists($save_path)) {
        mkdir($sDate);
      }
      $upload_name = "FILE_PATH";
      $max_file_size_in_bytes = 2147483647;				// 2GB in bytes
      
      
      
      // XFE 2.5 에서 수정.
      if($sContentType == "flash") {
		$extension_whitelist = array("swf");
	  } else if($sContentType == "media") {
	  	$extension_whitelist = array("mp4");
	  } else if($sContentType == "image") {
	  	$extension_whitelist = array("jpg", "jpeg", "gif", "png");
	  }
      
      //$extension_whitelist = ($sContentType == "flash") ? array("swf") : array("jpg", "jpeg", "gif", "png");	// Allowed file extensions
      
      
      
      $valid_chars_regex = '.A-Z0-9_ !@#$%^&()+={}\[\]\',~`-';				// Characters allowed in the file name (in a Regular Expression format)

      // Other variables	
      $MAX_FILENAME_LENGTH = 260;
      $file_name = "";
      $file_extension = "";
      $uploadErrors = array(
        0=>"There is no error, the file uploaded with success",
        1=>"The uploaded file exceeds the upload_max_filesize directive in php.ini",
        2=>"The uploaded file exceeds the MAX_FILE_SIZE directive that was specified in the HTML form",
        3=>"The uploaded file was only partially uploaded",
        4=>"No file was uploaded",
        6=>"Missing a temporary folder"
      );

      // Validate the upload
      if (!isset($_FILES[$upload_name]))
      {
        //$sErrorLog = "No upload found in \$_FILES for " . $upload_name;
        $sErrorLog = "No upload found in \$_FILES for " . $_FILES["FILE_PATH"]["size"];
      }
      else if (isset($_FILES[$upload_name]["error"]) && $_FILES[$upload_name]["error"] != 0)
      {
        $errorCode = $_FILES[$upload_name]["error"];
        $sErrorLog = $uploadErrors[$errorCode];
      }
      else if (!isset($_FILES[$upload_name]["tmp_name"]) || !@is_uploaded_file($_FILES[$upload_name]["tmp_name"]))
      {
        $sErrorLog = "Upload failed is_uploaded_file test.";
      }
      else if (!isset($_FILES[$upload_name]['name']))
      {
        $sErrorLog = "File has no name.";
      }
      else
      {

        // Validate the file size (Warning: the largest files supported by this code is 2GB)
        $file_size = @filesize($_FILES[$upload_name]["tmp_name"]);
        if (!$file_size || $file_size > $max_file_size_in_bytes) {
          $sErrorLog = "File exceeds the maximum allowed size";
        }
        else if ($file_size <= 0)
        {
          $sErrorLog = "File size outside allowed lower bound";
        }
        else
        {
/*
          // Validate file name (for our purposes we'll just remove invalid characters)
          $file_name = preg_replace('/[^'.$valid_chars_regex.']|\.+$/i', "", basename($_FILES[$upload_name]['name']));
          if (strlen($file_name) == 0 || strlen($file_name) > $MAX_FILENAME_LENGTH) {
            $sErrorLog = "Invalid file name";
            exit(0);
          }

          // Validate that we won't over-write an existing file
          if (file_exists($save_path . $file_name)) {
            $sErrorLog = "File with this name already exists";
            exit(0);
          }
*/

          // Validate file extension
          $path_info = pathinfo($_FILES[$upload_name]['name']);
          $file_extension = $path_info["extension"];
          $is_valid_extension = false;
          foreach ($extension_whitelist as $extension) {
            if (strcasecmp($file_extension, $extension) == 0) {
              $is_valid_extension = true;
              break;
            }
          }
          if (!$is_valid_extension) {
            $sErrorLog = "Invalid file extension : " . $file_extension;
          }
          else
          {

            $iTmpIndex = 1;
			
			
			
			//<---------- XFE 2.5 처리.
			if($sContentType == 'flash') {
				$file_name = 'FLA_';
			} else if($sContentType == 'media') {
				$file_name = 'MED_';
			} else {
				$file_name = 'IMG_';
			}
			
			$file_name = $file_name . $sTime . "." . $file_extension;
			
			while (file_exists($save_path . $file_name)) {
			  		
			  	if($sContentType == 'flash') {
					$file_name = 'FLA_';
				} else if($sContentType == 'media') {
					$file_name = 'MED_';
				} else {
					$file_name = 'IMG_';
				}
				
				$file_name = $file_name . $sTime . "_" . (string)$iTmpIndex . "." . $file_extension;
			  	$iTmpIndex += 1;
			}
			//<------------ XFE 2.5 처리 끝.
			
			
			/**  XFE 2.3 이하 버전
            $file_name = ($sContentType == "flash" ? "FLA_" : "IMG_") . $sTime . "." . $file_extension;
            while (file_exists($save_path . $file_name)) {
              $file_name = ($sContentType == "flash" ? "FLA_" : "IMG_") . $sTime . "_" . (string)$iTmpIndex . "." . $file_extension;
              $iTmpIndex += 1;
            }
			*/

			list($width, $height, $type, $attr)= getimagesize($_FILES[$upload_name]["tmp_name"]); 
			
            // Validate file contents (extension and mime-type can't be trusted)
            /*
              Validating the file contents is OS and web server configuration dependant.  Also, it may not be reliable.
              See the comments on this page: http://us2.php.net/fileinfo

              Also see http://72.14.253.104/search?q=cache:3YGZfcnKDrYJ:www.scanit.be/uploads/php-file-upload.pdf+php+file+command&hl=en&ct=clnk&cd=8&gl=us&client=firefox-a
              which describes how a PHP script can be embedded within a GIF image file.

              Therefore, no sample code will be provided here.  Research the issue, decide how much security is
              needed, and implement a solution that meets the needs.
            */


            // Process the file
            /*
              At this point we are ready to process the valid file. This sample code shows how to save the file. Other tasks
              could be done such as creating an entry in a database or generating a thumbnail.

              Depending on your server OS and needs you may need to set the Security Permissions on the file after it has
              been saved.
            */
            if (!@move_uploaded_file($_FILES[$upload_name]["tmp_name"], $save_path.$file_name)) {
              $sErrorLog = "File could not be saved.";
            }
            else
            {
              $sImagePath = $file_name;
            }
          }
        }
      }
    }

  /* Handles the error output. This error message will be sent to the uploadSuccess event handler.  The event handler
     will have to check for any error messages and react as needed. */
    function HandleError($message) {
      echo $message . "<br/>";
    }
?>

    <script type="text/javascript">
		window.onload = function () {
			
			var strContentType = "<?php echo $sContentType ?>";
			var strImagePath = "<?php echo $sImagePath ?>";
			
			// XFE 2.5 추가.
			var dialogType = "<?php echo $dialogType ?>";
			
			
			if (strImagePath != undefined && strImagePath != "") {
				var strLocation = location.href;
				var index = strLocation.lastIndexOf("/");
			
				if (index > 0) {
					strLocation = strLocation.substring(0, index+1) + "<?php echo $sDate . "/" . $file_name ?>";
				} else {
					console.log('upload path error.');            
				}
			
				if (strContentType == "flash") {					
			    
					if(parent.insertFlash) {
						parent.insertFlash.setFlash(strLocation);	
					} else {
						parent.parent.insertFlash.setFlash(strLocation);
					}
			    
				} else if (strContentType == 'media') {
					
					if(parent.insertMedia) {
						parent.insertMedia.setMedia(strLocation);
					} else {
						parent.parent.insertMedia.setMedia(strLocation);
					}
					
				} else if (strContentType == "image") {					
			  	
			  		if(dialogType === 'background') {
			  		
				  		if(parent.insertBackground) {
							parent.insertBackground.setBgImage(strLocation);
						} else {
							parent.parent.insertBackground.setBgImage(strLocation);
						}
			  		} else {
			  		
				  		if(parent.insertImage) {
							parent.insertImage.setImage(strLocation);
						} else {
							parent.parent.insertImage.setImage(strLocation);
						}
			  		}					
				}	
			
			} else {
				alert("Server Error : <?php echo $sErrorLog ?>");
			}
			    
		};
    </script>

  </head>
  <body>    
  </body>
</html>
