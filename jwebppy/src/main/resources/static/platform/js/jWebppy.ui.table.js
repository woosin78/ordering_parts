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
			this.table.find(".pagination a.item").on("click", function() {
				_this.settings.onClickPageNumber($(this).attr("DATA-VALUE"));
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
		
		if (totalPage == 0)
		{
			totalPage = 1;
		};
		
		let startPage = 1;
		let endPage = 9;
		
		if (pageNumber > 5)
		{
			startPage = pageNumber - 4;
			endPage = pageNumber + 4;
		};
		
		if (startPage <= 0)
		{
			startPage = 1;
		};
		
		if (endPage > totalPage)
		{
			endPage = totalPage;
		};
		
		let content = [];
		content.push("<div class='ui grid' style='margin-top:0;'>");
		content.push("	<div class='twelve wide column'>");
		content.push("		<div class='ui borderless menu pagination tiny'>");
		content.push("			<a class='item' DATA-VALUE='1'><i class='angle double left icon'></i></a>");
		
		for (let i=startPage; i<=endPage; i++)
		{
			let fgActive = "";
			
			if (i == pageNumber)
			{
				fgActive = "active";
			}
			
			content.push("<a class='item " + fgActive + "' DATA-VALUE='" + i + "'>" + i + "</a>");
		};
		content.push("	<a class='item' DATA-VALUE='" + totalPage + "'><i class='angle double right icon'></i></a>");
		content.push("		</div>");
		content.push("	</div>");
		content.push("	<div class='four wide column summary right aligned'>");
		content.push("		Total rows : " + JpUtilsNumber.addComma(totalCount) + " / Page " + JpUtilsNumber.addComma(pageNumber) + " of " +  JpUtilsNumber.addComma(totalPage));
		content.push("	</div>");
		content.push("</div>");
		
		return content.join("");
	};
};