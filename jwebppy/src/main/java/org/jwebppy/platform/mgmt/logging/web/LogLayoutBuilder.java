package org.jwebppy.platform.mgmt.logging.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmMyBatisQueryUtils;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Div;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;

public class LogLayoutBuilder
{
	public static Document getList(PageableList<DataAccessLogDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addTextTh("Type", "one wide");
		thTr.addTextTh("Command", "three wide");
		thTr.addTextTh("Class Name", "four wide");
		thTr.addTextTh("Method Name", "two wide");
		thTr.addTextTh("Elapsed(sec)", "one wide");
		thTr.addTextTh("Start Time", "two wide");
		thTr.addTextTh("Finish Time", "two wide");
		thTr.addTextTh("Reg. Username", "one wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<DataAccessLogDto> dataAccessLogs = pageableList.getList();

		if (CollectionUtils.isNotEmpty(dataAccessLogs))
		{
			for (DataAccessLogDto dataAccessLog : dataAccessLogs)
			{
				Tr tbTr = new Tr();

				tbTr.addTextTd(dataAccessLog.getType().getType());
				tbTr.addDataKeyLinkTd(dataAccessLog.getCommand(), dataAccessLog.getDlSeq());
				tbTr.addTextTd(dataAccessLog.getClassName());
				tbTr.addTextTd(dataAccessLog.getMethodName());
				tbTr.addTextTd(CmNumberUtils.round(dataAccessLog.getElapsedTime(), "#.###"));
				tbTr.addTextTd(dataAccessLog.getStartTimeToDate());
				tbTr.addTextTd(dataAccessLog.getFinishTimeToDate());
				tbTr.addTextTd(dataAccessLog.getRegUsername());

				if (dataAccessLog.getError() != null)
				{
					tbTr.setClass("error");
				}

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table(pageableList);
		table.addThead(thead);
		table.addTbody(tbody);

		Document document = new Document();
		document.addElement(table);

		return document;
	}

	public static Document getLog(DataAccessLogDto dataAccessLog)
	{
		if (CmStringUtils.equals(dataAccessLog.getType(), "J"))
		{
			return getJdbcLog(dataAccessLog);
		}
		else if (CmStringUtils.equals(dataAccessLog.getType(), "R"))
		{
			return getRfcLog(dataAccessLog);
		}

		return null;
	}

	public static Document getJdbcLog(DataAccessLogDto dataAccessLog)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Type", dataAccessLog.getType().getType());
		elementMap.put("Reg. Date", CmDateFormatUtils.format(dataAccessLog.getRegDate()));
		elementMap.put("Reg. Username", dataAccessLog.getRegUsername());
		elementMap.put("Elapsed", CmNumberUtils.round(dataAccessLog.getElapsedTime(), "#.###"));
		elementMap.put("Command", new Element("xmp", CmMyBatisQueryUtils.format(dataAccessLog.getCommand())));

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		List<DataAccessLogParameterDto> dataAccessLogParameters = dataAccessLog.getDataAccessLogParameters();

		if (CollectionUtils.isNotEmpty(dataAccessLogParameters))
		{
			Element title = new Element("h4", "Parameters");
			title.setClass("ui header");

			document.addElement(title);

			Tr thTr = new Tr();
			thTr.addTextTh("Name", "two wide");
			thTr.addTextTh("Value", "fourteen wide");

			Thead thead = new Thead();
			thead.addTr(thTr);

			Tbody tbody = new Tbody();

			for (DataAccessLogParameterDto dataAccessLogParameter : dataAccessLogParameters)
			{
				for (DataAccessLogParameterDetailDto dataAccessLogParameterDetail : dataAccessLogParameter.getDataAccessLogParameterDetails())
				{
					Tr tbTr = new Tr();
					tbTr.addTextTd(dataAccessLogParameterDetail.getName());
					tbTr.addTextTd(dataAccessLogParameterDetail.getValue());

					tbody.addTr(tbTr);
				}
			}

			Table table = new Table();
			table.addThead(thead);
			table.addTbody(tbody);

			document.addElement(table);
		}

		return document;
	}

	public static Document getRfcLog(DataAccessLogDto dataAccessLog)
	{
		Document document = new Document();

		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Type", dataAccessLog.getType().getType());
		elementMap.put("Command", dataAccessLog.getCommand());
		elementMap.put("Reg. Date", CmDateFormatUtils.format(dataAccessLog.getRegDate()));
		elementMap.put("Reg. Username", dataAccessLog.getRegUsername());
		elementMap.put("Elapsed(sec)", CmNumberUtils.round(dataAccessLog.getElapsedTime(), "#.###"));

		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));
		document.addElement(Div.hiddenDivider());

		addFields(document, dataAccessLog);

		document.addElement(Div.hiddenDivider());

		addStructures(document, dataAccessLog);

		document.addElement(Div.hiddenDivider());

		addTables(document, dataAccessLog);

		return document;
	}

