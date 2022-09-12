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
	
	if (JpUtilsString.trimToEmpty(str1) == JpUtilsString.trimToEmpty(str2))
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

JpUtilsString.equalsAny = function()
{
	let length = arguments.length; 
	
	if (length < 2)
	{
		return false;		
	};
	
	let str = JpUtilsString.trimToEmpty(arguments[0]);
	
	for (let i=1; i<length; i++)
	{
		if (JpUtilsString.equals(str, arguments[i]))
		{
			return true;			
		};
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

JpUtilsString.isAllEmpty = function()
{
	for (let i=0, length=arguments.length; i<length; i++)
	{
		if (JpUtilsString.isNotEmpty(strs[i]))
		{
			return false;
		};		
	};
	
	return true;
};

JpUtilsString.isAnyEmpty = function()
{
	for (let i=0, length=arguments.length; i<length; i++)
	{
		if (JpUtilsString.isEmpty(arguments[i]))
		{
			return true;
		};		
	};
	
	return false;
};

JpUtilsString.defaultString = function(str1, str2)
{
	if (JpUtilsString.isEmpty(str1))
	{
		return str2;
	}
	
	return str1;
};

JpUtilsString.padLeft = function(str, chr, length)
{
	if (JpUtilsString.isEmpty(str))
	{
		return "";
	};
	
	let result = [];
	result.push(str);
	
	for (let i=String(str).length; i<length; i++)
	{
		result.unshift(chr);
	};
	
	return result.join("");
};

JpUtilsString.lengthAsByte = function(str)
{
	let totalLength = 0;
	
	if (JpUtilsString.isNotEmpty(str))
	{
    	for (let i=0, length=str.length; i<length; i++)
    	{
    		if (escape(str.charAt(i)).length > 4)
    		{
    			totalLength += 3;
    		}
    		else
    		{
    			totalLength++;
    		};
    	};		    		
	};
	
	return totalLength;
};

JpUtilsString.replaceAll = function(str, from, to)
{
	let regex = new RegExp("\\" + from, "gi");
	
	return JpUtilsString.trimToEmpty(str).replace(regex, to);
};