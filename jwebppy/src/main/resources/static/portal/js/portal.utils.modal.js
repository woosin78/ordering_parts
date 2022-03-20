let Modal = {};

Modal.show = function(title, width, height, content, isAllowToClose) {
	//기존에 오픈된 modal 삭제
	$(".modal-block, .modal-dimmer").remove();	
	
	let html = [];
	html.push("<div class='modal-dimmer dimmer'></div>");
	html.push("<div class='modal-block'>");
	html.push("	<div class='modal-block-top-line'></div>");
	html.push("	<div class='modal-block-body-background'>");
	html.push("	<div class='modal-block-body'>");
	html.push("		<div class='modal-block-header'>");
	html.push("			<div class='modal-block-header-title'>" + title + "</div>");
	
	if (JpUtilsObject.isNull(isAllowToClose) || isAllowToClose)
	{
		html.push("			<div class='modal-block-header-button'><img src='/portal/img/popup/close.png' class='modal-block-close-button'></div>");	
	};
	
	html.push("		</div>");
	html.push("		<div class='modal-content'>" + content + "</div>");
	html.push("	</div>");
	html.push("	</div>");
	html.push("</div>");
	
	$("body").append(html.join(""));
	
	if ($.trim(width) == "")
	{
		width = 720;
	};
	
	if ($.trim(height) == "")
	{
		height = 540;
	};

	$(".modal-block").css({
		"width": width,
		"height": height
	});
	
	Modal.resize(width, height);
	
	$(".modal-dimmer, .modal-block").show();
	
	$(".modal-block-header-button").on("click", function() {
		$(".modal-dimmer, .modal-block").hide();
	});
	
	Modal.dimmerResize();
	
	$(window).resize(function() {
		Modal.resize(width, height);
		Modal.dimmerResize();
	});

	$(window).scroll(function() {
		Modal.dimmerResize();
	});
};

Modal.dimmerResize = function() {
	let width = $(window).width() + ($(document).width() - window.innerWidth) + $.position.scrollbarWidth();
	
	if (width > screen.availWidth)
	{
		width = screen.availWidth;
	};
	
	/*
	$(".modal-dimmer").css({
		"width": width,
		"height": $(document).height(),
		"overflow": "hidden"
	});
	*/
};

Modal.resize = function(width, height) {	
	let left = ($(window).innerWidth() - width) * 0.5;
	let top = ($(window).innerHeight() - height) * 0.5 + $(window).scrollTop();
	
	$(".modal-block").css({
		"left": left,
		"top": top
	});		
};