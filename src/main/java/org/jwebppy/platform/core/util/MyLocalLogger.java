package org.jwebppy.platform.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.jwebppy.platform.core.dao.DataType;
import org.jwebppy.platform.core.dao.MapParameterSource;
import org.jwebppy.platform.core.dao.ParameterValue;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

/** 로컬용 디버깅 로그 출력 모음 */
@Component
public class MyLocalLogger {

	private static Environment env;

	@Autowired
	@SuppressWarnings("static-access")
	public void setEnv(Environment env) {
		this.env = env;
	}

	// 로그 출력으로 인해 성능감소가 예상 되는 경우, 프로세스 자체를 막기 위해 아래의 옵션들을 사용.
	// 일부 로그의 실행을 부분적으로 제외 할때 false 대입.
	private static final boolean URL_REQUEST_LOG = true;
	private static final boolean CALL_STACK_LOG = true;
	private static final boolean POST_PARAMETER_LOG = true;
	private static final boolean RFC_REQUEST_LOG = true;
	private static final boolean RFC_PARAMETER_LOG = true;
	private static final boolean RFC_RESULT_LOG = true;

	// TABLE의 각 컬럼을 한줄에 몇개 까지 보여줄지...
	private static final int RFC_TABLE_NEWLINE = 6;

	// TABLE을 몇 줄까지 보여줄지...
	private static final int RFC_TABLE_LINE_LIMIT = 5;

	// 어느 서버까지 로그 출력을 허용할지 LOG_LEVEL에 대입하여 선택.
	// 해당되지 않는 서버는 로그 프로세스자체가 실행 안되서 성능 하락 없음.
	private static int LOCAL = 0;
	private static int DEV = 1;
	private static int PROD = 2;
	private static int profileNo = -1;
	private static int LOG_LEVEL = LOCAL;//local 에서만 로그 출력되도록 설정

	// RFC request와 result를 짝짖기 위한 시퀀스 번호.
	private static int RFC_SEQ = 91000;

	public static int getProfile() {
		if (profileNo <0) {
			profileNo = LOCAL;
			String[] profiles = env.getActiveProfiles();
			if ("prod".equals(profiles[0])) profileNo = PROD;
			if ("dev".equals(profiles[0])) profileNo = DEV;
		}
		return profileNo;
	}

	/** URL 요청 로그 쌓기. 리소스 파일은 제외 */
	public static void urlRequestLog(ServletRequest request) {
		if (getProfile() > LOG_LEVEL) return;
		if (!URL_REQUEST_LOG) return;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Logger logger = LoggerFactory.getLogger("URL");
		try {
			String url = httpRequest.getRequestURL().toString();
			if (!url.equals("http://doobizplus.doosan-iv.com/") && !url.equals("http://doobizplus-dev.doosan-iv.com/")) {
				if (!url.matches("(?i).*(\\.jpg|\\.jpeg|\\.png|\\.gif|\\.css|\\.js|\\.ico)$")) {
					String query = httpRequest.getQueryString();
					if (query != null) url += "?" + query;
					// TODO filter 에서 호출하면 username을 가져오지 못함. 개선 필요.
					// String username =
					// CmStringUtils.defaultString(UserAuthenticationUtils.getUsername());
					// logger.info(username + " : " + url);

					logger.info(url + postParameterLog(httpRequest));
				}
			}
		} catch (Exception e) {
			logger.error("MyLocalLogger error", e);
		}
	}

