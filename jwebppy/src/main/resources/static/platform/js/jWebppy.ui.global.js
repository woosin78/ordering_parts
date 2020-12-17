let DOCUMENT_ELEMENTS_SIZE = "small";

let JpUiGlobal = {
	changeSizeOfElement: function()
	{
		$(".render, .ui.breadcrumb, .ui.button, .ui.form, .ui.input, .ui.table, .ui.segment, .ui.tab, .ui.menu, .ui.dropdown").not(".pagination, .right_shortcut_menu").addClass(DOCUMENT_ELEMENTS_SIZE);
	},
	showEmptyMessage: function()
	{
		return "<img class='ui wireframe image' src='/img/common/paragraph.png' style='opacity:0.3'/>";
	},
	url: function(url)
	{
		let context = /*[[@{/}]]*/ '';
		
		return ((context != "") ? "/" : "") + url;
	},

	//Data Formats
	DEFAULT_EMPTY_TABLE_TEXT: "There is no data.",
	DEFAULT_DATE_FORMAT: "YYYY-MM-DD",
	DEFAULT_TIME_FORMAT: "HH:mm:ss",
	DEFAULT_DATE_TIME_FORMAT: "YYYY-MM-DD HH:mm:ss"
};