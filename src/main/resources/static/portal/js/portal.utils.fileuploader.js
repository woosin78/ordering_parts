FileUploader = function(options)
{
	this.UUID = self.crypto.randomUUID();
	this.target = $(options.target);
	this.fileElementName = options.fileElementName;
	this.deletedElementName = options.deletedElementName;
	this.i18n = options.i18n;
	
	let _this = this;
	
	this.init = function()
	{
		if (JpUtilsObject.isNull(this.i18n))
		{
			this.i18n = {
				FILE: "File",
				DELETE: "Delete"
			};
		};
	};
	
	this.draw = function()
	{
		let html = [];
		html.push("<div class='split-row-more-compact'></div>");
		html.push("<button type='button' class='BTN_searchFile small_button'>" + this.i18n.FILE + "</button><div class='split'></div><button type='button' class='BTN_deleteFile small_button'>" + this.i18n.DELETE + "</button>");
		html.push("<div class='split-row-more-compact'></div>");
		html.push("<div style='display: block; height: 1px; background-color: #cccccc; margin-top:2px; margin-bottom:2px;'></div>");
		html.push("<div class='split-row-more-compact'></div>");
		html.push("<div class='attachedFiles'></div>");
		html.push("<input type='file' name='" + this.fileElementName + "' multiple style='display:none;'/>");

		this.target.html(html.join(""));
	};
	
	this.addFile = function(name, size, uflSeq)
	{
		let html = [];
		html.push("<div>");
		html.push("<input name='uflSeq_" + this.UUID + "' type='checkbox' value='" + JpUtilsString.trimToEmpty(uflSeq) + "' class='custom-ui' fileName='" + JpUtilsString.trimToEmpty(name) + "'/><label>" + name + "</label>");
		html.push("<div class='split2'></div><span>" + JpUtilsFile.displaySize(size) + "</span>");
		html.push("</div>");
		
		this.target.find(".attachedFiles").append(html.join(""));
	};
	
	this.render = function()
	{
		this.init();
		this.draw();
		this.attachEvent();
	};
	
	this.attachEvent = function()
	{
		let fileObj = $(_this.target.find("input[type=file][name=" + this.fileElementName + "]"));
		
		this.target.find(".BTN_deleteFile").on("click", function(event) {
			let files = Array.from(fileObj[0].files);
			const dataTransfer = new DataTransfer();
			
			$.each(_this.target.find("input[name=uflSeq_" + _this.UUID + "]"), function(i, item) {
				if ($(this).is(":checked"))
				{
					for (let j=0, length=files.length; j<length; j++)
					{
						try
						{
							if (JpUtilsString.equals(files[j].name, $(this).attr("fileName")))
							{
								files.splice(j, 1);
							};					
						}
						catch(e)
						{
							console.log(e);
						}
					};
					
					files.forEach(file => { dataTransfer.items.add(file); });
					
					if (JpUtilsString.isNotEmpty($(this).val()))
					{
						//삭제 될 파일 식별자
						_this.target.append("<input type='hidden' name='" + _this.deletedElementName + "' value='" + $(this).val() + "' />");
					};
					
					$(this).closest("div").remove();
					
					console.log(dataTransfer.files);
					
					fileObj[0].files = dataTransfer.files;
				};
			});
		});
		
		this.target.find(".BTN_searchFile").on("click", function() {
			fileObj.trigger("click");
		});	

		fileObj.on("change", function() {
			for (let i=0, length=this.files.length; i<length; i++)
			{
				_this.addFile(this.files[i].name, this.files[i].size);
			};
		});				
	};
	
	this.getFileCount = function()
	{
		return this.target.find("input:checkbox[name=uflSeq_" + this.UUID + "]").length;
	};	
};