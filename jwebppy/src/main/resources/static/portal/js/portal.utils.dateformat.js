let DateFormat = {
		FULL_FORMAT: "YYYY-MM-DD HH:mm:ss",
		DATE_FORMAT: "YYYY-MM-DD",
		TIME_FORMAT: "HH:mm:ss"
};

DateFormat.format = function(date, format)
{
	return moment(date).format(format);
};

DateFormat.dateFormat = function(date)
{
	return this.format(date, this.DATE_FORMAT);
};

DateFormat.timeFormat = function(date)
{
	return this.format(date, this.TIME_FORMAT);
};

DateFormat.fullFormat = function(date)
{
	return this.format(date, this.FULL_FORMAT);
};