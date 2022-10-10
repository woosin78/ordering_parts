let JpUtilsObject = {};

JpUtilsObject.isNull = function(obj)
{
	if (JpUtilsObject.isJqueryObject(obj))
	{
		if (obj.length > 0)
		{
			return false;
		};
		
		return true;
	};
	
	if (typeof obj == "undefined" || obj == "null" || obj == null)
	{
		return true;
	};	
	
	return false;
};

JpUtilsObject.isAnyNull = function()
{
	for (let i=0; i<length; i++)
	{
		if (JpUtilsObject.isNull(arguments[i]))
		{
			return true;
		}
	};
};

JpUtilsObject.isNotNull = function(obj)
{
	return !JpUtilsObject.isNull(obj);
};

JpUtilsObject.defaultIfNull = function(obj1, obj2)
{
	if (JpUtilsObject.isNull(obj1))
	{
		return obj2;		
	};
	
	return obj1;
};

JpUtilsObject.isJqueryObject = function(obj)
{
	return obj instanceof jQuery;
};

JpUtilsObject.toJquery = function(obj)
{
	if (obj == null)
	{
		return null;
	}

	if (!JpUtilsObject.isJqueryObject(obj))
	{
		return $(obj);
	}
	return obj;
};