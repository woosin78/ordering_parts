
// 그리드에 값 없을때 표시할 내용
let emptyMessage = 
			"<div class='empty-message-area'>"
				+"<img src='/portal/img/no-data.png'/>"
				+"<div>There is no searching result.</div>"
			+"</div>";

// 그리드 기본 설정
$(function(){
	// DataTables 기본값 설정
	$.extend( $.fn.dataTable.defaults, {
		dom: 'rlftip',
		scrollX : true,
		scrollY : false,
		scrollCollapse: true,
		select: {
			style : 'single',
			blurable : false,
			toggleable: false
		},
		processing: true,
		lengthChange: false,
		searching : false,
		ordering : false,
		order: [],
		info : false,
		paging : false,
		pageLength: 20,
		pagingType: "first_last_numbers",
		language : {
			processing : "<div class='loading_image'></div>",
			emptyTable : emptyMessage,
			zeroRecords : "&nbsp;",
			paginate: {
				first: "<<",
				previous: "<",
				next: ">",
				last: ">>",
			}
		},
		deferRender: true
	});
});

/* 
$dataTable을 $parent 높이에 맞춤. window.resize에 사용. 
prevHeight 는 생략 가능
bottomHeight 는 Bottom 영역의 높이
*/
function dataTableResize($dataTable, $parent, prevHeight){
	/*
	let bottomHeight = $(".footer-area").outerHeight();
	let height = $parent.innerHeight() - bottomHeight;
	*/
	let height = $parent.innerHeight();
	
	if (typeof prevHeight == "undefined" || prevHeight != height) {
		// 그리드 헤더 빼주기
		height -= $parent.find(".dataTables_scrollHead").outerHeight() + 1;  // + 1 border
		// 그리드 풋터 빼주기
		height -= $parent.find(".dataTables_paginate").outerHeight() || 0;
		$parent.find(".dataTables_scrollBody").css({"maxHeight": height + "px"});
		$dataTable.fixedColumns().relayout();
		//$dataTable.draw();
	}
	
	return height;
}

/* 
$dataTable을 $parent 높이에 맞춤. window.resize에 사용. 
prevHeight 는 생략 가능
bottomHeight 는 Bottom 영역의 높이
*/
function dataTableResizeWithoutScrollY($dataTable, $parent){
	let maxHeight = $parent.find(".dataTables_scrollBody").prop("scrollHeight");
	
	if ($parent.find(".dataTables_scrollBody").prop("scrollWidth") > $parent.find(".dataTables_scrollBody").width())
	{
		maxHeight += $(".footer-area").height();
	};
	
	$parent.find(".dataTables_scrollBody").css({
		"maxHeight": maxHeight + "px"
	});
	
	$(".footer-area").remove();
	
	let bottom = "";
	bottom += "<div class='footer-area' style='margin-top: 10px'>";
	bottom += "	<div class='footer2'>";
	bottom += "		<div class='split-row-compact'></div>";
	bottom += "		<div class='logo-area'>";
	bottom += "			<img class='footer-logo' src='/portal/img/logo-s.png'>";
	bottom += "			<div class='split'></div>";
	bottom += "			<div class='copylight' th:text='#{DIVEU_TXT_0067}'></div>";
	bottom += "		</div>";
	bottom += "		<div>";
	bottom += "			<button>Language</button>";
	bottom += "		</div>";
	bottom += "		<div class='split-row-compact'></div>";
	bottom += "	</div>";
	bottom += "</div>";	
	
	$parent.append(bottom);

	$dataTable.fixedColumns().relayout();
}

/* 
페이지에 dataTable 리사이저를 설치한다. (jquery, throttle 필요)
주의) $parent 는 css 만으로 리사이징 돼야 한다.
*/
function installDataTableResizer($dataTable, $parent, isShowAllWithoutScrollY){
	let prevHeight = 0;
	$(window).resize($.debounce(100, function(e){
		if (isShowAllWithoutScrollY)
		{
			dataTableResizeWithoutScrollY($dataTable, $parent);
		}
		else
		{
			prevHeight = dataTableResize($dataTable, $parent, prevHeight);	
		};
	}));
	// 최초 로딩시 리사이징.
	$(window).resize();
}

/*
DataTable 버그 수정
*/
function installDataTableBugFix(tableId){
	/* FixedColumns 사용중 정렬 버그 해결 : 강제로 scrollTop(0) 이동해서 버그 피하기 */
	$("#"+tableId).on("order.dt", function(){
		$("#"+tableId+"_wrapper .DTFC_LeftBodyLiner").scrollTop(0);
	});
}

/*
dataTable 세팅시 defarLoading 방식으로 ajax 설정하게 해주는 펑션 
ajaxOptions 은 생략 가능
*/
function ajaxDeferFunction(url, paramsFunction, ajaxOptions){
	let defarLoading = true;
	return function(data, callback, settings) {
		if (defarLoading) {
			// 최초에는 empty 메시지를 보이지 않음
			settings.oLanguage.sEmptyTable = "&nbsp;";
			callback({data:[]});
			defarLoading = false;
		} else {
			let options = $.extend({data: paramsFunction()}, ajaxOptions);
			$.ajax(url, options)
			.fail(function(){
				callback({data:[]});
			})
			.done(function(result){
				// 검색후 결과가 없으면 메시지 표시
				settings.oLanguage.sEmptyTable = emptyMessage;
				callback(result);
				settings.oLanguage.sEmptyTable = "&nbsp;";
			});
		}
	};
}

/*
 * dataTables FixedColumns 와 row select 기능을 사용하고, 컬럼을 input이나 select로 할 경우, 값을 동기화시킴.
 */
function syncInput(obj) {
	let td = $tableObj.cell(obj).node();
	if (obj.tagName=='INPUT' && obj.type=='checkbox') {
		$(td).find("input").attr("checked", obj.checked).prop("checked", obj.checked);
	} else if (obj.tagName=='INPUT') {
		$(td).find("input").val(obj.value);
	} else if (obj.tagName=='SELECT') {
		$(td).find("select option[value="+obj.value+"]").attr("selected", true);
	}
}

/*
 * 그리드 헤더 체크박스로 전체 체크 처리  
 */
function allChecked(obj) {
	let checked = obj.checked;
	let tds = $tableObj.columns(0).nodes()[0];   // chk 컬럼은 0번 컬럼으로 가정
	$(tds).find("input").attr("checked", checked).prop("checked", checked);
	$tableObj.draw();
}

/*
 * 그리드 테이블 초기 설정
 */
function initGridTable(table, isShowAllWithoutScrollY) {
	let tableId = table.tables().nodes().to$().attr("id");
	
	// window 리사이즈시 그리드 높이 다시 맞춤. (그리드의 parent 요소의 높이에 맞춤)
	installDataTableResizer(table, $("#"+tableId+"_wrapper").closest(".grid-area"), isShowAllWithoutScrollY);
	// dataTable 버그 수정.
	installDataTableBugFix(tableId);	
}