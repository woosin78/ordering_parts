let OrderComponent = function(target)
{
	this.orderTypes = [];
	this.target = (target != null) ? $(target) : null;
	this.isAsync = true;//주문생성 화면에서 Order Type 은 async true 필수
	this.success;
	
	let _this = this;
	
	this.makeOrderType = function(target, name, defaultValue, fgAddEmptyOption, param)
	{
		this.target = $(target);
		
		$.ajax({
			url: "/portal/iv/hq/parts/domestic/order/create/order_type/data",
			async: _this.isAsync,
			data: param,
		    success: function(response, textStatus, jqXHR) 
		    {
		    	let data = response.RESULT;
		    	let options = [];
		    	
		    	if (fgAddEmptyOption)
		    	{
		    		options.push("<option value=''></option>");	
		    	};
		    	
		    	$.each(data, function(index, element) {
		    		options.push("<option value='" + this.AUART + "'>" + this.ZZAUKON + "</option>");
		    		
		    		_this.orderTypes.push(this);
		    	});

		    	let orderType = [];
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
		let orderType = this.target.find("option:selected").val();
		
		for (let i=0, length=this.orderTypes.length; i<length; i++)
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
		let orderType = this.target.find("option:selected").val();
		
		for (let i=0, length=this.orderTypes.length; i<length; i++)
		{
			if (this.orderTypes[i].AUART == orderType)
			{
				return this.orderTypes[i].VBTYP;
			}
		};
		
		return null;
	};	
}