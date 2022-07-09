const GNB_LEVEL2 = {};

function makeGnb()
{
	let GNB_MENU_PER_PAGE = 9;//한 화면에 보여질 메뉴 개수
	let GNB_LEVEL1_HEIGHT = JpUtilsNumber.defaultNumber($(".gnb-menu-area").height(), 50);//GNB Level1's height
	let GNB_LEVEL1_ACTIVE_HEIGHT = 125;//Level1 에 마우스오버 뵀을 때 Level1 + Level2 높이 
	
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
	    	
	    	let level1Length = level1.length;
	    	
	    	for (let i=level1Length-level1.length; i>0; i--)
	    	{
				level1.push("<li></li>");
			};			
	    	
	    	$(".gnb-menu-list2").html(level1.slice(0, level1Length).join(""));
	    	$(".gnb-menu-list2 li").css("width", Math.floor(100 / level1Length).toString() + "%");
	    	$(".gnb-menu-list2").show();
	    	$('.gnb-menu-list2').bxSlider({
			    slideWidth: 10000,  
			    autoHover: true, 
			    pager: false, 
			    moveSlides: GNB_MENU_PER_PAGE, 
			    minSlides: GNB_MENU_PER_PAGE,
			    maxSlides: GNB_MENU_PER_PAGE,
			    slideMargin: 0, 			    
			    infiniteLoop: false,
			    hideControlOnEnd:true,
			});
			$(".bx-controls-direction").hide();	    	
	    	
	    	$(".gnb-menu-list2 > li").on("click", function() {
				moveToGnbMenu($(this));
	    	});
	    	
	    	$(".gnb-menu-list2 > li").on("mouseover", function() {
					
				let key = JpUtilsString.trimToEmpty($(this).attr("data-key")); 
				
	    		if (JpUtilsString.isNotEmpty(key))
	    		{
	    			if (GNB_LEVEL2[key].length > 0)
	    			{
	    				$(".gnb-menu-area").css("height", GNB_LEVEL1_ACTIVE_HEIGHT);
	    				
	    				let submenu = [];
	    				
	    				$.each(GNB_LEVEL2[key], function(index, item){
	    					submenu.push(makeGnbItem(item));
	    				});
	    				
	    				$(".gnb-menu-sub-list")
	    					.css("height", (GNB_LEVEL1_ACTIVE_HEIGHT-GNB_LEVEL1_HEIGHT))
	    					.html(submenu.join(""))
	    					.show();
	    			}
	    			else
	    			{
	    				$(".gnb-menu-area").css("height", GNB_LEVEL1_HEIGHT);
	    			};
	    			
	    			$(".gnb-menu-sub-list > li").not(".mainmenu-seperator").on("click", function() {

	    				moveToGnbMenu($(this));
	    			});
	    		};
	    	});

			if (level1Length > GNB_MENU_PER_PAGE)
			{
				$(".gnb-menu-area").on("mouseover", function() {
					$(".bx-controls-direction").show();          
				});      
			};
			
			$(".gnb-menu-area").on("mouseout", function() {
				if (level1Length > GNB_MENU_PER_PAGE)
				{       
					$(".bx-controls-direction").hide();
				};         
				$(".gnb-menu-area").css("height", GNB_LEVEL1_HEIGHT);
			});
		
			$(".gnb-menu-sub-list").on("mouseover", function() {
				$(".gnb-menu-area").css("height", GNB_LEVEL1_ACTIVE_HEIGHT);

				if (level1Length > GNB_MENU_PER_PAGE)
				{
					$(".bx-controls-direction").show();       
				};                     
			});
		
			$(".gnb-menu-list2 > li").on("mouseover", function() {
				$(".gnb-menu-area").css("height", GNB_LEVEL1_ACTIVE_HEIGHT);
			});
		
			$(".bx-controls-direction").on("mouseover", function() {   
				if (level1Length > GNB_MENU_PER_PAGE)
				{
					$(".bx-controls-direction").show();   
				}   
				$(".gnb-menu-area").css("height", GNB_LEVEL1_HEIGHT);    
			});
		}
	});
};

function setGnbLevel2(item)
{
	GNB_LEVEL2[item.KEY] = [];
	
	$.each(item.SUB_ITEMS, function(index, element) {
		GNB_LEVEL2[item.KEY].push(element);
	});
};

function makeGnbItem(item)
{
	return "<li class='slide' data-key='" + item.KEY + "' data-url='" + JpUtilsString.trimToEmpty(item.URL) + "' data-launch_type='" + JpUtilsString.trimToEmpty(item.LAUNCH_TYPE) + "' data-width='" + JpUtilsString.trimToEmpty(item.WIDTH) + "' data-height='" + JpUtilsString.trimToEmpty(item.HEIGHT) + "'>" + item.NAME + "</li>";
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

$(document).ready(function () {	    	
	$(".onclick").on("click", function() { $(".bx-controls-direction").hide();	});
});

