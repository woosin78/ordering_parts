let JpUiTab = function(context)
{
	this.context = JpUtilsObject.toJquery(context);
	this.target = JpUtilsObject.toJquery(this.context).find(".item");
	this.tabPath;
	this.settings = {
			tab: {},
			ajax: {},
	};
	this.tabSettings = {};
	this.tables = {};

	let _this = this;

	this.render = function()
	{
		let ajaxSettings = {
				loadingDuration : 0,
				beforeSend: function(settings)
				{
					try
					{
						if (JpUtilsObject.isNotNull(_this.settings.ajax.form))
						{
							settings.data = _this.settings.ajax.form.serialize();
						}						
					}
					catch (e)
					{
						console.log(e.message);
					}					
					
					return settings;
				},
				onResponse: function(response)
				{
					return new JpUiRender().render(response);
				}
		};
		
		$.extend(true, ajaxSettings, this.settings.ajax);
		
		this.tabSettings = {
				context: "parent",
				cache: false,
				ignoreFirstLoad: false,
				auto: true,
				onRequest: function(tabPath)
				{
					if (JpUtilsObject.isNotNull(_this.settings.tab.onRequest))
					{
						_this.settings.tab.onRequest(tabPath);
					};
					
					_this.tabPath = tabPath;
				},
				onLoad: function(tabPath, parameterArray, historyEvent)
				{
					let thisTab = $(".ui.tab[data-tab=" + tabPath + "]");
					
					thisTab.find(".ui.dropdown").dropdown({
						minCharacters: 2,
						//placeholder: "Search",
						fullTextSearch: true
					});
					thisTab.find(".ui.checkbox").checkbox();
					
					$.each(thisTab.find("table"), function(i, item) {
						let id = $(this).attr("id");
						
						if (JpUtilsString.isEmpty(id))
						{
							id = "table" + i;
							$(this).attr("id", id);
						};
						
						let table = new JpUiTable($(this));
						table.initAfterRendering();
						
						_this.tables[id] = table;
					});
					
					if (JpUtilsObject.isNotNull(_this.settings.tab.onLoaded))
					{
						_this.settings.tab.onLoaded(tabPath, parameterArray, historyEvent);
					};
					
					thisTab.find("table").each(function(index) {
						if ($(this).get(0).scrollWidth > $(this).width())
						{
							$(this).css("display", "block");
						}
						else
						{
							$(this).css("display", "");
						};
					});
				}
		};
		
		if (JpUtilsObject.isNull(this.settings.tab.url))
		{
			this.settings.tab.url = JpUtilsPath.url(this.settings.tab.url);
		};		
		
		this.tabSettings.apiSettings = ajaxSettings;

		$.extend(true, this.tabSettings, this.settings.tab);
		this.target.tab(this.tabSettings);
		
		if (this.settings.tab.isLoadFirstTab)
		{
			this.changeTab(this.target.first().attr("data-tab"));
		};
    };
    
	this.changeTab = function(tabPath)
	{
		if (JpUtilsObject.isNull(tabPath))
		{
			tabPath = this.getTabPath();
		};
		
		this.target.tab("change tab", tabPath);
	};
	
	this.changePath = function(path)
	{
		this.settings.tab.path = JpUtilsPath.url(path);
		this.render();
	};    
    
    this.getTabPath = function()
    {
    	try
    	{
    		return this.target.tab("get path");	
    	}
    	catch (e)
    	{
    		if (this.tabPath == null)
    		{
    			return this.target.eq(0).attr("data-tab");
    		}
    		else
    		{
    			return this.tabPath;	
    		};
    	}
    };
    
    this.getPath = function()
    {
    	return this.settings.tab.path;
    }
};
