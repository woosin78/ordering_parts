$(function(){
	// Ajax 기본 에러 처리
	$.ajaxSetup({
		error: function(e) {
			if (e.status == "401" || e.status == "403")
			{
				alert("Your session has expired or you don't have an authority on this page. Please logon or contact the manager.");
				document.location.href = "/";
				
				/*
				if (confirm("로그아웃 되었습니다. 다시 로그인 하시겠습니까?"))
				{
					location = location;
				}
				*/
			} else {
				alert("Something unexpected has occurred. Please try again in a few minutes.");
				
				/*
				var message = "";
				
				if (e.responseText) {
					message = e.responseText;
				} else {
					message = e.statusText;
				}
				if (!message) {
					message = "System Error";
				}
				
				if (e.status == "400"){
					alert(message);
				} else {
					alert("(" + e.status + ") " + message);
				}
				*/				
			}
		}
	});	
});    