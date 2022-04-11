let JpUtilsAjax = {};

JpUtilsAjax.get = function(options)
{
	let additionalOption = {
		method: "GET"
	};
	
	$.extend(true, options, additionalOption);
	$.ajax(options);
};

JpUtilsAjax.post = function(options)
{
	let additionalOption = {
		method : "POST"		
	};
	
	try
	{
		if (JpUtilsObject.isNull(options.headers))
		{
			additionalOption.headers = {};
		}
		
		additionalOption.headers[$("meta[name=_csrf_header]").attr("content")] = $("meta[name=_csrf]").attr("content");
	}
	catch (e) {};
	
	options.url = JpUtilsPath.url(options.url);
	
	$.extend(true, options, additionalOption);
	$.ajax(options);
};

JpUtilsAjax.toJson = function(response)
{
	let data = [];

	try
	{
		if (typeof(response) == "string")
		{
			data = $.parseJSON(response);
		}
		else
		{
			data = response;
		};
			
	}
	catch (e)
	{
	    console.log(e.message);
	};
	
	return data;
};