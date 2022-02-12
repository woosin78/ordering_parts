let level2 = {};

function makeGmb()
{
	JpUtilsAjax.get({
		url: "/platform/mgmt/gmb/menu",
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
	    	
	    	$(".gmb-menu-list").html(level1.join(""));
	    	$(".gmb-menu-list li").css("width", Math.floor(100 / level1.length).toString() + "%");
	    	$(".gmb-menu-list").show();
	    	
	    	$(".gmb-menu-list > li").on("mouseover", function() {
	    		if (JpUtilsString.isNotEmpty($(this).attr("data-key")))
	    		{
	    			if (level2[$(this).attr("data-key")].length > 0)
	    			{
	    				$(".gmb-menu-area").css("height", 100);
	    				
	    				let submenu = [];
	    				
	    				$.each(level2[$(this).attr("data-key")], function(index, item){
	    					submenu.push("<li data-url='" + item.URL + "'>" + item.NAME + "</li>");
	    				});
	    				
	    				$(".gmb-menu-sub-list").html(submenu.join("")).show();
	    			}
	    			else
	    			{
	    				$(".gmb-menu-area").css("height", 50);
	    			};
	    			
	    			$(".gmb-menu-sub-list > li").not(".mainmenu-seperator").on("click", function() {
	    				document.location.href = $(this).attr("data-url");
	    			});
	    		};
	    	});
	    	
	    	$("div").on("mouseover", function(event) {
	    		if (!$(this).hasClass("gmb-menu-area") && !$(this).hasClass("gmb-menu-area"))
	    		{
	    			$(".gmb-menu-area").css("height", 50);
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