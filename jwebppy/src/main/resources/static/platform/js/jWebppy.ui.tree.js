let JpUiTree = function(object)
{
	this.context = JpUtilsObject.toJquery(object);
	this.items = [];
	this.content = [];
	this.icons = {};
	this.menu = "";
	this.viewDetail;
	this.addItem;
	this.deleteItem;
	this.onClickMenu;
	this.url;
	this.onLoad;

	let _this = this;
	
	this.setContext = function(object)
	{
		this.context = JpUtilsObject.toJquery(object);
	};
	
	this.setUrl = function(url)
	{
		this.url = url;
	};		
	
	this.render = function()
	{
		if (this.url != null)
		{
			let option = {
				url: _this.url,
				dataType: "json",
				success: function(data, textStatus, jqXHR)
				{
					_this.items = JpUtilsAjax.toJson(data).RESULT;
					_this.buildTree();
					
					if (JpUtilsObject.isNotNull(_this.onLoad))
					{
						_this.onLoad();
					};
				}
			};
			
			JpUtilsAjax.get(option);
		}
		else if (this.items != null)
		{
			_this.buildTree();
		};
	};

	this.buildTree = function()
	{
		this.content.push("<div class='ui list'>");
		this.makeItem(this.items);
		this.content.push("</div>");
		
		this.context.html(this.content.join(" "));
		
		this.context.find(".header span,.caret").css("cursor", "pointer");

		this.context.on("contextmenu", function() { return false; });
		this.context.find(".caret").on("click", function(event) {
			_this.collapse($(this));
		});

		this.attachEventClickItem();
	};

	this.collapse = function(obj)
	{
		let target = obj.closest("div").children(".content").children(".list");

		if (target.is(":visible"))
		{
			obj.removeClass("down").addClass("up");
			obj.next().removeClass("open");
		}
		else
		{
			obj.removeClass("up").addClass("down");
			obj.next().addClass("open");
		};

		target.slideToggle("fast");
	};

	this.collapseAll = function(isCollpase)
	{
		if (isCollpase)
		{
			this.context.find(".list:eq(1)").slideUp("fast", function() {
				_this.context.find(".caret").removeClass("down").addClass("up").next().removeClass("open");
				_this.context.find(".list:gt(1)").slideUp(0);
			});
		}
		else
		{
			this.context.find(".list:eq(1)").slideUp(0, function() {
				$(_this.context.find(".list:gt(1)").get().reverse()).each(function(index) {
					$(this).slideDown(0);
				});

				_this.context.find(".caret").removeClass("up").addClass("down").next().addClass("open");
				_this.context.find(".list:eq(1)").slideDown("fast");
			});
		};
	};

	this.makeItem = function(items)
	{
		for (let i=0, length=items.length; i<length; i++)
		{
			let subItemsLength = (items[i].SUB_ITEMS != null) ? items[i].SUB_ITEMS.length : 0;
			let type = items[i].TYPE;

			this.content.push("<div class='item'>");

			if (subItemsLength > 0)
			{
				this.content.push("<i class='caret down icon'></i>");
			};

			if (items[i].TYPE == "FOLDER" && subItemsLength > 0)
			{
				type = "FOLDER_OPEN";
			}

			if (this.icons[items[i].TYPE] != null)
			{
				this.content.push("<i class='" + this.icons[type] + " icon'></i>");
			};

			this.content.push("<div class='content'>");
			this.content.push("<div class='header' data-key='" + items[i].KEY + "' parent-data-key='" + items[i].P_KEY + "' data-title='" + items[i].NAME + "'><span class='name'>" + items[i].NAME + "</span>");

			//Page can not have a leaf.
			if (items[i].TYPE != "PAGE")
			{
				this.content.push("<i class='plus square outline icon' style='cursor:pointer;'></i>");
			};
			
			//Can not delete root
			if (JpUtilsObject.isNotNull(items[i].P_KEY))
			{
				this.content.push("<i class='minus square outline icon' style='cursor:pointer;'></i>");	
			};
						
			this.content.push("</div>");

			if (subItemsLength > 0)
			{
				this.content.push("<div class='list'>");
				this.makeItem(items[i].SUB_ITEMS);
				this.content.push("		</div>");
			};

			this.content.push("</div>");
			this.content.push("</div>");
		};
	};
	
	this.getDataKey = function(item)
	{
		return $(item).closest("div").attr("data-key");
	};
	
	this.getParentDataKey = function(item)
	{
		return $(item).closest("div").attr("parent-data-key");
	};
	
	this.getRoot = function()
	{
		return this.items[0];
	};

	this.attachEventClickItem = function()
	{
		this.context.find(".header > span.name").on("mousedown", function(event) {
			try
			{
				if (event.which == 1)
				{
					_this.viewDetail(_this.getDataKey(this), _this.getParentDataKey(this));	
				}
				else if (event.which == 3)
				{
					_this.onPopupMenu();
				};
			}
			catch (e)
			{
				console.log("There is no click event to show details.");
			};			
		});

		this.context.find(".header > i.plus").on("click", function(event) {
			try
			{
				_this.addItem(_this.getDataKey(this), _this.getParentDataKey(this));
			}
			catch (e) {};
		});
		
		this.context.find(".header > i.minus").on("click", function(event) {
			try
			{
				_this.deleteItem(_this.getDataKey(this), _this.getParentDataKey(this));
			}
			catch (e) {};
		});		
	};
	
	this.refresh = function()
	{
		this.content = null;
		this.content = [];

		this.render();
	};
};