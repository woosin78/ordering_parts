package org.jwebppy.platform.core;

public class PlatformCommonVo
{
	//기본 날짜 형식
	public static final String DEFAULT_COUNTRY = "KR";
	public static final String DEFAULT_TIMEZONE = "Asia/Seoul";
	public static final String DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DEFAULT_DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DEFAULT_TIME_FORMAT_HHMMSS = "HHmmss";
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	public static final String DEFAULT_TIME_FORMAT_MMHH = "HH:mm";
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String UNLIMITED_DATE_TIME = "9999-12-31 23:59:59";

	//통화 형식
	public static final String DEFAULT_CURRENCY_FORMAT = "#,###.00";

	//상태 표시
	public static final String ALL = "A"; //전체
	public static final String YES = "Y"; //예
	public static final String NO = "N"; //아니오

	//처리 결과 상태 표시
	public static final String SUCCESS = "S"; //성공
	public static final String FAIL = "F"; //실패

	//페이징 - 한 화면에 보여 쥴 게시물 수
	public static final int DEFAULT_ROW_PER_PAGE = 20;
}
