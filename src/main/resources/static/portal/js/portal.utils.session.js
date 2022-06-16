let CmSessionUtils = {};

CmSessionUtils.keepSession = function(interval)
{
	if (JpUtilsObject.isNull(interval))
	{
		//Default session timeout is 10 minutes.
		interval = 600000;
	};
	
	setInterval(function() {
		$.ajax({
			url: "/portal/common/session/refresh"
		});
	}, interval);		
};