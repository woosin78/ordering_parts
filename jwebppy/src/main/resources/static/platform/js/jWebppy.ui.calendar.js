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
							return moment(date).format(JpUiGlobal.DEFAULT_DATE_FORMAT);
						},
						time: function (time, settings)
						{
							return moment(time).format(JpUiGlobal.DEFAULT_TIME_FORMAT);
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
				settings = JpUiCalendar.defaultCalendarSettings(true);
			};
			
			$(obj).closest(".ui.calendar").calendar(settings);
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