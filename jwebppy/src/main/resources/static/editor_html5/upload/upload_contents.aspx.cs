using System;
using System.Collections;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;
using System.IO;

public partial class XFEditor_upload_upload_contents : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        string saveLocation = Server.MapPath(".");        
        string base64 = Request.Form["clip_contents"];
        string extension = Request.Form["file_extension"];
        string root_id = Request.Form["xfe_root_id"];
        string strToday = DateTime.Now.ToString("yyyyMMdd");
        string strSaveUrl = "";

        string strHours = DateTime.Now.TimeOfDay.Hours.ToString();
        string strMinutes = DateTime.Now.TimeOfDay.Minutes.ToString(); 
        string strSeconds = DateTime.Now.TimeOfDay.Seconds.ToString();

        if (strHours.Length == 1)
        {
            strHours = "0" + strHours;
        }

        if (strMinutes.Length == 1)
        {
            strMinutes = "0" + strMinutes;
        }

        if (strSeconds.Length == 1)
        {
            strSeconds = "0" + strSeconds;
        }

        string fileName = "IMG_" + strHours + strMinutes + strSeconds;

        if (extension != "")
        {
            fileName = fileName + "." + extension;
        }
        else
        {
            fileName = fileName + ".png";
        }

        strSaveUrl = HttpContext.Current.Request.Url.AbsoluteUri;

        strSaveUrl = strSaveUrl.Substring(0, strSaveUrl.LastIndexOf("/"));
        
        strSaveUrl = strSaveUrl + "/" + strToday + "/" + fileName;

       

        saveLocation = saveLocation + "/" + strToday;

        if (!Directory.Exists(saveLocation))
        {
            Directory.CreateDirectory(saveLocation);
        }

        saveLocation = saveLocation + "/" + fileName;

        byte[] contents = Convert.FromBase64String(base64);

        FileStream fs = new FileStream(saveLocation, FileMode.Create, FileAccess.Write);
        fs.Write(contents, 0, contents.Length);
        fs.Close();



        divImagePath.Value = strSaveUrl;
        divRootId = root_id;        

    }
}
