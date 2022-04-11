var ProgressBar = {};

ProgressBar.show = function(message)
{
	$(".progress-dimmer, .progress-block").remove();
	
	if ($.trim(message) == "")
	{
		message = "Progressing";
	};
	
	var html = [];
	html.push("<div class='progress-dimmer dimmer'></div>");
	html.push("<div class='progress-block' style='position: absolute; width: 300px; height:100px; box-shadow: 0 25px 25px 0 rgba(0, 0, 0, 0.16); background-color: #ffffff; text-align: center; color: #003081; font-weight: bold; padding-top:15px; z-index:999'>");
	html.push(message);
	html.push("<img src='/portal/img/common/progress.gif'>");
	html.push("</div>");	
	
	var width = 300;
	var height = 60;
	
	$("body").append(html.join(""));
	
	let left = ($(window).innerWidth() - width) * 0.5;
	let top = ($(window).innerHeight() - height) * 0.5;
	
	$(".progress-block").css({
		"width": width,
		"height": height,
		"left": left,
		"top": top
	});
	
	$(".progress-dimmer").css("height", $(window).innerHeight());
	
	$(".progress-dimmer, .progress-block").show();
};

ProgressBar.hide = function()
{
	$(".progress-dimmer, .progress-block").hide();
};