package org.jwebppy.platform.core.util;

import net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator;
import net.sf.log4jdbc.sql.Spy;

public class MyJDBCFormater extends Slf4jSpyLogDelegator
{
	@Override
	public void sqlOccurred(Spy spy, String methodCall, String sql)
	{
		// 두산 CallStack을 구해서 같이 표시
		String callstack = MyLocalLogger.getDoosanCallStack();

		if (callstack != null)
		{
			super.sqlOccurred(spy, methodCall, "\nSQL Call : " + CmStringUtils.defaultString(UserAuthenticationUtils.getUsername()) + "\n" + callstack + "\n		" + sql);
		}
	}
}
