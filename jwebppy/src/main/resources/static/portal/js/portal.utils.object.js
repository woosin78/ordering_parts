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

JpUtilsObject.isNotNull = function(obj)
{
	return !JpUtilsObject.isNull(obj);
};

JpUtilsObject.isJqueryObject = function(object)
{
	return object instanceof jQuery;
};

JpUtilsObject.toJquery = function(object)
{
	if (object == null)
	{
		return null;
	}

	if (!JpUtilsObject.isJqueryObject(object))
	{
		return $(object);
	}
	return object;
};