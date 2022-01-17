package org.jwebppy.platform.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Formatter {
	static final String defaultDateFormat = CmDateFormatUtils.getDateFormat();
	static final String defaultCurrencyFormat = "#,###.##";

	/**
	 * 현재(오늘)날짜 가져오기.<br>
	 * 현재(오늘)날짜를 기본 포맷(yyyy.MM.dd)으로 반환한다.
	 *
	 * @return 현재(오늘)날짜
	 */
	public static String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultDateFormat);
		return simpleDateFormat.format(date);
	}

	/**
	 * 현재(오늘)날짜 가져오기.<br>
	 * 현재(오늘)날짜를 원하는 포맷으로 반환한다.
	 *
	 * @param strFormat 날짜 포맷 - ex) 'yyyyMMddHHmmss', 'yyyyMMdd', 'yyyy-MM-dd',
	 *                  'yyyy/MM/dd'
	 * @return 현재(오늘)날짜
	 */
	public static String getCurrentDate(String strFormat) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
		return simpleDateFormat.format(date);
	}

	/**
	 * offset만큼 후의 날짜 가져오기.<br>
	 * 오늘 날짜 기준으로 offset만큼 후의 날짜를 원하는 포맷으로 반환한다.
	 *
	 * @param iDay      날짜 offset - 0 : 오늘날짜, 7 : 7일후 날짜, -7 : 7일전 날짜
	 * @param strFormat 날짜 포맷 - ex) 'yyyyMMddHHmmss', 'yyyyMMdd', 'yyyy-MM-dd',
	 *                  'yyyy/MM/dd'
	 * @return offset만큼 후의 날짜
	 */
	public static String getDate(int iDay, String strFormat) {
		Date date = new Date();

		int iCalDay = iDay;
		long lCalDate = 0;

		if (iDay >= 0) {
			while (iCalDay > 15) {
				lCalDate = lCalDate + 15 * (1000 * 60 * 60 * 24);
				iCalDay = iCalDay - 15;
			}
			lCalDate = lCalDate + iCalDay * (1000 * 60 * 60 * 24);
		} else {
			while (iCalDay < -15) {
				lCalDate = lCalDate - 15 * (1000 * 60 * 60 * 24);
				iCalDay = iCalDay + 15;
			}
			lCalDate = lCalDate + iCalDay * (1000 * 60 * 60 * 24);
		}
		long lBeforeWeekDate = date.getTime() + lCalDate;
		date.setTime(lBeforeWeekDate);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);

		return simpleDateFormat.format(date);
	}

	/**
	 * 문자열 날짜 변환.<br>
	 * 문자열로 된 날짜(예: 20080310)를 기본 포맷(yyyy.MM.dd)으로 변환한다.
	 *
	 * @param strDate   문자열로 된 날짜
	 * @param strFormat 원하는 날짜 포맷
	 * @return 변환 결과
	 */
	public static String getDefDateFormat(String strDate){
		strDate = deleteZeroDate(strDate);

		if (strDate != null && !"".equals(strDate)) {

			if (strDate.indexOf(".") > -1) {
				return strDate;
			} else if (strDate.indexOf("-") > -1 || strDate.indexOf("/") > -1) { // - 가 있는 경우
				return strDate.replaceAll("-|/", ".");
			} else {
				return getDateFormat(strDate, defaultDateFormat);
			}
		} else {
			return "";
		}

	}

	public static String getDefDateFormat(Object obj)
	{
		if (CmStringUtils.isEmpty(obj))
		{
			return "";
		}

		if (obj instanceof Date)
		{
			return new SimpleDateFormat(defaultDateFormat).format(obj);
		}

		if (obj != null && !"".equals(obj.toString()))
		{
			return getDefDateFormat(obj.toString());
		}
		else
		{
			return "";
		}

	}

	/**
	 * 문자열 날짜 변환.<br>
	 * 문자열로 된 날짜(예: 20080310)를 원하는 포맷(예: yyyy.MM.dd)으로 변환한다.
	 *
	 * @param strDate   문자열로 된 날짜
	 * @param strFormat 원하는 날짜 포맷
	 * @return 변환 결과
	 */
	public static String getDateFormat(String strDate, String strFormat) {
		if (strDate == null || strDate.length() <= 0) {
			return null;
		}

		int iYear = Integer.parseInt(strDate.substring(0, 4));
		int iMonth = Integer.parseInt(strDate.substring(4, 6));
		int iDay = Integer.parseInt(strDate.substring(6));

		Calendar calendar = Calendar.getInstance();
		calendar.set(iYear, iMonth - 1, iDay);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);

		return simpleDateFormat.format(calendar.getTime());
	}

	/**
	 * 날짜 객체 포맷.<br>
	 * 날짜(Date) 객체를 원하는 포맷(예: yyyy.MM.dd)으로 변환한다.
	 *
	 * @param date      날짜 객체
	 * @param strFormat 원하는 날짜 포맷
	 * @return 변환 결과
	 */
	public static String getDateFormat(Date date, String strFormat) {
		if (date == null || date.toString().length() <= 0) {
			return "";
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);

		return simpleDateFormat.format(date);
	}

	/**
	 * 문자열로 된 숫자를 기본 포맷(#,###.##)으로 변환하여 반환한다.
	 *
	 * @param strValue 문자열로 된 숫자
	 * @return 변환 결과
	 */
	public static String getDefDecimalFormat(String strValue) {
		if (strValue != null && !strValue.trim().equals("")) {
			DecimalFormat decimalFormat = new DecimalFormat(defaultCurrencyFormat);
			return decimalFormat.format(Double.parseDouble(strValue));
		} else {
			return "";
		}
	}

	public static String getQtyFormat(String strValue) {
		if (strValue != null && !strValue.trim().equals("")) {
			return Integer.toString(Integer.parseInt(strValue));
		} else {
			return "";
		}
	}

	/**
	 * 문자열로 된 숫자를 기본 포맷(#,###.##)으로 변환하여 반환한다.
	 *
	 * @param obj 문자열로 된 숫자
	 * @return 변환 결과
	 */
	public static String getDefDecimalFormat(Object obj) {
		if (obj != null && !obj.toString().trim().equals("")) {
			if (obj.toString().indexOf(",") > -1) {
				return obj.toString();
			} else {
				DecimalFormat decimalFormat = new DecimalFormat(defaultCurrencyFormat);
				return decimalFormat.format(Double.parseDouble(obj.toString()));

			}
		}
		return "";
	}

	/**
	 * 숫자를 해당하는 포맷으로 변환하여 반환한다.
	 *
	 * @param dValue    변환할 숫자
	 * @param strFormat 숫자 포맷 - ex) '#,###', '#,###.##'
	 * @return 변환 결과
	 */
	public static String getDecimalFormat(BigDecimal dValue, String strFormat) {
		if (dValue != null && strFormat != null) {
			DecimalFormat decimalFormat = new DecimalFormat(strFormat);
			return decimalFormat.format(dValue);
		} else {
			return "";
		}
	}

	/**
	 * 숫자를 해당하는 포맷으로 변환하여 반환한다.
	 *
	 * @param dValue    변환할 숫자
	 * @param strFormat 숫자 포맷 - ex) '#,###', '#,###.##'
	 * @return 변환 결과
	 */
	public static String getDecimalFormat(double dValue, String strFormat) {
		DecimalFormat decimalFormat = new DecimalFormat(strFormat);
		return decimalFormat.format(dValue);
	}

	/**
	 * 문자열로 된 숫자를 해당하는 포맷으로 변환하여 반환한다.
	 *
	 * @param strValue  문자열로 된 숫자
	 * @param strFormat 숫자 포맷 - ex) '#,###', '#,###.##'
	 * @return 변환 결과
	 */
	public static String getDecimalFormat(String strValue, String strFormat) {
		if (strValue != null && !strValue.trim().equals("")) {
			DecimalFormat decimalFormat = new DecimalFormat(strFormat);
			return decimalFormat.format(Double.parseDouble(strValue));

		} else {
			return "";
		}
	}

	public static String getIntegerFormat(Object value) {

		if (value != null)
		{
			DecimalFormat decimalFormat = new DecimalFormat("#");
			decimalFormat.setRoundingMode(RoundingMode.DOWN);

			if (value instanceof BigDecimal)
			{
				return decimalFormat.format(value);
			}
			else
			{
				if (!"".equals(value.toString().trim()))
				{
					return decimalFormat.format(Double.parseDouble((String)value));
				}
			}
		}

		return "";
	}

	public static String changeFormat(String strValue, String strUnit, String strFormat) {
		if (strValue.equals("0") || strValue.equals("")) {
			return "0";
		}

		DecimalFormat decimalFormat = new DecimalFormat(strFormat);
		return decimalFormat.format(Double.parseDouble(strValue.replaceAll(",", "")) / Double.parseDouble(strUnit));
	}

	public static String changeFormat(double dValue, String strUnit, String strFormat) {
		if (dValue == 0) {
			return "0";
		}

		DecimalFormat decimalFormat = new DecimalFormat(strFormat);
		return decimalFormat.format(dValue / Double.parseDouble(strUnit));
	}

	/**
	 * SAP User설정에 따라 number format이 #.###,##로 넘어오는 경우 comma[,]와 소수점 구분자[.]를 변경해준다.
	 */
	public static String changePriceFormt(String strValue, String strFormat) {
		if (strValue.equals("0") || strValue.equals("")) {
			return "0";
		}

		DecimalFormat decimalFormat = new DecimalFormat(strFormat);

		strValue = strValue.replaceAll("[.]", "");
		strValue = strValue.replaceAll("[,]", "[.]");

		return decimalFormat.format(Double.parseDouble(strValue));
	}

	/**
	 * 문자열에서 지정한 문자를 없애주는 함수<br>
	 * Ex) getUnDelim("12,345,678", ',')=12345678 <br>
	 * Ex) getUnDelim("2006.08.09", '.')=20060809
	 *
	 * @param strInput 입력 문자열
	 * @param chrDelim 제거할 문자
	 * @return 결과 문자열
	 */
	public static String getUnDelim(String strInput, char chrDelim) {
		String strOutput = "";
		for (int intCounter = 0; intCounter < strInput.length(); intCounter++) {
			if (strInput.charAt(intCounter) != chrDelim) {
				strOutput += strInput.charAt(intCounter);
			}
		}
		return strOutput;
	}

	/**
	 * 문자열에서 지정한 문자를 없애주는 함수<br>
	 * Ex) getUnDelim("12,345,678", ',')=12345678 <br>
	 * Ex) getUnDelim("2006.08.09", '.')=20060809
	 *
	 * @param strInput 입력 문자열
	 * @param chrDelim 제거할 문자
	 * @return 결과 문자열
	 */
	public static String getUnDelim2(String strInput, String chrDelim) {
		if (strInput != null) {
			return strInput.replaceAll("[" + chrDelim + "]+", "");
		}

		return "";
	}

	public static String getDecimal(String strInput) {
		String strOutput = strInput;
		strOutput = Formatter.getUnDelim(strOutput, ",".charAt(0));
		strOutput = Formatter.getUnDelim(strOutput, "%".charAt(0));

		return strOutput;
	}

	/**
	 * 입력값이 null,'',X 이면 0을 리턴
	 *
	 * @param strInput
	 * @return 0
	 */
	public static String getZero(String strInput) {
		if (strInput == null || strInput.trim().equals("") || strInput.trim().equals("X")) {
			return "0";
		}
		return strInput;
	}

	/**
	 * 입력값이 null,'',X 이면 0을 리턴
	 *
	 * @param strInput
	 * @return 0
	 */
	public static String getZero(Object strInput) {
		if (strInput == null || strInput.toString().trim().equals("")) {
			return "0";
		}
		return strInput.toString();
	}

	/**
	 * 문자열의 전체길이가 length가 될때까지<br>
	 * 앞에 0을 붙여준다.<br>
	 *
	 * @param strInput
	 * @param length
	 * @return 결과 문자열
	 */
	public static String fillZero(String strInput, int length) {
		if (strInput == null || strInput.trim().equals("")) {
			return "";
		}

		String s = strInput;
		int fillLength = length - strInput.length();
		for (int i = 0; i < fillLength; i++) {
			s = "0" + s;
		}

		return s;
	}

	/**
	 * 문자열의 전체길이가 length가 될때까지<br>
	 * 앞에 0을 붙여준다.<br>
	 *
	 * @param strInput
	 * @param length
	 * @return 결과 문자열
	 */
	public static String fillZero(int iInput, int length) {
		String strInput = Integer.toString(iInput);
		String s = strInput;
		int fillLength = length - strInput.length();
		for (int i = 0; i < fillLength; i++) {
			s = "0" + s;
		}

		return s;
	}

	/**
	 * 문자열의 맨 앞이 . 으로 시작되는 경우 앞에 0을 붙여준다
	 *
	 * @param str
	 * @return 결과 문자열
	 */
	public static String fixedPoint(String str) {
		if (str != null && !"".equals(str)) {
			if (str.substring(0, 1).equals(".")) {
				return "0" + str;
			} else {
				return str;
			}
		}
		return "";
	}

	/**
	 * 문자열의 맨 앞이 . 으로 시작되는 경우 앞에 0을 붙여준다
	 *
	 * @param str
	 * @return 결과 문자열
	 */
	public static String fixedPoint(Object obj) {
		if (obj != null && !"".equals(obj.toString())) {
			if (obj.toString().substring(0, 1).equals(".")) {
				return "0" + obj.toString();
			} else {
				return obj.toString();
			}
		}
		return "";
	}

	/**
	 * 문자열 앞에 있는 0을 모두 삭제한다.
	 *
	 * @param strInput
	 * @param length
	 * @return 결과 문자열
	 */
	public static String deleteZero(String strInput, int length) {
		if (strInput == null || strInput.equals("")) {
			return "";
		} else {
			return strInput.replaceAll("0{" + length + "}", "");
		}
	}

	/**
	 * 문자열 앞에 있는 0을 모두 삭제한다.
	 *
	 * @param strInput
	 * @return 결과 문자열
	 */
	public static String deleteFrontZero(String strInput) {
		if (strInput == null || strInput.equals("")) {
			return "";
		} else {
			try {
				return Integer.parseInt(strInput) + "";

			} catch (Exception e) {
				return strInput;
			}

		}
	}

	/**
	 * 문자열 앞에 있는 0을 모두 삭제한다.
	 *
	 * @param objInput
	 * @return 결과 문자열
	 */
	public static String deleteFrontZero(Object objInput) {
		if (objInput == null || objInput.equals("")) {
			return "";
		} else {
			try {
				return Integer.parseInt(objInput.toString()) + "";

			} catch (Exception e) {
				return objInput.toString();
			}

		}
	}

	/**
	 * 특수문자(-_:.등을 제외한 날짜가 0인경우 빈값을 리턴
	 *
	 * @param strDate
	 * @return 결과 문자열
	 */
	public static String deleteZeroDate(String strDate) {
		if (strDate == null || strDate.equals("")) {
			return "";
		} else {
			try {
				String str = strDate.replaceAll("-|_|:|\\.", "");
				int date = Integer.parseInt(str);
				if (date == 0) {
					return "";
				} else {
					return strDate;
				}

			} catch (Exception e) {
				return "";
			}
		}
	}

	public static String makeFormat(String f1, String f2) {
		if (f2.length() == 1) {
			return "#,###";
		}
		String fmt = f1;
		for (int i = 0; i < f2.length() - 1; i++) {
			fmt = fmt + "#";
		}

		return fmt;
	}

	/**
	 * YYYYMMDD형식의 문자열을 Calendar형식으로 변환
	 *
	 * @param strYYYYMMDD YYYYMMDD형식의 문자열
	 * @return Calendar 객체
	 */
	public static Calendar getCalender(String strYYYYMMDD) {
		Calendar calendar = Calendar.getInstance();
		String strYYYY = strYYYYMMDD.substring(0, 4);
		String strMM = strYYYYMMDD.substring(4, 6);
		String strDD = strYYYYMMDD.substring(6, 8);
		int intYYYY = Integer.parseInt(strYYYY);
		int intMM = Integer.parseInt(strMM);
		int intDD = Integer.parseInt(strDD);
		int intHH = 0;
		int intMI = 0;
		int intSS = 0;
		if (strYYYYMMDD.length() >= 10) {
			intHH = Integer.parseInt(strYYYYMMDD.substring(8, 10));
		}
		if (strYYYYMMDD.length() >= 12) {
			intMI = Integer.parseInt(strYYYYMMDD.substring(10, 12));
		}
		if (strYYYYMMDD.length() >= 14) {
			intSS = Integer.parseInt(strYYYYMMDD.substring(12, 14));
		}
		calendar.set(intYYYY, intMM - 1, intDD, intHH, intMI, intSS);
		return calendar;
	}

	/**
	 * 문자열이 지정한 길이를 넘어가면 ...을 붙여준다.
	 *
	 * @param str
	 * @param len
	 * @return
	 */
	public static String ellipsis(String str, int len) {
		if (str != null && str.length() > len) {
			return str.substring(0, len) + "...";
		}

		return str;
	}

	/**
	 * 문자열이 지정한 길이를 넘어가면 ...을 붙여준다.
	 *
	 * @param str
	 * @param len
	 * @return
	 */
	public static String ellipsis(Object str, int len) {
		if (str != null && str.toString().length() > len) {
			return str.toString().substring(0, len) + "...";
		}

		return str.toString();
	}

	public static double getUnFormattedValue(String str) {
		if (str != null) {
			return Double.parseDouble(str.replaceAll("[[^0-9]&&[^.]]+", ""));
		}

		return 0;
	}

	public static String getUnformattedDate(String str) {
		if (str != null) {
			return str.replaceAll("\\D", "");
		}
		return "";

	}
}
