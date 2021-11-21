let JpUiAutoComplete = function(target, url) {
	this.target = target;
	this.url = url;
	this.type = "category";
	this.maxResult = 10;
	this.minCharacters = 3;
	this.url;
	this.onFirstLoad;
	this.onResponse;
	this.onSelect;
	
	let _this = this;
	
	this.go = function() {
		if (this.onFirstLoad != null && typeof(this.onFirstLoad) == "function")
		{
			this.onFirstLoad();
		};
		
		this.target.search({
			type: _this.type,
			maxResults: _this.maxResults,
			minCharacters: _this.minCharacters,
			fullTextSearch: false,
			apiSettings: {
				url: _this.url,
				onResponse: function(data) {
					if (_this.onResponse != null && typeof(_this.onResponse) == "function")
					{
						return _this.onResponse(data);
					}
					else
					{
						let response = { results: {} };

						$.each(data[0].RESULT, function(i, item) {
							console.log(item.LABEL);

							if (response.results[item.LABEL] === undefined)
							{
								response.results[item.LABEL] = {
									results : []
								};
							};

							response.results[item.LABEL].results.push({
								title: item.LABEL,
								description: item.TEXT
							});
						});

						return response;
					};
				}
			},
			onSelect: function(result, response) {
				if (_this.onSelect != null && typeof(_this.onSelect) == "function")
				{
					_this.onSelect(result, response);
				}
				console.log(result.title + "," + result.description);
			}
		});
	};			
};