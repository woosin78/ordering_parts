var OrderComponent = function(target)
{
	this.orderTypes = [];
	this.target = (target != null) ? $(target) : null;
	this.success;
	
	var _this = this;
	
	this.makeOrderType = function(target, name, defaultValue, fgAddEmptyOption, param)
	{
		this.target = $(target);
		
		$.ajax({
			url: "/portal/dbkr/scm/parts/domestic/order/order_type/data",
			async: false,//Order Type 은 async 필수
			data: param,
		    success: function(response, textStatus, jqXHR) 
		    {
		    	var data = response.RESULT;
		    	var options = [];
		    	
		    	if (fgAddEmptyOption)
		    	{
		    		options.push("<option value=''></option>");	
		    	};
		    	
		    	$.each(data, function(index, element) {
		    		options.push("<option value='" + this.AUART + "'>" + this.ZZAUKON + "</option>");
		    		
		    		_this.orderTypes.push(this);
		    	});

		    	var orderType = [];
		    	orderType.push("<select class='custom-ui' id='" + name + "' name='" + name + "'>");
		    	orderType.push(options.join(""));
		    	orderType.push("</select>");
		    	
		    	$(target).html(orderType.join(""));
		    	
		    	if (defaultValue != null)
		    	{
		    		$("#" + name).val(defaultValue);
		    	}
		    	else
		    	{
		    		$("#" + name).find("option:eq(0)").attr("selected", "true");
		    	};
		    	
		    	if (_this.success != null && typeof(_this.success) == "function")
		    	{
		    		_this.success(response, textStatus, jqXHR);
		    	};
		    }		
		});
	};
	
	this.getSelectedOrderType = function()
	{
		return this.target.find("option:selected").val();
	};	
	
	this.getSelectedPriceGroup = function()
	{
		var orderType = this.target.find("option:selected").val();
		
		for (var i=0, length=this.orderTypes.length; i<length; i++)
		{
			if (this.orderTypes[i].AUART == orderType)
			{
				return this.orderTypes[i].KONDA;
			}
		};
		
		return null;
	};
	
	this.getSelectedDocType = function()
	{
		var orderType = this.target.find("option:selected").val();
		
		for (var i=0, length=this.orderTypes.length; i<length; i++)
		{
			if (this.orderTypes[i].AUART == orderType)
			{
				return this.orderTypes[i].VBTYP;
			}
		};
		
		return null;
	};	
}