let JpUtilsFile = {};

JpUtilsFile.displaySize = function(size)
{
	let KB = 1024;
	let MB = Math.pow(1024, 2);
	let GB = Math.pow(1024, 3);
	
	let result = [];

	if (size > GB)
	{
		result.push((size / GB).toFixed(2));
		result.push("GB");
	}
	else if (size > MB)
	{
		result.push((size / MB).toFixed(2));
		result.push("MB");
	}
	else if (size > KB)
	{
		result.push((size / KB).toFixed(2));
		result.push("KB");
	}
	else
	{
		result.push(size);
		result.push("B");
	};
	
	return result.join("");
};