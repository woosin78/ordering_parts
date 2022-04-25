let JpUiPopup = {};

JpUiPopup.open = function(settings)
{
	let url = settings.url;
	let width = JpUtilsString.defaultString(settings.width, String(screen.availWidth * 0.7));
	let height = JpUtilsString.defaultString(settings.height, String(screen.availHeight * 0.7));
	let fgCenter = JpUtilsString.defaultString(settings.fgCenter, "Y");
	let fgScrollbars = JpUtilsString.defaultString(settings.fgScrollbars, "N");;
	let left = 100;
	let top = 100;
	
	if (fgCenter == "Y")
	{
		left = (screen.availWidth - width) * 0.5;
		top = (screen.availHeight - height) * 0.5;
	};
	
	let scrollbars = (fgScrollbars == "Y") ? "yes" : "no";
	
	let specs = "toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=" + scrollbars + ", copyhistory=yes, resizable=no";
	
    let popup = window.open("about:blank", "", "left=" + left + ", top=" + top + ", width=" + width + ", height=" + height + ", " + specs);    
    
    if (popup) popup.location.href = JpUtilsPath.url(url); //열림   
    popup.focus();
	
	return popup;
};

let Popup = {};

Popup.open = function(url, width, height, fgCenter, fgScrollbars)
{
	let settings = {
			url: url,
			width: width,
			height: height,
			fgCenter: fgCenter,
			fgScrollbars: fgScrollbars
	};
	
	JpUiPopup.open(settings);
};