let JpUiDimmer = {
	html: "<div id='jsUiDimmer' class='ui dimmer'><div class='ui text inverted loader'><div class='content'>Loading</div></div></div>",	
	showDimmer: function(message) {
		if (!JpUiDimmer.isThereActiveDimmer())
		{
			let dimmer = $("#jsUiDimmer");

			if (dimmer.length == 0)
			{
				$("body").append(JpUiDimmer.html);
				dimmer = $("#jsUiDimmer");
			};
			
		    if (!dimmer.dimmer("is active"))
		    {
		        if (message == null)
		        {
		            message = "페이지를 불러오는 중입니다.";
		        };

		        dimmer.find(".content").text(message);
		        dimmer.dimmer({ closable: false }).dimmer("show");
		    };			
		};
	},
	hideDimmer: function() {
		$("#jsUiDimmer").dimmer("hide");
	},
	isThereActiveDimmer: function()
	{
		return ($(".ui.dimmer.active").length == 0) ? false : true;
	}
};