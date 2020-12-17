let JpUtilsNumber = {};

JpUtilsNumber.defaultNumber = function(num1, num2)
{
	if (JpUtilsObject.isNull(num1))
	{
		if (JpUtilsObject.isNull(num2))
		{
			return 0;
		}

		return num2;
	}
	
	return num1;
};

JpUtilsNumber.addComma = function(num)
{
	if (JpUtilsObject.isNotNull(num))
	{
		let str = new String(num);
		return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");		
	}
	
	return num;
};

JpUtilsNumber.undoComma = function(num)
{
	if (JpUtilsObject.isNotNull(num))
	{
		let str = new String(num);
		return str.replace(/[^\d]+/g, "");		
	};
	
	return num;
};