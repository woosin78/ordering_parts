let JpUtilsString = {};

JpUtilsString.equalsIgnoreCase = function(str1, str2)
{
	
	if (JpUtilsObject.isNull(str1) || JpUtilsObject.isNull(str2))
	{
		return false;
	};
	
	if ($.trim(str1).toUpperCase() == $.trim(str2).toUpperCase())
	{
		return true;
	};
	
	return false;
};

JpUtilsString.equals = function(str1, str2)
{
	
	if (JpUtilsObject.isNull(str1) || JpUtilsObject.isNull(str2))
	{
		return false;
	};
	
	if ($.trim(str1) == $.trim(str2))
	{
		return true;
	};
	
	return false;
};

JpUtilsString.notEquals = function(str1, str2)
{
	
	if (JpUtilsObject.isNull(str1) || JpUtilsObject.isNull(str2))
	{
		return false;
	};
	
	if ($.trim(str1) != $.trim(str2))
	{
		return true;
	};
	
	return false;
};

JpUtilsString.trimToEmpty = function(str)
{
	if (JpUtilsObject.isNull(str))
	{
		return "";
	}
	
	return $.trim(str);
};

JpUtilsString.isEmpty = function(str)
{
	if (JpUtilsString.trimToEmpty(str) == "")
	{
		return true;
	}
	
	return false;
};

JpUtilsString.isNotEmpty = function(str)
{
	return !JpUtilsString.isEmpty(str);
};

JpUtilsString.defaultString = function(str1, str2)
{
	if (JpUtilsString.isEmpty(str1))
	{
		return str2;
	}
	
	return str1;
};