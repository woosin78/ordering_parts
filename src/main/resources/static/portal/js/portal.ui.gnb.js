let gnbLevel2 = {};

function makeGnb()
{
	JpUtilsAjax.get({
		url: "/platform/mgmt/gnb/menu",
		success: function(data, textStatus, jqXHR)
		{
			let items = data.RESULT;
			let level1 = [];
			let MENU_PER_PAGE = 7;
			
	    	for (let i=0, length=items.length; i<length; i++)
	    	{
	    		if (items[i].TYPE == "R")
	    		{
	    			$.each(items[i].SUB_ITEMS, function(index, element) {
	    				level1.push(makeGnbItem(element));
	    				
	    				setGnbLevel2(element);
	    			});
	    		}
	    		else
	    		{
	    			level1.push(makeGnbItem(items[i]));
	    			
	    			if (items[i].TYPE == "M")
	    			{
	    				setGnbLevel2(item[i]);
	    			};
	    		};
	    	};
	    	
	    	for (let i=MENU_PER_PAGE-level1.length; i>0; i--)
	    	{
				level1.push("<li></li>");
			};
	    	
	    	$(".gnb-menu-list").html(level1.slice(0, MENU_PER_PAGE).join(""));
	    	$(".gnb-menu-list li").css("width", Math.floor(100 / MENU_PER_PAGE).toString() + "%");
	    	$(".gnb-menu-list").show();
	    	
	    	$(".gnb-menu-list > li").on("click", function() {
				moveToGnbMenu($(this));
	    	});
	    	
	    	$(".gnb-menu-list > li").on("mouseover", function() {
				let key = JpUtilsString.trimToEmpty($(this).attr("data-key")); 
				
				
	    		if (JpUtilsString.isNotEmpty(key))
	    		{
	    			if (gnbLevel2[key].length > 0)
	    			{
	    				$(".gnb-menu-area").css("height", 100);
	    				
	    				let submenu = [];
	    				
	    				$.each(gnbLevel2[key], function(index, item){
	    					submenu.push(makeGnbItem(item));
	    				});
	    				
	    				$(".gnb-menu-sub-list").html(submenu.join("")).show();
	    			}
	    			else
	    			{
	    				$(".gnb-menu-area").css("height", 50);
	    			};
	    			
	    			$(".gnb-menu-sub-list > li").not(".mainmenu-seperator").on("click", function() {

	    				moveToGnbMenu($(this));
	    			});
	    		};
	    	});
	    	
	    	$("div").on("mouseover", function() {
	    		if (!$(this).hasClass("gnb-menu-area"))
	    		{
	    			$(".gnb-menu-area").css("height", 50);
	    		};
	    		
	    		return false;
	    	});
		}
	});
};

function setGnbLevel2(item)
{
	gnbLevel2[item.KEY] = [];
	
	$.each(item.SUB_ITEMS, function(index, element) {
		gnbLevel2[item.KEY].push(element);
	});
};

function makeGnbItem(item)
{
	return "<li data-key='" + item.KEY + "' data-url='" + JpUtilsString.trimToEmpty(item.URL) + "' data-launch_type='" + JpUtilsString.trimToEmpty(item.LAUNCH_TYPE) + "' data-width='" + JpUtilsString.trimToEmpty(item.WIDTH) + "' data-height='" + JpUtilsString.trimToEmpty(item.HEIGHT) + "'>" + item.NAME + "</li>";
};

function moveToGnbMenu(item)
{
	let url = JpUtilsString.trimToEmpty($(item).attr("data-url"));
	
	if (JpUtilsString.isNotEmpty(url))
	{
		let launchType = $(item).attr("data-launch_type");
		
		if (JpUtilsString.equals(launchType, "N"))
		{
			window.open(url, $(item).html());
		}
		else if (JpUtilsString.equals(launchType, "N"))
		{
			Popup.open(url, $(item).attr("WIDTH"), $(item).attr("HEIGHT"));						
		} 
		else
		{
			document.location.href = url;	
		};
	};
};