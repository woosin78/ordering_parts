let JpUiDimmer = {
		
		html: "<div id='jsUiDimmer' class='ui dimmer'></div>",
	
		type1: "<div class='ui text inverted loader'><div class='content'></div></div>",
		type2: "<div class='content'><h3 class='ui inverted header'></h3><button class='ui teal button ok'>OK</button></div>",
		type3: "<div class='content'><h3 class='ui inverted header'></h3><button class='ui teal button ok'>OK</button><button class='ui button cancel'>Cancel</button></button></div>",
		
		isMessageDimmerActive: false,
		obj: null,
		show: function(message) {
			let obj = JpUiDimmer.getDimmer(1, message);
			
			JpUiDimmer.showDimmer();
		},
		alert: function(message, callEvent) {
			JpUiDimmer.isMessageDimmerActive = true;
			
			let obj = JpUiDimmer.getDimmer(2, message);
			
			obj.find(".ui.button.ok").on("click", function() {
				JpUiDimmer.isMessageDimmerActive = false;
				obj.dimmer("hide");
				
				if (JpUtilsObject.isNotNull(callEvent))
				{
					callEvent();
				};
			});
			
			JpUiDimmer.showDimmer(function() {
				//obj.find(".ui.button.ok").focus();
			});
		},
		confirm: function(message, callEvent, cancelEvent) {
			JpUiDimmer.isMessageDimmerActive = true;
			
			let obj = JpUiDimmer.getDimmer(3, message);
			
			obj.find(".ui.button").on("click", function() {
				JpUiDimmer.isMessageDimmerActive = false;
				obj.dimmer("hide");
				
				if ($(this).hasClass("ok"))
				{
					if (JpUtilsObject.isNotNull(callEvent))
					{
						callEvent();
					};				
				}
				else
				{
					if (JpUtilsObject.isNotNull(cancelEvent))
					{
						cancelEvent();
					};				
				};
			});
			
			JpUiDimmer.showDimmer(function() {
				//obj.find(".ui.button.cancel").focus();
			});
		},
		hide: function() {
			if (!JpUiDimmer.isMessageDimmerActive)
			{
				$("#jsUiDimmer").dimmer("hide");
			};
		},	
		showDimmer: function(onShow) {
			$("#jsUiDimmer").dimmer({
				closable: false,
				onShow: onShow
			}).dimmer("show");
			
			$(document).find("input, select").blur();
		},
		getDimmer: function(type, message) {
			let obj = $("#jsUiDimmer");
			
			if (JpUtilsObject.isNull(obj))
			{
				$("body").append(JpUiDimmer.html);
				obj = $("#jsUiDimmer");
			};
			
			switch(type) {
			case 1:
				obj.html(JpUiDimmer.type1);
				obj.find(".content").html(JpUtilsString.defaultString(message, "페이지를 불러오는 중입니다."));
				break;
			case 2:
				obj.html(JpUiDimmer.type2);
				obj.find(".ui.header").html(JpUtilsString.trimToEmpty(message));
				break;
			case 3:
				obj.html(JpUiDimmer.type3);
				obj.find(".ui.header").html(JpUtilsString.trimToEmpty(message));
				break;
			}
			
			return obj;
		}
};