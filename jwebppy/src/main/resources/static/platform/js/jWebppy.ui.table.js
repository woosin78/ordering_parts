let JpUiTable = function(table)
{
	this.table = JpUtilsObject.toJquery(table);
	this.request;
	this.render;
	this.thCheckbox;
	this.settings = {
			checkbox: {},
			ajax: {}
	};	
	this.pageNumber;
	this.rowPerPage;
	this.totalPage;
	this.totalCount;
	
	let _this = this;
	
	this.attachEvent = function()
	{
		this.table.find("tbody tr td a[data-key]").on("click", function(event) {
			
			_this.changeRowStatus(this);
			
			if (JpUtilsObject.isNotNull(_this.settings.onClickDataKey))
			{
				_this.settings.onClickDataKey(this, $(this).attr("data-key"), $(this).closest("tr"));
			};
        });
		
		if (JpUtilsObject.isNotNull(this.settings.onClickPageNumber))
		{
			let previousPage = (this.pageNumber - 1 <= 0) ? 1 : this.pageNumber - 1;
			let nextPage = (this.pageNumber + 1 >= this.totalPage) ? this.totalPage : this.pageNumber + 1;
			
			this.table.find(".pagination-previous").attr("DATA-VALUE", previousPage);
			this.table.find(".pagination-next").attr("DATA-VALUE", nextPage);
			
			this.table.find(".pagination button").on("click", function() {
				_this.settings.onClickPageNumber($(this).attr("DATA-VALUE"));
			});
			
			this.table.find(".pagination-pageNumber").on("keydown", function(e) {
				if (e.keyCode == 13)
				{
					let pageNumber = e.target.value; 
					
					if (pageNumber <= 0)
					{
						pageNumber = 1;
					}
					else if (pageNumber > _this.totalPage)
					{
						pageNumber = _this.totalPage;
					};
					
					_this.settings.onClickPageNumber(pageNumber);	
				};
				
			});			
		};
		
		this.table.find("input:text, textarea").on("click", function() {
			$(this).select();
		});		
	};

	this.changeRowStatus = function(element)
	{
		this.table.find("tbody tr").removeClass("active");
		$(element).closest("tr").addClass("active");
	};
	
	this.getCheckedCount = function()
	{
		let count = 0;
		
		$.each(this.table.find("tbody tr td .ui.checkbox.data-key"), function(i, item) {
			if ($(item).checkbox("is checked"))
			{
				count++;
			}
		});
		
		return count;
	};
	
	this.getCheckedValues = function()
	{
		let values = [];
		
		$.each(this.table.find("tbody tr td .ui.checkbox.data-key"), function(i, item) {
			if ($(item).checkbox("is checked"))
			{
				values.push($(item).find("input[type=checkbox]").val());
			}
		});
		
		return values;
	};
	
	this.isChecked = function()
	{
		return (this.getCheckedCount == 0) ? false : true;
	};
	
	this.editColumns = function(callback)
	{
		if (_this.table.find("tr.empty").length == 0)
		{
			$.each(_this.table.find("tbody > tr"), function (trIndex, tr) {
				$.each($(this).find("td"), function (tdIndex, td) {
					$(this).html(callback(trIndex, tr, tdIndex, td, $(this).html()));
				});
			});
		};
	};
	
	this.initAfterRendering = function()
	{
		if (_this.table.find("tbody tr").length > 0)
		{
			if (JpUtilsObject.isNotNull(_this.settings.editColumns))
			{
				this.editColumns(_this.settings.editColumns);	
			};
			
			if (this.isTable())
			{
				this.table.tablesort();
			}
			else
			{
				this.table.find("table").tablesort();
			};
			
			$.each(_this.table.find(".ui.checkbox"), function(i, item) {
				if ($(this).find("input:checkbox").hasClass("master"))
				{
					let dataKeys = _this.table.find("tbody tr td .ui.checkbox.data-key");
					
					$(this).checkbox({
						onChecked: function()
						{
							dataKeys.checkbox("check");
						},
						onUnchecked: function()
						{
							dataKeys.checkbox("uncheck");
						}
					});
				}
				else
				{
					$(this).checkbox(_this.settings.checkbox);
				}
			});
			
			_this.table.append(_this.addPagination());
			
			_this.attachEvent();
		}
		else
		{
			_this.addEmptyRow();
		};
	};

	this.afterHandle = function(data)
	{
		this.table.html(new JpUiRender().render(data));
		
		this.initAfterRendering();
		
		this.table.show();		
	};
	
	this.load = function(data)
	{
		if (data != null)
		{
			this.afterHandle(data);		
		}
		else
		{
			let ajaxSettings =
			{
					dataType: "json",
					cache: false,
					data: ((JpUtilsObject.isNotNull(_this.settings.ajax.form)) ? this.settings.ajax.form.serialize() : false),
					success: function(data, textStatus, jqXHR)
					{
						_this.afterHandle(data);
					}			
			};
			
			$.extend(true, ajaxSettings, this.settings.ajax);
			
			_this.table.hide();
			
			if (JpUtilsString.equalsIgnoreCase(ajaxSettings.method, "POST"))
			{
				JpUtilsAjax.post(ajaxSettings);
			}
			else
			{
				JpUtilsAjax.get(ajaxSettings);
			}			
		};
	};
	
	this.render = function()
	{
		this.load();
	};
	
	this.isTable = function()
	{
		return (_this.table.prop("tagName") == "TABLE") ? true : false;
	};

	this.remove = function()
	{
		$.each(this.table.find("tbody tr td .ui.checkbox.data-key"), function(i, item) {
			if ($(this).checkbox("is checked"))
			{
				$(this).closest("tr").remove();
			};
		});
		
		this.addEmptyRow();
	};
	
	this.addEmptyRow = function()
	{
		if ($(this.table).find("tbody tr td").length == 0)
		{
			let content = [];

			content.push("<tr class='empty'>");
			content.push("	<td colspan='" + this.table.find("thead tr th").length + "'>" + JpUiGlobal.DEFAULT_EMPTY_TABLE_TEXT + "</td>");
			content.push("</tr>");
			
			this.table.find("tbody").html(content.join(""));
		};
	};
	
	this.getHeadCell = function(rowIndex, columnIndex)
	{
		return this.table.find("thead tr:eq(" + rowIndex + ") th:eq(" + columnIndex + ")");
	};
	
	this.getBodyCell = function(rowIndex, columnIndex)
	{
		return this.table.find("tbody tr:eq(" + rowIndex + ") td:eq(" + columnIndex + ")");
	};
	
	this.getActiveRow = function()
	{
		return this.table.find("tbody tr.active");
	};
	
	this.addPagination = function()
	{
		let table = this.table.find("table");
		let totalCount = table.attr("DATA-TOTAL-COUNT");
		
		if (JpUtilsNumber.defaultNumber(totalCount) == 0)
		{
			return "";
		};
		
		let pageNumber = Number(table.attr("DATA-PAGE-NUMBER"));
		let rowPerPage = Number(table.attr("DATA-ROW-PER-PAGE"));							
		let totalPage = Math.ceil(totalCount / rowPerPage);
		let prePage = pageNumber - 1;
		let nextPage = pageNumber + 1;
		
		if (totalPage == 0)
		{
			totalPage = 1;
		};
		
		this.totalCount = table.attr("DATA-TOTAL-COUNT");
		this.pageNumber = Number(table.attr("DATA-PAGE-NUMBER"));
		this.rowPerPage = Number(table.attr("DATA-ROW-PER-PAGE"));
		this.totalPage = Math.ceil(totalCount / rowPerPage);
		
		let content = [];
		content.push("<div class='ui grid' style='margin-top:0;'>");
		content.push("	<div class='twelve wide column'>");
		content.push("		<div class='ui mini form'>");
		content.push("			<div class='fields pagination'>");
		content.push("				<div class='field'>");
		content.push("					<button class='ui compact icon button pagination-first' DATA-VALUE='1'><i class='angle double left icon'></i></button>");
		content.push("					<button class='ui compact icon button pagination-previous'><i class='angle left icon'></i></button>");
		content.push("					<input type='number' class='ui mini pagination-pageNumber' style='width:6em;' value='" + pageNumber + "'>");
		content.push("				</div>");
		content.push("				<div>of " + JpUtilsNumber.addComma(totalPage) + "</div>");
		content.push("				<div class='field'>");
		content.push("					<button class='ui compact icon button pagination-next'><i class='angle right icon'></i></button>");
		content.push("					<button class='ui compact icon button pagination-last' DATA-VALUE='" + totalPage + "'><i class='angle double right icon'></i></button>");
		content.push("				</div>");
		content.push("			</div>");
		content.push("		</div>");
		content.push("	</div>");
		content.push("	<div class='four wide column summary right aligned'>Total rows : " + JpUtilsNumber.addComma(totalCount) + "</div>");
		content.push("</div>");

		return content.join("");
	};
};