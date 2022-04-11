let GridFileDownload = function(target)
{
	this.target = target;
	this.url = "/portal/iv/download/grid/excel";
	this.fileName;
	
	let _this = this;
	
	this.excelDownload = function()
	{
		ProgressBar.show("Downloading");
		
		let options = {
				httpMethod: "POST",
				data:  { fileName: _this.fileName, data: JSON.stringify(_this.getGridData()), "_csrf": $("meta[name=_csrf]").attr("content") },				
				successCallback: function(url)
				{
					ProgressBar.hide();
				},
				failCallback: function(responseHtml, url, error)
				{
					 alert("There was sonmething wrong with downloading. Please try it again.");
					 ProgressBar.hide();
				}
		};
		
		$.fileDownload(_this.url, options);
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