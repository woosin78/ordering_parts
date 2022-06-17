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

JpUtilsString.isAllEmpty = function(strs)
{
	if (strs == null || !Array.isArray(strs) || strs.length == 0)
	{
		console.log(1);
		return false;		
	};
	
	for (let i=0, length=strs.length; i<length; i++)
	{
		console.log(strs[i]);
		
		if (JpUtilsString.isNotEmpty(strs[i]))
		{
			return false;
		}		
	};
	
	return true;
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