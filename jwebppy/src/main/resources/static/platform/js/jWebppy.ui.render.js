let JpUiRender = function()
{
	let _this = this;
	
	this.checkFormElement = function(element)	
	{
		let tagName = JpUtilsString.trimToEmpty(element.tagName).toUpperCase();

		if (tagName == "")
		{
			return false;
		};
		
		let elementType = "";
		
		if (tagName == "GROUP")
		{
			if (element.attributes != null)
			{
				for (let i=0, length=element.attributes.length; i<length; i++)
				{
					if (JpUtilsString.equalsIgnoreCase(element.attributes[i].name, "TYPE") && JpUtilsString.equalsIgnoreCase(element.attributes[i].value, "FORM"))
					{
						return true;
					};
				};				
			};
		}
		
		if (tagName == "INPUT" || tagName == "SELECT" || tagName == "TEXTAREA")
		{
			return true;
		};
		
		return false;
	};
	
	this.render = function(response)
	{
	    let data = JpUtilsAjax.toJson(response);
	    
	    let result = [];

	    if (data.RESULT != null && Array.isArray(data.RESULT))
	    {
	    	result.push("<div class='ui one column grid'>");
		    
		    data.RESULT.forEach(function(element, index) {
		    	let content = _this.makeElement(element);
		    	
		    	if (content.indexOf("HIDDEN") > -1)
		    	{
		    		result.push(content);
		    	}
		    	else
		    	{
			    	result.push("<div class='column' style='padding-top: 0.5rem; padding-bottom: 0.5rem;'>");
			    	result.push(content);
			    	result.push("</div>");		    		
		    	};
		    });
		
		    result.push("</div>");
	    };
	    
	    return result.join("");
	};
	
	this.makeElement = function(element)
	{
		let content = [];
		let tagName = element.tagName.toUpperCase();
		let isFormElement = _this.checkFormElement(element);
		let attributes = [];
		let isRequired = false;
		let isHiddenFormElement = false;
		let isDateFormElement = false;
		let typeFormElement = "TEXT";
		let closeTags = [];
		let dataKey = "";
		
		if (element.attributes != null)
		{
			element.attributes.forEach(function(attribute) {
				let attributeName = $.trim(attribute.name).toUpperCase();
				let attributeValue = $.trim(attribute.value);
					
				if (attributeName == "REQUIRED")
				{
					isRequired = true;
				};
				
				if (isFormElement)
				{
					if (attributeName == "TYPE")
					{
						typeFormElement = attributeValue.toUpperCase();
						
						if (typeFormElement == "HIDDEN")
						{
							isHiddenFormElement = true;
						};
					};
				};

				if (isFormElement && attributeName == "TYPE" && JpUtilsString.equalsIgnoreCase(attributeValue, "DATE"))
				{
					attributes.push(attributeName + "='TEXT' ");
					attributes.push("readonly ");					
				}
				else
				{
					if (JpUtilsString.isNotEmpty(attributeName))
					{
						if (JpUtilsString.isNotEmpty(attributeValue))
						{
							attributes.push(attributeName + "='" + attributeValue + "' ");
						}
						else
						{
							attributes.push(attributeName + " ");
						};
					}
				};
				
				if (JpUtilsString.equalsIgnoreCase(attributeName, "DATA-KEY"))
				{
					dataKey = attributeValue;
				}
			});
		};
		
		if (isFormElement)
		{
			if (!isHiddenFormElement)
			{
				content.push("<div class='field" + (isRequired ? " required" : "") + "'>");
				closeTags.push("</div>");

				if (typeFormElement != "CHECKBOX")
				{
					if (element.label != null)
					{
						let label = $.trim(element.label.text);
						
						if (label != "")
						{
							content.push("<label>" + label + "</label>");
						};						
					};
				};
			};
		};
		
		if (typeFormElement == "DROPDOWN")
		{
			content.push(this.makeDropDown(element));
		}
		else
		{
			if (typeFormElement == "DATE")
			{
				content.push("<div class='ui calendar'><div class='ui input left icon'><i class='calendar icon'></i>");
				closeTags.push("</div></div>");
			};
			
			if (typeFormElement == "CHECKBOX")
			{
				content.push("<div class='ui checkbox");
				
				if (dataKey != "")
				{
					content.push(" data-key");
				}
				
				content.push("'>");
				
				closeTags.push("</div>");
			};			
			
			/* start main tag */
	    	content.push("<" + tagName + " " + attributes.join(""));
	    	
	    	if (isHiddenFormElement)
    		{
	    		content.push(" style='display:none;' ")
    		};
    		
    		content.push(">");
	    	
			if (typeFormElement == "CHECKBOX")
			{
				if (element.label != null)
				{
					let label = $.trim(element.label.text);
					
					if (label != "")
					{
						content.push("<label>" + label + "</label>");
					};					
				};
			};
	    	
	    	if (element.text != null)
	    	{
	    		content.push(element.text);
	    	};
	    	
	    	if (element.children != null)
	    	{
	        	element.children.forEach(function(subElement) {
	        		content.push(_this.makeElement(subElement));
	        	});
	    	};
	    	
	    	content.push("</" + tagName + ">");
	    	/* finish main tag */		
		};
    	
    	content.push(closeTags.join(""));
    	
    	return content.join("");
	};
	
	this.makeDropDown = function(element)
	{
		let content = [];
		let attributes = {};
		
		if (element.attributes != null)
		{
			element.attributes.forEach(function(attribute) {
				attributes[$.trim(attribute.name).toUpperCase()] = $.trim(attribute.value);
			});
		};
		
		content.push("<div class='ui fluid search selection dropdown " + attributes["NAME"] + "'>");
		content.push("<input type='hidden' ");
		
		if (JpUtilsObject.isNotNull(attributes["ID"]))
		{
			content.push("id='" + attributes["ID"] + "' ");
		};
		
		content.push("name='" + attributes["NAME"] + "' ");
		
		if (JpUtilsObject.isNotNull(attributes["VALUE"]))
		{
			content.push("value='" + attributes["VALUE"] + "' ");
		};
		
		content.push("/>");
		content.push("<i class='dropdown icon'></i>");
		
		if (JpUtilsObject.isNotNull(attributes["DEFAULT_TEXT"]))
		{
			content.push("<div class='default text'>" + attributes["DEFAULT_TEXT"] + "</div>");
		};		
		
		content.push("<div class='menu'>");
		
    	if (element.children != null)
    	{
        	element.children.forEach(function(subElement) {
        		let subAttributes = {};
        		
        		subElement.attributes.forEach(function(attribute) {
    				subAttributes[$.trim(attribute.name).toUpperCase()] = $.trim(attribute.value);
    			});        		
        		
        		content.push("<div class='item' data-value='" + subAttributes["VALUE"] + "'>" + subElement.text + "</div>");
        	});
    	};		
		
		content.push("</div></div>");
		
		return content.join("");
	};
};	
