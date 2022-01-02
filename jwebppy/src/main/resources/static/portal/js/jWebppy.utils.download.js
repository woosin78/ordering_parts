let JpUtilsDownload = {
		url: "/platform/mgmt/download",
		download: function(key, sCallback, fCallback) {
			//url = JpUtilsPath.url(JpUtilsString.defaultString(url, "/platform/mgmt/download"));
			
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