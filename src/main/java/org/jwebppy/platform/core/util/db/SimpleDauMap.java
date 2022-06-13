package org.jwebppy.platform.core.util.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class SimpleDauMap extends DauGeneralMap
{
	private static final long serialVersionUID = 1L;

	private static final String PREFIX_QUERY_REPLACE_REGEX = "(?i)(:+|<(\\s+|)/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/|%3C|%3C/)\\s*";
	private static final String SUFIX_QUERY_REPLACE_REGEX = "(\\s|\\W)+";

	private static Properties prop = new Properties();
	private String connMethod;
	private String jndiName;
	private String driver;
	private String target;
	private Connection conn;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private boolean isDebug;
	private String debugMode;
	private String debugDisplayTarget;
	private StringBuffer sql = new StringBuffer();
	private List removeAsteriskTarget = new ArrayList();

	public SimpleDauMap()
	{
		initEnv();
	}

	public SimpleDauMap(Map map)
	{
		if (map != null)
		{
			this.putAll(map);
		}
	}

	public SimpleDauMap(String target)
	{
		this.target = target;
		initEnv();
	}

    public SimpleDauMap(String target, Map map)
    {
        this.target = target;
        initEnv();
        if (map != null)
        {
        	this.putAll(map);
        }
    }

	protected void initEnv()
	{
		initProp();

        this.connMethod = prop.getProperty("CONNECTION_METHOD", "M");//S:DataSource,M:DriverManager
        this.jndiName = prop.getProperty("JNDI_NAME");
        this.driver = prop.getProperty("DRIVER");
        this.isDebug = new Boolean(prop.getProperty("DEBUG", "false")).booleanValue();
        this.debugMode = prop.getProperty("DEBUG_MODE", "D");
        this.debugDisplayTarget = prop.getProperty("DEBUG_DISPLAY_TARGET", "C");
	}

	protected void initProp()
	{
		StringBuffer content = new StringBuffer();
		content.append(" ###### Properties load");

		Resource resource = new ClassPathResource("/config/db/DBInfo_" + this.target + ".properties");

		if (resource.exists())
		{
			try
			{
				prop = PropertiesLoaderUtils.loadProperties(resource);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		Iterator it = prop.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry et = (Entry)it.next();
			content.append(" ").append(et.getKey()).append(" = ").append(et.getValue());
		}

		printLog(content.toString());
	}

	protected Connection getConnection()
	{
	    if ("S".equals(connMethod))
	    {
	        return getConnectionByDataSource();
	    }
	    else
	    {
	        return getConnectionByDriverManager();
	    }
	}

	private Connection getConnectionByDriverManager()
	{
		if (this.conn != null)
		{
			return conn;
		}

		try
		{
			Class.forName(driver);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			conn = DriverManager.getConnection(prop.getProperty(target + "_URL"), prop.getProperty(target + "_USERID"), prop.getProperty(target + "_PWD"));
			conn.setAutoCommit(false);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return conn;
	}

	private Connection getConnectionByDataSource()
    {
        if (this.conn != null)
        {
            return conn;
        }

        try
        {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource)initContext.lookup(jndiName);

            conn = ds.getConnection();
            conn.setAutoCommit(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return conn;
    }

	private PreparedStatement setPstmtParam(Connection con, String sql, List paramList) throws SQLException
	{
		PreparedStatement pstmt = null;

		pstmt = con.prepareStatement(sql);

		for (int i=0,size=paramList.size(); i<size; i++)
		{
			pstmt.setObject(i+1, paramList.get(i));
		}

		return pstmt;
	}

	private List getResult(ResultSet rs) throws SQLException
	{
		List rowList = new ArrayList();
		Map rowDataMap = null;

		ResultSetMetaData rsmd = rs.getMetaData();
		int colCnt = rsmd.getColumnCount();
		String colName = null;

		while (rs.next())
		{
			rowDataMap = new LinkedHashMap();

			for (int i=1; i<=colCnt; i++)
			{
				colName = rsmd.getColumnName(i);

				if (rsmd.getColumnType(i) == Types.CLOB)
				{
					rowDataMap.put(colName, rs.getString(colName));
				}
				else
				{
					rowDataMap.put(colName, rs.getObject(colName));
				}
			}

			rowList.add(rowDataMap);
		}

		return rowList;
	}

	public SimpleDauMap query(Object obj)
	{
	    sql.append(obj.toString()).append("\n");

	    return this;
	}

	public void clearQuery()
	{
	    sql = new StringBuffer();
	}

	public List executeQuery() throws SQLException
	{
		return executeQuery(sql, makeParamList(sql.toString()));
	}

	public List executeQueryWithPaging() throws Exception
	{
	    return executeQueryWithPaging(null);
	}

	public List executeQueryWithPaging(Object obj) throws Exception
	{
		StringBuffer sqlForPaging = new StringBuffer();

		sqlForPaging.append("select\n");
		sqlForPaging.append("z0003.tot_cnt-z0003.rnum+1 no,\n");
		sqlForPaging.append("z0003.*\n");

		if (obj != null)
		{
		    String addSql = obj.toString();

		    if (!"".equals(addSql))
		    {
		    	sqlForPaging.append(addSql).append("\n");
		    }
		}

		sqlForPaging.append("from\n");
		sqlForPaging.append("(\n");
		sqlForPaging.append("    select\n");
		sqlForPaging.append("    ceil(z0002.tot_cnt/:listCount) tot_page_num,\n");
		sqlForPaging.append("    z0002.*\n");
		sqlForPaging.append("    from\n");
		sqlForPaging.append("    (\n");
		sqlForPaging.append("        select\n");
		sqlForPaging.append("        rownum rnum,\n");
		sqlForPaging.append("        z0001.*\n");
		sqlForPaging.append("        from\n");
		sqlForPaging.append("        (\n");
		sqlForPaging.append("            select\n");
		sqlForPaging.append("            count(rownum) over() tot_cnt,\n");
		sqlForPaging.append("            z0000.*\n");
		sqlForPaging.append("            from\n");
		sqlForPaging.append("            (\n");
		sqlForPaging.append(sql.toString());
		sqlForPaging.append("            ) z0000\n");
		sqlForPaging.append("        ) z0001\n");
		sqlForPaging.append("        where\n");
		sqlForPaging.append("        rownum<:pageNum*:listCount+1\n");
		sqlForPaging.append("    ) z0002\n");
		sqlForPaging.append("    where\n");
		sqlForPaging.append("    rnum>(:pageNum-1)*:listCount\n");
		sqlForPaging.append(") z0003\n");

		return executeQuery(sqlForPaging, makeParamList(sqlForPaging.toString()));
	}

	public Map executeQueryForMap() throws Exception
	{
		List list = executeQuery(sql, makeParamList(sql.toString()));

		if (!list.isEmpty())
		{
			return (HashMap)list.get(0);
		}

		return null;
	}

	public int executeQueryForInt() throws Exception
	{
		int result = 0;

		try
		{
			result = Integer.parseInt(executeQueryForObject(sql).toString());
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}

		return result;
	}

    public String executeQueryForString() throws Exception
    {
        try
        {
            Object obj = executeQueryForObject(sql);

            if (obj == null)
            {
                return "";
            }
            else
            {
                return obj.toString();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

	protected Object executeQueryForObject(Object sql) throws Exception
	{
		List list = executeQuery(sql, makeParamList(sql.toString()));

		if (!list.isEmpty() && list.size() == 1)
		{
			Map rowMap = (HashMap)list.get(0);

			if (rowMap != null && !rowMap.isEmpty())
			{
				return ((Entry)(rowMap.entrySet().iterator()).next()).getValue();
			}
		}

		return null;
	}

	public int executeUpdate() throws Exception
	{
		return executeUpdate(sql, makeParamList(sql.toString()));
	}

	protected List executeQuery(Object sql, List paramList) throws SQLException
	{
		List list = new ArrayList();
		long elapsedTime = 0;
		String errorMsg = null;

		try
		{
			conn = getConnection();
			pstmt = setPstmtParam(conn, sql.toString(), paramList);

			long startTime = System.currentTimeMillis();
			rs = pstmt.executeQuery();
			elapsedTime = System.currentTimeMillis() - startTime;

			list = getResult(rs);

			closeOthers();
		}
		catch (SQLException e)
		{
			closeWithError();
			e.printStackTrace();
			errorMsg = getStackTrace(e);

			throw e;
		}
		finally
		{
			debug(sql.toString(), elapsedTime, errorMsg);
		}

		return list;
	}

	protected int executeUpdate(Object sql, List paramList) throws SQLException
	{
		int result = -1;
		long elapsedTime = 0;
		String errorMsg = null;

		try
		{
			conn = getConnection();
			pstmt = setPstmtParam(conn, sql.toString(), paramList);

			long startTime = System.currentTimeMillis();
			result = pstmt.executeUpdate();
			elapsedTime = startTime - System.currentTimeMillis();

			closeOthers();
		}
		catch (SQLException e)
		{
			closeWithError();
			e.printStackTrace();
			errorMsg = getStackTrace(e);

			throw e;
		}
		finally
		{
			debug(sql.toString(), elapsedTime, errorMsg);
		}

		return result;
	}

	public void addRemoveAsteriskTarget(String colName)
	{
	    removeAsteriskTarget.add(colName);
	}

	private List makeParamList(String sql)
	{
		List paramList = new ArrayList();

		try
		{
			Pattern p = null;
			Matcher m = null;
			String key = null;
			Object value = null;
			Map paramTreeMap = new TreeMap();

			Iterator it = (this.keySet()).iterator();

			while (it.hasNext())
			{
				key = (String)it.next();
				value = this.get(key);

				if (removeAsteriskTarget.contains(key))
				{
                    if (value instanceof String || value instanceof StringBuffer)
                    {
                        if (value != null)
                        {
                            String tmp = getString(key);

                            if (tmp.startsWith("*"))
                            {
                                tmp = tmp.substring(1);
                            }

                            if (tmp.endsWith("*"))
                            {
                                tmp = tmp.substring(0, tmp.length()-1);
                            }

                            value = tmp;
                        }
                    }
				}

				p = Pattern.compile(PREFIX_QUERY_REPLACE_REGEX + key + SUFIX_QUERY_REPLACE_REGEX);
				m = p.matcher(sql);

				while (m.find())
				{
                    paramTreeMap.put(lpad(new Integer(m.start()), "0", 10), value);
				}
			}

			it = (paramTreeMap.keySet()).iterator();
			while (it.hasNext())
			{
				paramList.add(paramTreeMap.get(it.next()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return paramList;
	}

	public String lpad(Object obj, String s, int size)
    {
    	String tmp = obj.toString();

    	if (tmp == null)
    	{
    		return "";
    	}

    	StringBuffer sb = new StringBuffer();
    	int len = tmp.length();

    	for (int i=size-len; i>0; i--)
    	{
    		sb.append(s);
    	}
    	sb.append(tmp);

		return sb.toString();
    }

    // trace 내용 반환
    protected String getStackTrace(Exception e)
    {
        StackTraceElement[] ste = e.getStackTrace();

        StringBuffer content = new StringBuffer();
        content.append(e.toString()).append("\n");

        if (ste != null)
        {
            for (int i=0,len=ste.length; i<len; i++)
            {
                content.append(ste[i].toString()).append("\n");
            }
        }

        return content.toString();
    }

    protected void debug(String sql, long elapsedTime, String errorMsg)
	{
		DecimalFormat df = new DecimalFormat("0.##");
		StringBuffer content = new StringBuffer();

		content.append("\n###### Thread Name : ").append(Thread.currentThread().getName());
		content.append("\n###### Query\n");
		content.append(sql);
		content.append("\n###### Parameter");

		Iterator it = this.entrySet().iterator();
		Entry et = null;
		while (it.hasNext())
		{
		    et = (Entry)it.next();
		    content.append("\n").append(et.getKey()).append(" : ").append(et.getValue());
		}

		content.append("\n###### Elapsed Time : ").append(df.format(elapsedTime*0.001)).append("s");

		if (errorMsg != null && !"".equals(errorMsg))
		{
			content.append("\n###### Error Message");
			content.append(errorMsg);
		}

		printLog(content.toString());
	}

    protected void printLog(String content)
	{
		if (isDebug)
		{
			if ("C".equals(debugDisplayTarget))
			{
				StringBuffer frame = new StringBuffer();
				frame.append("\n=========================================================================================================");
				frame.append("\n###### Log Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				frame.append(content);
				frame.append("\n=========================================================================================================");

				if ("E".equals(debugMode))
				{
					System.err.println(frame.toString());
				}
				else
				{
					System.out.println(frame.toString());
				}
			}
		}
	}

	public void close()
	{
		try { if (conn != null) { conn.commit(); } } catch (Exception ee) {}
		closeOthers();
		closeConn();
	}

	private void closeWithError()
	{
		try { if (conn != null) { conn.rollback(); } } catch (Exception ee) {}
		closeOthers();
		closeConn();
	}

	private void closeOthers()
	{
		try { if (rs != null) { rs.close(); } } catch (Exception e) {} rs = null;
		try { if (pstmt != null) { pstmt.close(); } } catch (Exception e) {} pstmt = null;
	}

	private void closeConn()
	{
		try { if (conn != null) { conn.close(); } } catch (Exception e) {} conn = null;
	}
}
