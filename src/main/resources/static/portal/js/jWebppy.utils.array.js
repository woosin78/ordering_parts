let JpUtilsArray = {};

JpUtilsArray.isEmpty = function(obj)
{
	if (JpUtilsObject.isNull(obj))
	{
		return true;		
	};
	
	if (!Array.isArray(obj))
	{
		throw "This is not an array type.";
	};
	
	return obj.length == 0;
};

JpUtilsArray.isNotEmpty = function(obj)
{
	return !JpUtilsArray.isEmpty(obj);
};


JpUtilsArray.emptyIfNull = function(obj)
{
	if (JpUtilsArray.isEmpty(obj))
	{
		return [];
	};
	
	return obj;
};

JpUtilsArray.defaultIfNull = function(obj1, obj2)
{
	if (JpUtilsArray.isEmpty(obj1))
	{
		return obj2;
	};
	
	return obj1;
};