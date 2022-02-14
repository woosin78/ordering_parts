let level2 = {};

function makeGmb()
{
	JpUtilsAjax.get({
		url: "/platform/mgmt/gnb/menu",
		success: function(data, textStatus, jqXHR)
		{
			let items = data.RESULT;
			let level1 = [];

	    	for (let i=0, length=items.length; i<length; i++)
	    	{
	    		if (items[i].TYPE == "R")
	    		{
	    			$.each(items[i].SUB_ITEMS, function(index, element) {
	    				level1.push("<li data-key='" + element.KEY + "' data-url='" + JpUtilsString.trimToEmpty(element.URL) + "'>" + element.NAME + "</li>");
	    				
	    				setLevel2(element);
	    			});
	    		}
	    		else
	    		{
	    			level1.push("<li data-key='" + items[i].KEY + "' data-url='" + JpUtilsString.trimToEmpty(items[i].URL) + "'>" + items[i].NAME + "</li>");
	    			
	    			if (items[i].TYPE == "M")
	    			{
	    				setLevel2(item[i])
	    			};
	    		};
	    	};
	    	
	    	$(".gnb-menu-list").html(level1.join(""));
	    	$(".gnb-menu-list li").css("width", Math.floor(100 / level1.length).toString() + "%");
	    	$(".gnb-menu-list").show();
	    	
	    	$(".gnb-menu-list > li").on("click", function() {
	    		if (JpUtilsString.isNotEmpty($(this).attr("data-url")))
	    		{
	    			document.location.href = $(this).attr("data-url");
	    		};
	    	});
	    	
	    	$(".gnb-menu-list > li").on("mouseover", function() {
	    		if (JpUtilsString.isNotEmpty($(this).attr("data-key")))
	    		{
	    			if (level2[$(this).attr("data-key")].length > 0)
	    			{
	    				$(".gnb-menu-area").css("height", 100);
	    				
	    				let submenu = [];
	    				
	    				$.each(level2[$(this).attr("data-key")], function(index, item){
	    					submenu.push("<li data-url='" + item.URL + "'>" + item.NAME + "</li>");
	    				});
	    				
	    				$(".gnb-menu-sub-list").html(submenu.join("")).show();
	    			}
	    			else
	    			{
	    				$(".gnb-menu-area").css("height", 50);
	    			};
	    			
	    			$(".gnb-menu-sub-list > li").not(".mainmenu-seperator").on("click", function() {
	    				document.location.href = $(this).attr("data-url");
	    			});
	    		};
	    	});
	    	
	    	$("div").on("mouseover", function(event) {
	    		if (!$(this).hasClass("gnb-menu-area"))
	    		{
	    			$(".gnb-menu-area").css("height", 50);
	    		};
	    		
	    		return false;
	    	});
		}
	});
};

function setLevel2(item)
{
	level2[item.KEY] = [];
	
	$.each(item.SUB_ITEMS, function(index, element) {
		level2[item.KEY].push(element);
	});
};