	/** POST 요청시 파라미터 로그 텍스트 만들기 */
	public static String postParameterLog(HttpServletRequest httpRequest) {
		if (POST_PARAMETER_LOG && "POST".equals(httpRequest.getMethod())) {
			ArrayList<String> params = new ArrayList<String>();
			Enumeration<String> names = httpRequest.getParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String[] values = httpRequest.getParameterValues(name).clone();
				for (int i=0; i<values.length; i++){
					if (values[i].length() > 100) {
						values[i] = values[i].substring(0, 100);
					}
					//if (name.equals("password")) values[i]="******";
				}
				String param = "";
				param += name + ":";
				if (values.length > 1) {
					param += "[" + String.join(",", values) + "]";
				} else {
					param += values[0];
				}
				params.add(param);
			}
			if (params.size() > 0) {
				return "		{ " + String.join(", ", params) + " }";
			}
		}
		return "";
	}

	private static String getUsername(){
		return CmStringUtils.defaultString(UserAuthenticationUtils.getUsername());
	}

	/** RFC 요청 로그 쌓기. */
	public static int rfcRequestLog(RfcRequest rfcRequest) {
		if (getProfile() > LOG_LEVEL) return 0;
		if (!RFC_REQUEST_LOG) return 0;
		Logger logger = LoggerFactory.getLogger("RFC request");
		try {
			String callstack = getDoosanCallStack();
			if (callstack != null) {
				String log = "";
				//String landscape = CmStringUtils.defaultString(rfcRequest.getLandscape(), "P09"); // application.properties: sap.landscape.default
				//log += "\n" + "\t("+RFC_SEQ+") Function: " + "[" + landscape + "] " + rfcRequest.getFunctionName();
				log += "\n" + "\t("+RFC_SEQ+") Function: " + rfcRequest.getFunctionName();
				if (RFC_PARAMETER_LOG) {
					log += "\n" + "\tParameters: " + getRfcParameterLog("\t", rfcRequest);
				}
				logger.info("\nRFC Call : " + getUsername() + " \n" + callstack + log);
			}
		} catch (Exception e) {
			logger.error("MyLocalLogger error", e);
		}
		return RFC_SEQ++;
	}

	/** RFC 요청 파라미터 텍스트 만들기. */
	public static String getRfcParameterLog(String indent, RfcRequest rfcRequest) {
		Map<String, ParameterValue> dataMap = rfcRequest.getParameterSource().getValues();
		String text = "{\n";
		if (MapUtils.isNotEmpty(dataMap)) {

			for (String name : dataMap.keySet()) {
				ParameterValue params = dataMap.get(name);

				switch (params.getType()) {
				case DataType.STRUCTURE:
					Map<String, ParameterValue> structure = ((MapParameterSource) params.getValue()).getValues();
					text += indent + "\t" + name + " :{\n";
					for (String key : structure.keySet()) {
						text += indent + "\t\t" + key + " :" + CmStringUtils.defaultString(structure.get(key).getValue()) + "\n";
					}
					text += indent + "\t" + "}\n";
					break;

				case DataType.TABLE:
					Map<String, ParameterValue> table = ((MapParameterSource) params.getValue()).getValues();;
					text += indent + "\t" + name + " :[\n";
					for (String linekey : table.keySet()) {
						Map<String, Object> line = (Map<String, Object>) table.get(linekey).getValue();
						text += indent + "\t\t" + linekey + " :{";
						ArrayList<String> sl = new ArrayList<String>();
						for (String columnkey : line.keySet()) {
							sl.add(columnkey + ":" + CmStringUtils.defaultString(line.get(columnkey)));
						}
						text += sortedJoinColumns(",  ", sl, ",\n" + indent + "\t\t\t") + "}\n";
					}
					text += indent + "\t" + "]\n";
					break;

				default:
					text += indent + "\t" + name + " :" + params.getValue() + "\n";
					break;
				}
			}
			text += indent + "}";
			return text;
		}
		return "";
	}

	/** RFC 결과값 텍스트 로그 쌓기 */
	public static void rfcResultLog(int rfc_seq, StopWatch stopWatch, JCoFunction jcoFunction) {
		if (getProfile() > LOG_LEVEL) return;
		if (!RFC_RESULT_LOG) return;
		Logger logger = LoggerFactory.getLogger("RFC result");
		try {
			String text = "\t("+rfc_seq+") "+getUsername()+" : " + jcoFunction.getName() + ", Elapsed Time :" + stopWatch.getTime() + "ms\n";
			text += "\tExport :" + getRfcResultParameterLog("\t", jcoFunction.getExportParameterList()) + "\n";
			// text += "\tChanging :" + rfcResultParaameterLog("\t",
			// jcoFunction.getChangingParameterList()) + "\n";
			text += "\tTable :" + getRfcResultParameterLog("\t", jcoFunction.getTableParameterList());
			logger.info("\n" + text);
		} catch (Exception e) {
			logger.error("MyLocalLogger error", e);
		}
	}

	public static String getRfcResultParameterLog(String indent, JCoParameterList params) {
		String text = "";
		if (params != null) {
			text = "{\n";
			for (JCoField field : params) {
				if (field.isStructure()) {
					JCoStructure structure = field.getStructure();
					text += indent + "\t" + field.getName() + " :{\n";
					ArrayList<String> sl = new ArrayList<String>();
					for (JCoField sf : structure) {
						sl.add(indent + "\t\t" + sf.getName() + " :" + safeToString(sf.getValue()) + "\n");
					}
					text += sortedJoin("", sl);
					text += indent + "\t}\n";
				} else if (field.isTable()) {
					JCoTable table = field.getTable();
					text += indent + "\t" + field.getName() + " :[";
					if (table.getNumRows() > 0) {
						text += "\n";
						for (int r = 0; r < table.getNumRows(); r++) {
							// 테이블 결과를 최대 RFC_TABLE_LINE_LIMIT 줄만 보여줌.
							if (r >= RFC_TABLE_LINE_LIMIT) {
								text += indent + "\t\t" + r + " :.............. (" + table.getNumRows() + " rows)\n";
								break;
							}
							text += indent + "\t\t" + r + " :{\t";
							table.setRow(r);
							ArrayList<String> tl = new ArrayList<String>();
							for (JCoField tf : table) {
								tl.add(tf.getName() + ":" + safeToString(tf.getValue()));
							}
							text += sortedJoinColumns(",  ", tl, ",\n" + indent + "\t\t\t") + " }\n";
						}
						text += indent + "\t";
					}
					text += "]\n";
				} else {
					text += indent + "\t" + field.getName() + " :" + safeToString(field.getValue()) + "\n";
				}
			}
			text += "\t}";
		} else {
			text = "{}";
		}
		return text;
	}

	private static String safeToString(Object obj) {
		if (obj == null) return "";
		if (obj instanceof Date){
			SimpleDateFormat sf = new SimpleDateFormat("<yyyy-MM-dd HH:mm:ss>");
			return sf.format(obj);
		}
		return obj.toString();
	}

	private static String sortedJoin(String delimiter, ArrayList<String> list) {
		list.sort((a, b) -> a.compareTo(b));
		return String.join(delimiter, list);
	}

	private static String sortedJoinColumns(String delimiter, ArrayList<String> list, String indent) {
		list.sort((a, b) -> a.compareTo(b));
		String text = "";
		// RFC_TABLE_NEWLINE 씩 끊어서 join
		for (int i = 0; i < list.size(); i += RFC_TABLE_NEWLINE) {
			List<String> sublist = list.subList(i, Math.min(i + RFC_TABLE_NEWLINE, list.size()));
			text += String.join(delimiter, sublist);
			if (list.size() > i + RFC_TABLE_NEWLINE) {
				text += indent;
			}
		}
		return text;
	}

	/**
	 * 두산 코드만 CallStack 구함. 완전히 제외할 콜은 null 반환. (공백은 유효한 콜)
	 */
	public static String getDoosanCallStack() {
		if (!CALL_STACK_LOG) return "";
		// 실제 SQL을 호출하는 클래스&메소드를 구해서 출력
		StringBuilder callstack = new StringBuilder();
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();

		for (int i=0, length=trace.length; i<length; i++)
		{
			StackTraceElement ti = trace[i];
			String className = ti.getClassName();
			String methodName = "." + ti.getMethodName() + "(";

			if (CmStringUtils.equalsAny(className
					,"org.jwebppy.platform.mgmt.logging.service.DataAccessLogService"//로그 실횅 처리 호출
					,"org.jwebppy.platform.mgmt.logging.service.DataAccessResultLogService"//로그 결과 처리 호출;
					,"org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource"//다국어
					,"org.jwebppy.platform.core.security.authentication.service.LoginHistoryService"//로그인 이력
					,"org.jwebppy.config.AsyncConfig$HandlingExecutor$2"//비동기 제외. (현재 로그출력에만 사용됨.)
					)
					|| (className.equals("org.jwebppy.platform.mgmt.user.service.UserGroupService") && (methodName.equals(".getUserGroups(")))
					|| (className.equals("org.jwebppy.platform.mgmt.user.service.UserService") && (methodName.equals(".getUser(") || methodName.equals(".getPageableUsers(")))//사용자 정보 조회
					|| (className.equals("org.jwebppy.platform.mgmt.content.service.ContentAuthorityService") && methodName.equals(".getMyCitems("))//권한 조회
					|| (className.equals("org.jwebppy.platform.mgmt.content.service.ContentService") && methodName.equals(".getCitemsFormTree("))//권한 조회
					|| (className.equals("org.jwebppy.platform.mgmt.authority.service.AuthorityService") && methodName.equals(".getSubRoles("))//권한 조회
					)
			{
				return null;
			}

			// 두산에서 만든 소스만 표시
			if (className.startsWith("org.jwebppy.")
					&& !className.equals("org.jwebppy.platform.core.util.MyLocalLogger")
					&& !className.equals("org.jwebppy.platform.core.util.MyJDBCFormater")
					&& !className.equals("org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate")
					&& !className.equals("org.jwebppy.platform.core.dao.sap.RfcTemplate")
					&& !className.startsWith("org.jwebppy.platform.core.interceptor")
					&& !className.startsWith("org.jwebppy.platform.core.filter.PlatformRequestFilter")
					&& className.indexOf("$$FastClassBySpringCGLIB$$") == -1
					&& className.indexOf("$$EnhancerBySpringCGLIB$$") == -1
					)
			{
				callstack.append("   ").append(ti.toString()).append("\n");
			}
		}
		return callstack.toString();
	}
}
