JpUiForm = function(form)
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
		let element = this.form.find("[name=" + name + "]");
		
		let tagName = element.prop("tagName");
		let type = element.attr("type").toLowerCase();
		
		if  (type == "checkbox")
		{
			return JpUiForm.checkbox.checkedValues(element);
		}
		else if (type == "radio")
		{
			return JpUtilsString.trimToEmpty(JpUiForm.checkbox.checkedRadioValue(element));
		}
		else
		{
			return JpUtilsString.trimToEmpty(this.form.find("[name=" + name + "]").val());	
		};
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
		checkedValues: function(checkbox)
		{
			let element = JpUiForm.checkbox.checked(checkbox);
			
			let values = new Array();
			let index = 0;
			
			$(checkbox).each(function() {
				values[index++] = $(this).val();
			});			
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

JpUiForm.input = {
	on: {
		select: function()
		{
			for (let i=0; i<arguments.length; i++)
			{
				if (Array.isArray(arguments[i]))
				{
					$(arguments[i]).each(function(index, item) {
						//$(this).select();
						
						$(item).on("click", function() { $(this).select(); });
					});
				}
				else
				{
					$(arguments[i]).on("click", function() { $(this).select(); });	
				};
			};
		},
		focus: function()
		{
			for (let i=0; i<arguments.length; i++)
			{
				if (Array.isArray(arguments[i]))
				{
					$(arguments[i]).each(function(index, item) {
						$(this).focus();
					});
				}
				else
				{
					$(arguments[i]).focus();	
				};			
			};
		},
		enter: function(element, func)
		{
			if (JpUtilsObject.isNotNull(element))
			{
				if (element.length > 1)
				{
					$(element).each(function(index, item) {
						$(item).on("keydown", function(event) {
							if (event.keyCode == 13)
							{
								func(this, index);
							};
						});
					});
				}
				else
				{
					$(element).on("keydown", function(event) {
						if (event.keyCode == 13)
						{
							func(this);
						};
					});
				};			
			};
		},
		keyup: function(element, func)
		{
			if (JpUtilsObject.isNotNull(element))
			{
				if (Array.isArray(element))
				{
					$(element).not(".autocomplete").each(function(index, item) {
						$(item).on("keyup", function() {
							func(item);
						});
					});
				}
				else
				{
					$(element).not(".autocomplete").on("keyup", function() {
						func(this);
					});
				};			
			};
		}
	},
	validation: {
		number: function(element, min, max)
		{
			min = JpUtilsNumber.defaultNumber(min, 1);
			max = JpUtilsNumber.defaultNumber(max, Number.MAX_SAFE_INTEGER);
			
			JpUiForm.input.on.keyup(element, function(item) {
				if (!jQuery.isNumeric($(item).val()))
				{
					$(item).val("");
					return;					
				};
				
				let num = parseFloat($(item).val());
				
				if (num < min)
				{
					num = min;
				}
				else if (num > max)
				{
					num = max;
				};
				
				$(item).val(num);
			});
		}		
	}
};
