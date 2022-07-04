
/*
 * RFC I/F Log
 */
$(document).on("keydown", function(e) {
	if (e.ctrlKey && e.key === "z")
	{
		if (JpUiDimmer.isActive())
		{
			JpUiDimmer.hide();			
		}
		else
		{
			JpUtilsAjax.get({
				url: "/platform/mgmt/log/shortcut/list/data",
				global: false,
				data: {referer: window.location.pathname},
				success: function(data, textStatus, jqXHR)
				{
					let result = data.RESULT;
					let content = "<table id='TBL_rfcLog' class='condition' style='width:600px;'><tr><th class='center'>RFC Name</th><th class='center'>Reg.Date</th></tr>";
					
					if (JpUtilsObject.isNotNull(result))
					{
						$.each(result, function(index, element) {
							let link = "<a class='link list'>" + element.command + "</a>";
							
							if (JpUtilsString.isNotEmpty(element.dlSeq))
							{
								link += " <a class='link view' data-key='" + element.dlSeq + "'><i class='external alternate icon'></i></a>";
							};
							
							content += "<tr style='background-color:#fff'>";
							content += "<td class='left'>" + link + "</td><td class='center'>" + JpUtilsString.trimToEmpty(element.displayRegDate) + "</td>";
							content += "</tr>";
						});						
					}
					else
					{
						content += "<tr style='background-color:#fff'><td colspan='2' class='center'>There is no data.</tr>";						
					};
					
					content += "</tbody></table>";
					
					JpUiDimmer.alert(content);
					
					$("#TBL_rfcLog a.link.list").on("click", function() {
						Popup.open("/platform/mgmt/log/list?command=" + $(this).html() + "&regUsername=" + GLOBAL_CONST.USERNAME);	
					});

					$("#TBL_rfcLog a.link.view").on("click", function() {
						Popup.open("/platform/mgmt/log/view?dlSeq=" + $(this).attr("data-key"), 1024, screen.availHeight*0.8, 'Y', 'Y');	
					});					
				},
				error: function(jqXHR, textStatus, errorThrown)
				{
					console.log(errorThrown);
				}
			});			
		};
	};
});