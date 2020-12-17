let JpUiForm = function(form)
{
	this.form = $(form);
	this.form;
	let _this = this;
	
	this.setValues = function(data)
	{
	    try
	    {
	    	if (typeof(data) == "string")
	    	{
	    		data = $.parseJSON(data);
	    	}
	    		
	    }
	    catch (e)
	    {
	        console.log(e.message);
	        return null;
	    };		
		
	    $.each(data.RESULT, function(name, value) {
	        _this.form.find("input:text[name=" + name + "],textarea[name=" + name + "],select[name=" + name + "]").val(value);
	        _this.form.find("input:checkbox[name=" + name + "][value='" + value + "'],input:radio[name=" + name + "][value='" + value + "']").attr("checked", true);
	    });
	};
	
	this.isEmptyVal = function(name)
	{
		return JpUtilsString.isEmpty(this.form.find("[name=" + name + "]").val());
	};
	
	this.val = function(name)
	{
		return JpUtilsString.trimToEmpty(this.form.find("[name=" + name + "]").val());
	};
	
	this.submit = function()
	{
		this.form.submit();
	};
};