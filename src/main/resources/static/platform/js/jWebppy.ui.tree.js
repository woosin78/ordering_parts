let JpUiTree = function(object)
{
	this.context = JpUtilsObject.toJquery(object);
	this.items = [];
	this.content = [];
	this.icons = {};
	this.menu = "";
	this.viewDetail;
	this.createItem;
	this.deleteItem;
	this.copyItem;
	this.cutItem;
	this.pasteItem;
	this.onClickMenu;
	this.url;
	this.onLoad;
	this.selectedItem;
	this.commands = new Array();
	this.status = [];

	let _this = this;
	
	this.setUrl = function(url)
	{
		this.url = url;
	};		
	
	this.render = function()
	{
		if (this.url != null)
		{
			let option = {
				url: JpUtilsPath.url(_this.url),
				dataType: "json",
				beforeSend: function(jqXHR, settings)
				{
					_this.refreshStatus();
				},
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
	
	this.refreshStatus = function()
	{
		this.status = null;
		this.status = [];
		
		this.context.find(".caret").each(function(index) {
			let key = String($(this).attr("data-key"));
			let value = $(this).hasClass("down") ? "down" : "up";
			
			_this.status[key] = value;
		});		
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
		
		$("#MENU_command").remove();		
		$("body").append(this.menu());
		
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

	this.collapseAll = function(isCollpase, callBack)
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
			let direction = JpUtilsString.defaultString(this.status[items[i].KEY], "up");

			this.content.push("<div class='item'>");
			
			if (subItemsLength > 0)
			{
				this.content.push("<i class='caret " + direction + " icon' data-key='" + items[i].KEY + "'></i>");
			};

			if (items[i].TYPE == "FOLDER" && subItemsLength > 0)
			{
				type = "FOLDER";
			}

			if (this.icons[items[i].TYPE] != null)
			{
				this.content.push("<i class='" + this.icons[type] + " icon'></i>");
			};

			this.content.push("<div class='content'>");
			this.content.push("<div class='header' data-key='" + items[i].KEY + "' parent-data-key='" + items[i].P_KEY + "' data-title='" + items[i].NAME + "' data-type='" + items[i].TYPE + "'><span class='name'>" + items[i].NAME + "</span>");
			this.content.push("</div>");

			if (subItemsLength > 0)
			{
				let display = (direction == "up") ? "none" : "block";
				
				this.content.push("<div class='list' style='display:" + display + ";'>");
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
	
	this.getDataType = function(item)
	{
		return $(item).closest("div").attr("data-type");
	};	
	
	this.getRoot = function()
	{
		return this.items[0];
	};
	
	this.getSelectedItem = function()
	{
		return this.selectedItem; 
	};
	
	this.showPopupMenu = function()
	{
		$("#MENU_command .item").hide();
		
		if (this.isRoot())
		{
			$("#MC_createItem").show();
		}
		else
		{
			let type = this.getDataType($(_this.selectedItem));
			
			if (type == "PAGE")
			{
				$("#MC_deleteItem, #MC_copyItem, #MC_cutItem").show();
			}
			else
			{
				$("#MC_createItem, #MC_deleteItem, #MC_copyItem, #MC_cutItem, #MC_newWindow").show();				
			};
			
			let command = this.getCommand();
			
			if (JpUtilsObject.isNotNull(command))
			{
				if (command.COMMAND == "COPY" || command.COMMAND == "CUT")
				{
					$("#MC_pasteItem").show();	
				};				
			};
		};
		
		$("#MENU_command").hide(0, "linear", function() {
			$("#MENU_command").fadeIn(150);
		});
	};
	
	this.hidePopupMenu = function()
	{
		$("#MENU_command").hide();
	};
	
	this.attachEventClickItem = function()
	{
		this.context.find(".header > span.name").dblclick(function(event) {
			
		});
		
		
		this.context.find(".header > span.name").on("mousedown", function(event) {
			try
			{
				_this.selectedItem = $(this).closest("div");
				
				console.log(event.which + "," + event.clickCount);
				
				if (event.which == 1)
				{
					_this.viewDetail(_this.getDataKey(this), _this.getParentDataKey(this), this);
				}
				else if (event.which == 3)
				{
					if (JpUtilsObject.isNull(_this.onPopupMenu))
					{
						_this.onPopupMenu = function(key, pkey, e, target)
						{
							//let left = $(target).offset().left + $(target).width() + 5;
							let left = $(target).offset().left;
							let top = $(target).offset().top + $(target).height() - 10;
							
							$("#MENU_command").css("left", left).css("top", top);
							
							_this.showPopupMenu();
						}
					};
					
					_this.onPopupMenu(_this.getDataKey(this), _this.getParentDataKey(this), event, this);
				};
			}
			catch (e)
			{
				console.log(e);
			};			
		});
		
		this.context.find(".header > span.name").on("mouseover", function() {
			$(this).css("color", "#00b5ad");
		});
		
		this.context.find(".header > span.name").on("mouseout", function() {
			$(this).css("color", "#000");
		});		
		
		$("#MC_createItem").on("click", function(event) {
			try
			{
				_this.doCommand("CREATE", this, event);
			}
			catch (e) {};
		});
		
		$("#MC_deleteItem").on("click", function(event) {
			try
			{
				_this.doCommand("DELETE", this, event);
			}
			catch (e) {};
		});
		
		$("#MC_copyItem").on("click", function(event) {
			try
			{
				_this.doCommand("COPY", this, event);
			}
			catch (e) {};
		});
		
		$("#MC_cutItem").on("click", function(event) {
			try
			{
				_this.doCommand("CUT", this, event);
			}
			catch (e) {};
		});
		
		$("#MC_pasteItem").on("click", function(event) {
			try
			{
				_this.doCommand("PASTE", this, event);
			}
			catch (e) {};
		});
		
		$("#MC_newWindow").on("click", function(event) {
			try
			{
				_this.doCommand("NEW_WINDOW", this, event);
			}
			catch (e) {};
		});
		
		$("body").on("scroll", function() {
			_this.hidePopupMenu();
		});
		
		$(document).on("click", function(e) {
			_this.hidePopupMenu();
		});
	};
	
	this.addCommandHistory = function(command, key, pKey)
	{
		let object = new Object();
		object.KEY = key;
		object.P_KEY = pKey;
		object.COMMAND = command;
		
		this.commands.push(object);		
	};
	
	this.getCommand = function()
	{
		try
		{
			return this.commands[this.commands.length-1];	
		}
		catch (e)
		{
			console.log(e);
			return null;
		};
	};
	
	this.getPreviousCommand = function()
	{
		try
		{
			return this.commands[this.commands.length-2];	
		}
		catch (e)
		{
			console.log(e);
			return null;
		};
	};	
	
	this.doCommand = function(command, target, event)
	{
		let key = this.getDataKey(this.getSelectedItem());
		let pKey = this.getParentDataKey(this.getSelectedItem());
		
		this.addCommandHistory(command, key, pKey);
		
		if (command == "CREATE")
		{
			this.createItem(key, pKey, this, event);
		}
		else if (command == "DELETE")
		{
			this.deleteItem(key, pKey, this, event);
		}
		else if (command == "COPY")
		{
			this.copyItem(key, pKey, this, event);
		}
		else if (command == "CUT")
		{
			this.cutItem(key, pKey, this, event);
		}		
		else if (command == "PASTE")
		{
			this.pasteItem(key, pKey, this, event);
		}
		else if (command == "NEW_WINDOW")
		{
			this.newWindow(key, pKey, this, event);
		}		
		
		this.hidePopupMenu();
	};
	
	this.isRoot = function()
	{
		if (JpUtilsObject.isNull(this.getParentDataKey(this.getSelectedItem())))
		{
			return true;
		};
		
		return false;
	};
	
	this.menu = function()
	{
		let menu = "";
		menu += "<div id='MENU_command' class='ui inverted vertical menu' style='display:none; position: absolute;'>";
		menu += "	<a id='MC_createItem' class='item'><i class='plus icon'></i> Create</a>";
		menu += "	<a id='MC_deleteItem' class='item'><i class='minus icon'></i> Delete</a>";
		menu += "	<a id='MC_copyItem' class='item'><i class='copy icon'></i> Copy</a>";
		menu += "	<a id='MC_cutItem' class='item'><i class='cut icon'></i> Cut</a>";
		menu += "	<a id='MC_pasteItem' class='item'><i class='paste icon'></i> Paste</a>";
		menu += "	<a id='MC_newWindow' class='item'><i class='clone outline icon'></i> New Window</a>";
		menu += "</div>";
		
		return menu;
	};	
	
	this.refresh = function()
	{
		this.content = null;
		this.content = [];

		this.render();
	};
};