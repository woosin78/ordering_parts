XfreeEditor = function(options)
{
	this.basePath = JpUtilsPath.url("/editor_html5");
	this.target = $("#" + options.target);
	this.htmlContent = JpUtilsString.trimToEmpty(options.htmlContent);
	this.options = options;
	this.xfeOptions;	
	this.xfe;
	
	_this = this;	
	
	this.setXfreeOptions = function(xfeOptions)
	{
		this.xfeOptions = xfeOptions;		
	};	
	
	this.render = function()
	{
		let options = {
			basePath: _this.basePath,
			onLoad: function() {
				_this.xfe.setBodyValue(_this.htmlContent);
			},
			width: "100%",
			height: "100%",			
			language: ((JpUtilsString.equals(GLOBAL_CONST.LANG, "KO")) ? "korean" : "english"),
			initFontFamily: (JpUtilsString.equals(GLOBAL_CONST.LANG, "KO")) ? "맑은 고딕" : "Arial",			
		};
		
		if (JpUtilsObject.isNotNull(this.xfeOptions))
		{
			$.extend(options, this.xfeOptions);
		};
		
		this.xfe = new XFE(options);
		
		this.xfe.render(this.target.prop("id"));
		
		return this.xfe;
	};	
};

XfreeViewr = function(options)
{
	this.basePath = JpUtilsPath.url("/editor_html5");
	this.target = $("#" + options.target);
	this.htmlContent = JpUtilsString.trimToEmpty(options.htmlContent);
	this.resizeMode = JpUtilsString.defaultString(options.resizeMode, "F");//F: 내용 전체가 보이도록 세로 사이즈 조정, S: body 스크롤바가 생기지 않도록 내용 영역의 세로 사이즈 조정
	this.options = options;
	this.xfeOptions;	
	this.xfe;
	
	_this = this;
	
	if (JpUtilsString.equals(this.resizeMode, "F"))
	{
		$("body").css("overflow-y", "scroll");	
	};
	
	this.setXfreeOptions = function(xfeOptions)
	{
		this.xfeOptions = xfeOptions;		
	};
	
	this.render = function()
	{
		let options = {
			basePath: _this.basePath,
			viewMode: true,
		    viewModeOptions: {
		    	borderWidth: "0px"
		    },
			onLoad: function() {
				_this.xfe.setBodyValue(_this.htmlContent);
				
				if (JpUtilsString.equals(_this.resizeMode, "F"))
				{
					_this.resizeToFull();					
				};
				
				if (JpUtilsString.equals(_this.resizeMode, "S"))
				{
					_this.initResizer(_this.options.container);	
				};
			}
		};
		
		if (JpUtilsObject.isNotNull(this.xfeOptions))
		{
			$.extend(options, this.xfeOptions);
		};
		
		this.xfe = new XFE(options);
		
		this.xfe.render(this.target.prop("id"));
		
		return this.xfe;
	};
	
	this.initResizer = function($container)
	{
		$(window).on("load", function() {
			_this.resize($container);
		});

		$(window).on("resize", $.debounce(100, function(e){
			_this.resize($container);
		}));
	};
	
	this.resize = function($area)
	{
		let height = 0;
		
		$.each($("body").children(":not(script)").not($area), function(index, element) {
			if (!$(element).is(":visible"))
			{
				return true;
			};
			
			height += $(element).outerHeight(true);
		});
		
		let bodyHeight = $("body").height();
		let h1 = $area.outerHeight(true) - this.target.outerHeight(true);//게시글 영역 외 height
		
		this.target.height(bodyHeight - height - h1);
		this.xfe.setHeight(this.target.height() + "px");
	};	
	
	this.resizeToFull = function()
	{
		let xfreeFrame = _this.target.find(".xfeViewFrame");
		
		let intervalId = setInterval(function() {
			if (_this.hasScrollbar(xfreeFrame))
			{
				_this.target.height(xfreeFrame.contents().height());
			}
			else
			{
				clearInterval(intervalId);
			};
		}, 500);	
	};	
	
	this.hasScrollbar = function(element)
	{
		/*
		if (JpUtilsString.equalsIgnoreCase($(element).prop("tagName"), "iframe"))
		{
			return ($(element).prop("scrollHeight") < $(element).contents().height()) ? true: false;
		}
		else
		{
			return ($(document).height() > $(window).height()) ? true: false;		
		};
		
		return false;
		*/
		return ($(element).prop("scrollHeight") < $(element).contents().height()) ? true: false;
	};	
};