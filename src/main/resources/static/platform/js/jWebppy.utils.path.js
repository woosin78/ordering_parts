let JpUtilsPath = {};

JpUtilsPath.url = function(url)
{
	let context = /*[[@{/}]]*/ '';
	
	return ((context != "") ? "/" : "") + url;
};

JpUtilsPath.jsonToQueryString = function(params)
{
	let queryString = [];

	if (JpUtilsObject.isNotNull(params))
	{
		for (let param in params)
		{
			if (params.hasOwnProperty(param))
			{
				queryString.push(encodeURIComponent(param) + "=" + encodeURIComponent(params[param]));
			};
		};		
	};
	
	return queryString.join("&");	
};