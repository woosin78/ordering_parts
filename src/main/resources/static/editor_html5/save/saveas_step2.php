
<?php
  // Code for Session Cookie workaround
  if (isset($_POST["PHPSESSID"])) {
    session_id($_POST["PHPSESSID"]);
  } else if (isset($_GET["PHPSESSID"])) {
    session_id($_GET["PHPSESSID"]);
  }

  session_start();

  // String for the saving content
  $sContent = $_POST["mime_contents"];

  header("content-type: text/html; charset=utf-8");
  header("content-disposition: attachment; filename=제목없음.htm");
  echo $sContent;
?>
