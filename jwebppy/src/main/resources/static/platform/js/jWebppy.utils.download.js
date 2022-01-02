let JpUtilsDownload = {
		url: "/platform/mgmt/download",
		download: function(key, sCallback, fCallback) {

			let options = {
					data: { key: key },
					successCallback: function(url)
					{
						if (JpUtilsObject.isNotNull(sCallback)) {
							sCallback(url);
						}
					},
					failCallback: function(responseHtml, url, error)
					{
						if (JpUtilsObject.isNotNull(fCallback)) {
							fCallback(responseHtml, url, error);
						}
					}
			};
			
			$.fileDownload(url, options);
		}
}