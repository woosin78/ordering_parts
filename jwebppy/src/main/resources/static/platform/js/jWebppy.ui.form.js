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

JpUiForm.checkbox = {
		getCheckboxByValue: function(checkbox, value)
		{
			if (JpUtilsObject.isNull(checkbox) || JpUtilsObject.isNull(value))
			{
				return null;
			};
			
			let element = null;
			
			$(checkbox).each(function() {
				if (this.value == value)
				{
					element = this;
					return false;
				};
			});
			
			return element;
		},
		
		check: function(checkbox)
		{
			if (JpUtilsObject.isNotNull(checkbox))
			{
				$(checkbox).attr("checked", true);
			}
		},
		checkByValue: function(checkbox, value)
		{
			if (JpUtilsObject.isNotNull(checkbox))
			{
				$(checkbox).each(function() {
					if (this.value == value)
					{
						this.checked = true;						
					}
				});
			}
		},		
		uncheck: function(checkbox)
		{
			if (JpUtilsObject.isNotNull(checkbox))
			{
				$(checkbox).attr("checked", false);
			}
		},
		uncheckByValue: function(checkbox)
		{
			if (JpUtilsObject.isNotNull(checkbox))
			{
				$(checkbox).each(function() {
					if (this.value == value)
					{
						this.checked = false;						
					}
				});
			}
		},		
		checked: function(checkbox)
		{
			if (JpUtilsObject.isNull(checkbox))
			{
				return null;
			}
			
			let elements = new Array();
			let index = 0;
			
			$(checkbox).each(function() {
				elements[index++] = this;
			});
			
			return elements;
		},
		checkedRadio: function(radio)
		{
			if (JpUtilsObject.isNull(radio))
			{
				return null;
			};
			
			let element = null;
			
			$(radio).each(function() {
				if (this.checked)
				{
					element = this;
					return false;
				}
			});
			
			return element;
		},
		checkedRadioValue: function(radio)
		{
			let element = JpUiForm.checkbox.checkedRadio(radio);
			
			if (JpUtilsObject.isNotNull(element))
			{
				return element.value;
			};
			
			return null;
		}		
};