	public static Document getRfcResultLog(DataAccessLogDto dataAccessLog)
	{
		Document document = new Document();

		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Reg. Date", CmDateFormatUtils.format(dataAccessLog.getRegDate()));

		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));
		document.addElement(Div.hiddenDivider());

		addFields(document, dataAccessLog);

		document.addElement(Div.hiddenDivider());

		addStructures(document, dataAccessLog);

		document.addElement(Div.hiddenDivider());

		addTables(document, dataAccessLog);

		return document;
	}

	private static void addFields(Document document, DataAccessLogDto dataAccessLog)
	{
		int count = 0;

		if (CollectionUtils.isNotEmpty(dataAccessLog.getDataAccessLogParameters()))
		{
			Element segment = new Div();
			segment.setClass("ui teal segment");

			for (DataAccessLogParameterDto dataAccessLogParameter : ListUtils.emptyIfNull(dataAccessLog.getDataAccessLogParameters()))
			{
				if (!ParameterType.F.equals(dataAccessLogParameter.getType()))
				{
					continue;
				}

				for (DataAccessLogParameterDetailDto logParameterDetail : ListUtils.emptyIfNull(dataAccessLogParameter.getDataAccessLogParameterDetails()))
				{
					segment.addElement(PlatformLayoutBuildUtils.defaultLabelText(logParameterDetail.getName(), logParameterDetail.getValue()));

					count++;
				}
			}

			if (count > 0)
			{
				Element title = new Element("h4", "Scalar");
				title.setClass("ui block header");

				document.addElement(title);

				document.addElement(segment);
			}
		}
	}

	private static void addStructures(Document document, DataAccessLogDto dataAccessLog)
	{
		if (CollectionUtils.isNotEmpty(dataAccessLog.getDataAccessLogParameters()))
		{
			List<DataAccessLogParameterDto> dataAccessLogParameters = new ArrayList<>();

			for (DataAccessLogParameterDto dataAccessLogParameter : ListUtils.emptyIfNull(dataAccessLog.getDataAccessLogParameters()))
			{
				if (!ParameterType.S.equals(dataAccessLogParameter.getType()))
				{
					continue;
				}

				dataAccessLogParameters.add(dataAccessLogParameter);
			}

			if (CollectionUtils.isNotEmpty(dataAccessLogParameters))
			{
				Element title = new Element("h4", "Structure");
				title.setClass("ui block header");

				document.addElement(title);

				for (DataAccessLogParameterDto dataAccessLogParameter : ListUtils.emptyIfNull(dataAccessLog.getDataAccessLogParameters()))
				{
					if (!ParameterType.S.equals(dataAccessLogParameter.getType()))
					{
						continue;
					}

					document.addElement(header(dataAccessLogParameter.getName()));

					Element segment = new Div();
					segment.setClass("ui teal segment");

					for (DataAccessLogParameterDetailDto logParameterDetail : ListUtils.emptyIfNull(dataAccessLogParameter.getDataAccessLogParameterDetails()))
					{
						segment.addElement(PlatformLayoutBuildUtils.defaultLabelText(logParameterDetail.getName(), logParameterDetail.getValue()));
					}

					document.addElement(segment);
				}
			}
		}
	}

	private static void addTables(Document document, DataAccessLogDto dataAccessLog)
	{
		List<DataAccessLogParameterDto> dataAccessLogParameters = new ArrayList<>();

		for (DataAccessLogParameterDto dataAccessLogParameter : ListUtils.emptyIfNull(dataAccessLog.getDataAccessLogParameters()))
		{
			if (!ParameterType.T.equals(dataAccessLogParameter.getType()))
			{
				continue;
			}

			dataAccessLogParameters.add(dataAccessLogParameter);
		}

		if (CollectionUtils.isNotEmpty(dataAccessLogParameters))
		{
			Element title = new Element("h4", "Table");
			title.setClass("ui block header");

			document.addElement(title);

			for (DataAccessLogParameterDto dataAccessLogParameter : dataAccessLogParameters)
			{
				List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails = ListUtils.emptyIfNull(dataAccessLogParameter.getDataAccessLogParameterDetails());

				if (CollectionUtils.isNotEmpty(dataAccessLogParameterDetails))
				{
					document.addElement(header(dataAccessLogParameter.getName()));

					Set<String> trSet = new LinkedHashSet<>();
					int lineNo = dataAccessLogParameterDetails.get(0).getLineNo();
					Tr thTr = new Tr();

					for (DataAccessLogParameterDetailDto logParameterDetail : dataAccessLogParameterDetails)
					{
						if (lineNo != logParameterDetail.getLineNo())
						{
							break;
						}

						trSet.add(logParameterDetail.getName());
						thTr.addTextTh(logParameterDetail.getName());
					}

					Thead thead = new Thead();
					thead.addTr(thTr);

					lineNo = dataAccessLogParameterDetails.get(0).getLineNo();
					Tbody tbody = new Tbody();
					Tr tbTr = new Tr();

					Map<String, String> dataMap = new HashMap<>();

					for (DataAccessLogParameterDetailDto logParameterDetail : dataAccessLogParameterDetails)
					{
						if (lineNo == logParameterDetail.getLineNo())
						{
							dataMap.put(logParameterDetail.getName(), logParameterDetail.getValue());
							continue;
						}
						else
						{
							for (String key : trSet)
							{
								tbTr.addTextTd(dataMap.get(key));
							}

							tbody.addTr(tbTr);
							tbTr = new Tr();

							dataMap.clear();
							dataMap.put(logParameterDetail.getName(), logParameterDetail.getValue());

							lineNo = logParameterDetail.getLineNo();
						}
					}

					for (String key : trSet)
					{
						tbTr.addTextTd(dataMap.get(key));
					}

					tbody.addTr(tbTr);

					Table table = new Table();
					table.setStyle("overflow-x: auto; display: block; white-space: nowrap;");
					table.addThead(thead);
					table.addTbody(tbody);

					document.addElement(table);
				}
			}
		}
	}

	private static Element header(String title)
	{
		Element element = new Div(title);
		element.setClass("ui dividing small header");

		return element;
	}

	public static Document getError(DataAccessLogDto dataAccessLog)
	{
		Document document = new Document();
		document.addElement(PlatformLayoutBuildUtils.defaultText(new Element("xmp", dataAccessLog.getError())));

		return document;
	}
}
