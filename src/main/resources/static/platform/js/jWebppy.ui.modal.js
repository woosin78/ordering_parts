/*
 * https://semantic-ui.com/modules/modal.html#/settings
 */
JpUiModal = function(target)
{
	this.target = JpUtilsObject.toJquery(target);
	this.url;
	this.onHidden;
	this.onShow;
	this.contentArea;
	this.settings = {};	
	
	let _this = this;
	
	this.show = function()
	{
		this.target.modal("hide others");
		
		this.target.modal({
		    autofocus: false,
		    onHidden: function(element)
		    {
		    	if (_this.onHidden != null && typeof(_this.onHidden) == "function")
	    		{
		    		_this.onHidden(element);
	    		};
		    },
		    onShow: function()
		    {
		    	_this.render();
		    }
		})
		.modal("setting", "closable", false)
		.modal("show");
	};
	
	this.render = function()
	{
    	if (JpUtilsObject.isNotNull(this.settings.ajax))
    	{
    		let data = null;
    		
			if (JpUtilsObject.isNotNull(this.settings.ajax.form))
			{
				data = this.settings.ajax.form.serialize();
			};
    		
    		let ajaxSettings = {
    				data: data,
			        success: function(data, textStatus, jqXHR)
			        {			        	
			        	_this.getContentArea().html(new JpUiRender().render(data));

			        	_this.target.find(".ui.dropdown").dropdown({
							minCharacters: 3,
							placeholder: "Search",
							fullTextSearch: true
			        	});

				    	if (_this.onShow != null && typeof(_this.onShow) == "function")
			    		{
				    		_this.onShow();
			    		}
			        }
    		};
    		
    		if (JpUtilsObject.isNull(this.settings.ajax.url))
    		{
    			this.settings.ajax.url = JpUtilsPath.url(this.settings.ajax.url);
    		};	    		
    		
    		$.extend(true, ajaxSettings, this.settings.ajax);
    		
    		JpUtilsAjax.get(ajaxSettings);
    	}
    	else
    	{
	    	if (this.onShow != null && typeof(this.onShow) == "function")
    		{
	    		this.onShow();
    		};		    		
    	};
	};
	
	this.getContentArea = function()
	{
		if (JpUtilsObject.isNotNull(this.contentArea))
		{
			return this.contentArea;
		}
		else
		{
	    	let contentArea = this.target.find(".content form");
	    	
	    	if (JpUtilsObject.isNotNull(contentArea))
	    	{
	    		return contentArea;
	    	}
	    	
	    	return this.target.find(".content");
		};
	};
	
	this.reload = function()
	{
		this.render();
	};
	
	this.hide = function()
	{
		if (this.target != null)
		{
			this.getContentArea().empty();
			
			this.target.modal("hide");
			this.target.unwrap();//다른 dimmer 동시 동작할 경우 modal dimmer 가 사라지지 않는 버그 있음
		};
	};
};
