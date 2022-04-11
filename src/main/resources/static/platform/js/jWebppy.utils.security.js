let JpUtilsSecurity = {};


JpUtilsSecurity.CSRF = {
		NAME: $("meta[name=_csrf_header]").attr("content"),
		TOKEN: $("meta[name=_csrf]").attr("content")
};