package org.jwebppy.portal.iv.eu.parts.merchandize;

import java.io.File;

import org.jwebppy.platform.core.PlatformCommonVo;

public class EuMerchandizeCommonVo extends PlatformCommonVo
{
	public static final String CATEGORY_NAME = "CATEGORY_NAME";	// 다국어 테이블에서 사용하는 상품 관련 구분:카테고리명
	public static final String PRODUCT_NAME = "PRODUCT_NAME";	// 다국어 테이블에서 사용하는 상품 관련 구분:상품명
	public static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";		// 다국어 테이블에서 사용하는 상품 관련 구분:상품설명

	public static final String INSERT = "INSERT";	// 저장 시 삽입 또는 갱신 판별용
	public static final String UPDATE = "UPDATE";	// 저장 시 삽입 또는 갱신 판별용

	public static final String FILE_TARGET_MALL	= "MALL";	// 파일 업로드 테이블에서 사용할 대상 구분자(게시판=BBS, 머천다이즈:MALL 등)
	public static final String FILE_TYPE_MAIN = "MAIN";		// 파일 테이블에서 어떤 용도의 파일인지 구분하는 구분자(메인 이미지)
	public static final String FILE_TYPE_THUMBNAIL = "THUMBNAIL";		// 파일 테이블에서 어떤 용도의 파일인지 구분하는 구분자(상품 이미지 썸네일)
	public static final String FILE_TYPE_IMAGE = "IMAGE";		// 파일 테이블에서 어떤 용도의 파일인지 구분하는 구분자(상품 이미지)

	public static final String FILECHECK_MALL_MAIN_ID = "MALL_MAIN";	// 파일 Validation 검사할 때 머천다이즈 메인 화면에서 호출되었음을 알리는 ID(EP_FILE_UPLOAD_INFO테이블에서 사용)
	public static final String FILECHECK_MALL_PRODUCT_ID = "MALL_PRODUCT";

	public static final int FILE_SIZE_MALL_THUMBNAIL_W = 100;	// 상품 썸네일 이미지 가로 사이즈
	public static final int FILE_SIZE_MALL_THUMBNAIL_H = 100;	// 상품 썸네일 이미지 세로 사이즈
	
	public static final double round_digit_2 = 100.0;	// Math.round로 소수점 두번째 자릿수 계산 시 사용
	
	public static final int PRODUCT_IMAGE_PREVIEW_LIMIT = 5; 	// 상품 이미지 프리뷰 갯수
	public static final int REPRODUCT_MALL_ITEM_LIMIT = 12;		// 추천상품 갯수
	
	
	public static final String CURRENCY_KRW	= "KRW";	// 통화단위(한국 원)
	
	public static final String SAP_FIELD_MATNR	= "MATNR";	// SAP필드(상품코드)
	public static final String SAP_FIELD_KBETR	= "KBETR";	// SAP필드(신규가격)
	public static final String SAP_FIELD_KONWA	= "KONWA";	// SAP필드(통화단위)
	
	public static final String MERCHANDIZE_USER_PATH = "/portal/corp/eu/scm/parts/merchandize/main/main";	// 머천다이즈 기본화면 path(관리자일 경우 _admin을 붙임)
	
	public static final String MERCHANDIZE_FILE_PATH = "mall" + File.separator;	// 클라우드 파일 스토리지에 머천다이즈 관련 파일이 적재될 디렉토리명

}
