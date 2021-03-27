var GridFileDownload = function(target)
{
	this.target = target;
	this.url = "/portal/dbkr/download/grid/excel";
	this.fileName;
	
	let _this = this;
	
	this.excelDownload = function()
	{
		let download = $("iframe[name=IFM_download]");
		
		if (download.length == 0)
		{
			$("body").append("<iframe name='IFM_download' style='display:none;'></iframe>");
			
			let form = [];
			form.push("<form id='FORM_download' name='FORM_download' method='post' target='IFM_download'>");
			form.push("<input type='hidden' name='fileName'/>")
			form.push("<input type='hidden' name='data'/>")
			form.push("</form>");
			
			$("body").append(form.join(""));
		};
		
		console.log(JSON.stringify(this.getGridData()));
		
		$("#FORM_download input[name=fileName]").val(this.fileName);
		$("#FORM_download input[name=data]").val(JSON.stringify(this.getGridData()));
		
		$("#FORM_download").attr("action", this.url).submit();
	};
	
	this.getGridData = function()
	{
		let header = [];
		let body = [];
		
		$(this.target.row(0).nodes()).find("td").each(function(index, element) {
			
			if (!$(this).hasClass("skip-download"))
			{
				header.push($(_this.target.columns(index).header()).html());	
			}
		});
		
		for (let i=0, length=this.target.rows().data().length; i<length; i++)
		{
			let data = [];
			
			$(this.target.row(i).nodes()).find("td").each(function(index, element) {
				
				if (!$(this).hasClass("skip-download"))
				{
					let value;
					
					try
					{
						value = _this.target.cell($(element)).data();
					}
					catch (e)
					{
						value = $(this).html();
					};

					data.push(value);
				};
			});
			
			body.push(data);
		};
		
		let result = {};
		result.header = header;
		result.body = body;
		
		return result;
	};
};