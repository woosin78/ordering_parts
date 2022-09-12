let JpUiPopup = {};

JpUiPopup.open = function(settings)
{
	let url = settings.url;
	let name = JpUtilsString.defaultString(settings.name, "");
	let width = JpUtilsString.defaultString(settings.width, String(screen.availWidth * 0.7));
	let height = JpUtilsString.defaultString(settings.height, String(screen.availHeight * 0.7));
	let fgCenter = JpUtilsString.defaultString(settings.fgCenter, "Y");
	let fgScrollbars = JpUtilsString.defaultString(settings.fgScrollbars, "N");;
	let left = JpUtilsString.defaultString(settings.left, "100");
	let top = JpUtilsString.defaultString(settings.top, "100");
	
	if (fgCenter == "Y")
	{
		left = (screen.availWidth - width) * 0.5;
		top = (screen.availHeight - height) * 0.5;
	};
	
	let scrollbars = (fgScrollbars == "Y") ? "yes" : "no";
	
	let specs = "toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=" + scrollbars + ", copyhistory=yes, resizable=no";
	
    let popup = window.open("about:blank", name, "left=" + left + ", top=" + top + ", width=" + width + ", height=" + height + ", " + specs);    
    
    if (popup) popup.location.href = JpUtilsPath.url(url); //열림
    
	try
	{
		popup.focus();
	}
	catch (e)
	{
		let message = "Your popup blocker is preventing you from proceeding. To continue, please disable your popup blocker for this site.";
		
		if (JpUtilsString.equalsAny(navigator.language, "ko-KR", "ko"))
		{
			message = "팝업이 차단 되었습니다. 팝업 차단을 해제하고 이용해 주세요.";			
		};
		
		alert(message);
	};    
       
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
	
	return JpUiPopup.open(settings);
};