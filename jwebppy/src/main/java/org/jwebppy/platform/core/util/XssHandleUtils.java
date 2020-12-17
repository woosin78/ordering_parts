package org.jwebppy.platform.core.util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class XssHandleUtils
{
	private static Set<String> TAGS = new LinkedHashSet<>();
	private static Set<String> EVENTS = new HashSet<>();

	static
	{
		TAGS.add("<body");
		TAGS.add("<embed");
		TAGS.add("<frameset");
		TAGS.add("<frame");
		TAGS.add("<script");
		TAGS.add("<link");
		TAGS.add("<iframe");
		TAGS.add("<object");
		TAGS.add("<style");
		TAGS.add("<meta");
	}

	static
	{
		EVENTS.add("onabort");
		EVENTS.add("onactivate");
		EVENTS.add("onafterprint");
		EVENTS.add("onafterupdate");
		EVENTS.add("onbeforeactivate");
		EVENTS.add("onbeforecopy");
		EVENTS.add("onbeforecut");
		EVENTS.add("onbeforedeactivate");
		EVENTS.add("onbeforeeditfocus");
		EVENTS.add("onbeforepaste");
		EVENTS.add("onbeforeprint");
		EVENTS.add("onbeforeunload");
		EVENTS.add("onbeforeupdate");
		EVENTS.add("onbegin");
		EVENTS.add("onblur");
		EVENTS.add("onbounce");
		EVENTS.add("oncellchange");
		EVENTS.add("onchange");
		EVENTS.add("onchange");
		EVENTS.add("onclick");
		EVENTS.add("oncontentready");
		EVENTS.add("oncontentsave");
		EVENTS.add("oncontextmenu");
		EVENTS.add("oncontrolselect");
		EVENTS.add("oncopy");
		EVENTS.add("oncut");
		EVENTS.add("ondataavailable");
		EVENTS.add("ondatasetchanged");
		EVENTS.add("ondatasetcomplete");
		EVENTS.add("ondatasetcomplete");
		EVENTS.add("ondblclick");
		EVENTS.add("ondeactivate");
		EVENTS.add("ondetach");
		EVENTS.add("ondocumentready");
		EVENTS.add("ondrag");
		EVENTS.add("ondragdrop");
		EVENTS.add("ondragend");
		EVENTS.add("ondragenter");
		EVENTS.add("ondragleave");
		EVENTS.add("ondragover");
		EVENTS.add("ondragstart");
		EVENTS.add("ondrop");
		EVENTS.add("onend");
		EVENTS.add("onerror");
		EVENTS.add("onerror");
		EVENTS.add("onerror");
		EVENTS.add("onerror");
		EVENTS.add("onerrorupdate");
		EVENTS.add("onfilterchange");
		EVENTS.add("onfinish");
		EVENTS.add("onfocus");
		EVENTS.add("onfocusin");
		EVENTS.add("onfocusout");
		EVENTS.add("onhelp");
		EVENTS.add("onhide");
		EVENTS.add("onkeydown");
		EVENTS.add("onkeypress");
		EVENTS.add("onkeyup");
		EVENTS.add("onlayoutcomplete");
		EVENTS.add("onload");
		EVENTS.add("onload");
		EVENTS.add("onlosecapture");
		EVENTS.add("onmediacomplete");
		EVENTS.add("onmediaerror");
		EVENTS.add("onmedialoadfailed");
		EVENTS.add("onmousedown");
		EVENTS.add("onmouseenter");
		EVENTS.add("onmouseleave");
		EVENTS.add("onmousemove");
		EVENTS.add("onmouseout");
		EVENTS.add("onmouseover");
		EVENTS.add("onmouseup");
		EVENTS.add("onmousewheel");
		EVENTS.add("onmove");
		EVENTS.add("onmoveend");
		EVENTS.add("onmovestart");
		EVENTS.add("onopenstatechange");
		EVENTS.add("onoutofsync");
		EVENTS.add("onpaste");
		EVENTS.add("onpause");
		EVENTS.add("onplaystatechange");
		EVENTS.add("onpropertychange");
		EVENTS.add("onreadystatechange");
		EVENTS.add("onrepeat");
		EVENTS.add("onreset");
		EVENTS.add("onreset");
		EVENTS.add("onresize");
		EVENTS.add("onresizeend");
		EVENTS.add("onresizestart");
		EVENTS.add("onresume");
		EVENTS.add("onreverse");
		EVENTS.add("onrowclick");
		EVENTS.add("onrowenter");
		EVENTS.add("onrowexit");
		EVENTS.add("onrowout");
		EVENTS.add("onrowover");
		EVENTS.add("onrowsdelete");
		EVENTS.add("onrowsinserted");
		EVENTS.add("onsave");
		EVENTS.add("onscroll");
		EVENTS.add("onseek");
		EVENTS.add("onselect");
		EVENTS.add("onselectionchange");
		EVENTS.add("onselectstart");
		EVENTS.add("onshow");
		EVENTS.add("onstart");
		EVENTS.add("onstop");
		EVENTS.add("onsubmit");
		EVENTS.add("onsyncrestored");
		EVENTS.add("ontimeerror");
		EVENTS.add("ontrackchange");
		EVENTS.add("onunload");
		EVENTS.add("onurlflip");
	}

	public static String removeInvalidTag(String str)
	{
		if (CmStringUtils.isEmpty(str))
		{
			return str;
		}

		for (String tag : TAGS)
		{
			str = CmStringUtils.replaceIgnoreCase(str, tag, tag.replace("<", "&lt;"));
		}

		return str;
	}

	public static String removeInvalidEvent(String str)
	{
		if (CmStringUtils.isEmpty(str))
		{
			return str;
		}

		for (String event : EVENTS)
		{
			str = CmStringUtils.replaceIgnoreCase(str, event, event.replace("on", "on-"));
		}

		return str;
	}

	public static String removeInvalidTagAndEvent(String str)
	{
		return removeInvalidEvent(removeInvalidTag(str));
	}

	public static String convertToText(String str)
	{
		if (CmStringUtils.isEmpty(str))
		{
			return str;
		}

		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		//str = str.replaceAll("(", "&#40;");
		//str = str.replaceAll(")", "&#41;");
		str = str.replaceAll("#", "&#35;");
		str = str.replaceAll("&", "&#38;");
		str = str.replaceAll("'", "&#039;");
		str = str.replaceAll("\"", "&quot;");

		return str;
	}
}
