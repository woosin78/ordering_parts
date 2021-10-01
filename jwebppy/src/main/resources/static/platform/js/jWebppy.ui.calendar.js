let JpUiCalendar = {
		defaultCalendarSettings: function(isOnlyDate)
		{
			let settings = {			
					selectAdjacentDays: true,
					ampm: false,
					today: true,
					formatter: {
						date: function (date, settings)
						{
							return moment(date).format(JpUtilsDateFormat.DATE_FORMAT);
						},
						time: function (time, settings)
						{
							return moment(time).format(JpUtilsDateFormat.TIME_FORMAT);
						}
					}
			};
			
			if (isOnlyDate)
			{
				settings.type = "date";
			};
			
			return settings;	
		},
		calendar: function(obj, settings)
		{
			if (settings == null)
			{
				return JpUiCalendar.dateCalendar(obj);
			};
			
			$(obj).closest(".ui.calendar").calendar(settings);
		},
		dateCalendar: function()
		{
			for (let i=0, length=arguments.length; i<length; i++)
			{
				$(arguments[i]).closest(".ui.calendar").calendar(JpUiCalendar.defaultCalendarSettings(true));	
			};
		},
		datetimeCalendar: function()
		{
			for (let i=0, length=arguments.length; i<length; i++)
			{
				$(arguments[i]).closest(".ui.calendar").calendar(JpUiCalendar.defaultCalendarSettings(false));	
			};			
		},		
		rangeCalendar: function(from, to, settings)
		{
			let settingsFrom = settings;
			let settingsTo = settings;
			
			if (settings == null)
			{
				settingsFrom = JpUiCalendar.defaultCalendarSettings(true);
				settingsTo = JpUiCalendar.defaultCalendarSettings(true);
			};
			
			settingsFrom.endCalendar = $(to).closest(".ui.calendar");
			settingsTo.startCalendar = $(from).closest(".ui.calendar");
			
			$(from).closest(".ui.calendar").calendar(settingsFrom);
			$(to).closest(".ui.calendar").calendar(settingsTo);
		}
};