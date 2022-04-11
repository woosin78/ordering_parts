
<% @LANGUAGE='VBSCRIPT' CODEPAGE='65001' %>
<% 
  Dim sContent

  sContent = request("mime_contents")

  Response.Clear
  Response.AddHeader "content-type", "text/html"
  Response.AddHeader "content-disposition", "attachment; filename=제목없음.htm"

  'Response.charset = "utf-16"
  'Response.BinaryWrite Chrb(255)
  'Response.BinaryWrite Chrb(254)
  'Response.BinaryWrite sContent

  Response.charset = "utf-8"
  Response.BinaryWrite Chrb(239)
  Response.BinaryWrite Chrb(187)
  Response.BinaryWrite Chrb(191)
  Response.Write sContent

  Response.End
%>

