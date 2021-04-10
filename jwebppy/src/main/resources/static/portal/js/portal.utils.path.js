let JpUtilsPath = {};

JpUtilsPath.url = function (url)
{
	let context = /*[[@{/}]]*/ '';
	
	return ((context != "") ? "/" : "") + url;
};