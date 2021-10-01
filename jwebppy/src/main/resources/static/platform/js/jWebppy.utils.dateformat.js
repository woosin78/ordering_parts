let JpUtilsDateFormat = {
		DATE_TIME_FORMAT: "YYYY-MM-DD HH:mm:ss",//Default date-time format
		DATE_FORMAT: "YYYY-MM-DD",//Default date format
		TIME_FORMAT: "HH:mm:ss",//Default time format
		SUBMIT_FORMAT: "YYYYMMDDHHmmss",//Default submit format
		format: function(date, format)
		{
			return moment(date).format(format);
		},
		dateFormat: function(date)
		{
			return this.format(date, this.DATE_FORMAT);
		},
		timeFormat: function(date)
		{
			return this.format(date, this.TIME_FORMAT);
		},
		fullFormat: function(date)
		{
			return this.format(date, this.DATE_TIME_FORMAT);
		}
};