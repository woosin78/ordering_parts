let Popup = {};

Popup.open = function(url, width, height, fgCenter, fgScrollbars)
{
	let left = 100;
	let top = 100;
	
	if (width == null)
	{
		width = 1000;
	};
	
	if (height == null)
	{
		height = 700;
	};
	
	if (fgCenter == null)
	{
		fgCenter = "Y";
	};
	
	if (fgCenter == "Y")
	{
		left = (screen.availWidth - width) * 0.5;
		top = (screen.availHeight - height) * 0.5;
	};
	
	if (fgScrollbars == null)
	{
		fgScrollbars = "no";
	}
	else
	{
		fgScrollbars = "yes";
	};
	
	let specs = "toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=" + fgScrollbars + ", copyhistory=yes, resizable=no";
	
    let popup = window.open("about:blank", "", "left=" + left + ", top=" + top + ", width=" + width + ", height=" + height + ", " + specs);    
    
    if (popup) popup.location.href = url; //열림   
    popup.focus();
	
	return popup;
};