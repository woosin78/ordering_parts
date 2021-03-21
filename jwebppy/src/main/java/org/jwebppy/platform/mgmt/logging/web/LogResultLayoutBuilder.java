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
import org.jwebppy.platform.core.web.ui.dom.Div;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;

public class LogResultLayoutBuilder
{
	public static Document getLog(DataAccessResultLogDto dataAccessResultLog)
	{
		return getRfcLog(dataAccessResultLog);
	}

	public static Document getRfcLog(DataAccessResultLogDto dataAccessResultLog)
	{
		Document document = new Document();

		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Reg. Date", CmDateFormatUtils.format(dataAccessResultLog.getRegDate()));

		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));
		document.addElement(Div.hiddenDivider());

		addFields(document, dataAccessResultLog);

		document.addElement(Div.hiddenDivider());

		addStructures(document, dataAccessResultLog);

		document.addElement(Div.hiddenDivider());

		addTables(document, dataAccessResultLog);

		return document;
	}

	private static void addFields(Document document, DataAccessResultLogDto dataAccessResultLog)
	{
		int count = 0;

		if (CollectionUtils.isNotEmpty(dataAccessResultLog.getDataAccessResultLogParameters()))
		{
			Element segment = new Div();
			segment.setClass("ui teal segment");

			for (DataAccessResultLogParameterDto dataAccessResultLogParameter : ListUtils.emptyIfNull(dataAccessResultLog.getDataAccessResultLogParameters()))
			{
				if (!ParameterType.F.equals(dataAccessResultLogParameter.getType()))
				{
					continue;
				}

				for (DataAccessResultLogParameterDetailDto logParameterDetail : ListUtils.emptyIfNull(dataAccessResultLogParameter.getDataAccessResultLogParameterDetails()))
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

	private static void addStructures(Document document, DataAccessResultLogDto dataAccessResultLog)
	{
		if (CollectionUtils.isNotEmpty(dataAccessResultLog.getDataAccessResultLogParameters()))
		{
			List<DataAccessResultLogParameterDto> dataAccessResultLogParameters = new ArrayList<>();

			for (DataAccessResultLogParameterDto dataAccessResultLogParameter : ListUtils.emptyIfNull(dataAccessResultLog.getDataAccessResultLogParameters()))
			{
				if (!ParameterType.S.equals(dataAccessResultLogParameter.getType()))
				{
					continue;
				}

				dataAccessResultLogParameters.add(dataAccessResultLogParameter);
			}

			if (CollectionUtils.isNotEmpty(dataAccessResultLogParameters))
			{
				Element title = new Element("h4", "Structure");
				title.setClass("ui block header");

				document.addElement(title);

				for (DataAccessResultLogParameterDto dataAccessResultLogParameter : ListUtils.emptyIfNull(dataAccessResultLog.getDataAccessResultLogParameters()))
				{
					if (!ParameterType.S.equals(dataAccessResultLogParameter.getType()))
					{
						continue;
					}

					document.addElement(header(dataAccessResultLogParameter.getName()));

					Element segment = new Div();
					segment.setClass("ui teal segment");

					for (DataAccessResultLogParameterDetailDto logParameterDetail : ListUtils.emptyIfNull(dataAccessResultLogParameter.getDataAccessResultLogParameterDetails()))
					{
						segment.addElement(PlatformLayoutBuildUtils.defaultLabelText(logParameterDetail.getName(), logParameterDetail.getValue()));
					}

					document.addElement(segment);
				}
			}
		}
	}

	private static void addTables(Document document, DataAccessResultLogDto dataAccessResultLog)
	{
		List<DataAccessResultLogParameterDto> dataAccessResultLogParameters = new ArrayList<>();

		for (DataAccessResultLogParameterDto dataAccessResultLogParameter : ListUtils.emptyIfNull(dataAccessResultLog.getDataAccessResultLogParameters()))
		{
			if (!ParameterType.T.equals(dataAccessResultLogParameter.getType()))
			{
				continue;
			}

			dataAccessResultLogParameters.add(dataAccessResultLogParameter);
		}

		if (CollectionUtils.isNotEmpty(dataAccessResultLogParameters))
		{
			Element title = new Element("h4", "Table");
			title.setClass("ui block header");

			document.addElement(title);

			for (DataAccessResultLogParameterDto dataAccessResultLogParameter : dataAccessResultLogParameters)
			{
				List<DataAccessResultLogParameterDetailDto> dataAccessResultLogParameterDetails = ListUtils.emptyIfNull(dataAccessResultLogParameter.getDataAccessResultLogParameterDetails());

				if (CollectionUtils.isNotEmpty(dataAccessResultLogParameterDetails))
				{
					document.addElement(header(dataAccessResultLogParameter.getName()));

					Set<String> trSet = new LinkedHashSet<>();
					int lineNo = dataAccessResultLogParameterDetails.get(0).getLineNo();
					Tr thTr = new Tr();

					for (DataAccessResultLogParameterDetailDto logParameterDetail : dataAccessResultLogParameterDetails)
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

					lineNo = dataAccessResultLogParameterDetails.get(0).getLineNo();
					Tbody tbody = new Tbody();
					Tr tbTr = new Tr();

					Map<String, String> dataMap = new HashMap<>();

					for (DataAccessResultLogParameterDetailDto logParameterDetail : dataAccessResultLogParameterDetails)
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
}
