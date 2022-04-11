package org.jwebppy.portal.iv.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;

public class BwParseHelper
{
	public final String NAME_KEY = "VAR_NAME_";
	public final String VALUE_KEY = "VAR_VALUE_EXT_";
	public final String LOW_VALUE_KEY = "VAR_VALUE_LOW_EXT_";
	public final String HIGH_VALUE_KEY = "VAR_VALUE_HIGH_EXT_";

	public final String O_KEY = "VAR_OPERATOR_";
	public final String O_EQUAL = "EQ";
	public final String O_BETWEEN = "BT";
	public final String O_LESSTHAN = "LT";
	public final String O_LESSTHAN_OR_EQUAL = "LE";
	public final String O_GREATTHAN = "GT";
	public final String O_GREATTHAN_OR_EQUAL = "GE";

	int seq = 1;

	public List<Map<String, Object>> range(String name, Object value)
	{
		List<Map<String, Object>> list = new ArrayList<>();

		Map<String, Object> rowMap1 = new HashMap<>();
		rowMap1.put("NAME", NAME_KEY + seq);
		rowMap1.put("VALUE", name);
		list.add(rowMap1);

		Map<String, Object> rowMap2 = new HashMap<>();
		rowMap2.put("NAME", LOW_VALUE_KEY + seq);
		rowMap2.put("VALUE", value);
		list.add(rowMap2);

		Map<String, Object> rowMap3 = new HashMap<>();
		rowMap3.put("NAME", HIGH_VALUE_KEY + seq);
		rowMap3.put("VALUE", value);
		list.add(rowMap3);

		Map<String, Object> rowMap4 = new HashMap<>();
		rowMap4.put("NAME", O_KEY + seq);
		rowMap4.put("VALUE", O_EQUAL);
		list.add(rowMap4);

		seq++;

		return list;
	}

	public List<Map<String, Object>> single(String name, Object value)
	{
		List<Map<String, Object>> list = new ArrayList<>();

		Map<String, Object> rowMap1 = new HashMap<>();
		rowMap1.put("NAME", NAME_KEY + seq);
		rowMap1.put("VALUE", name);
		list.add(rowMap1);

		Map<String, Object> rowMap2 = new HashMap<>();
		rowMap2.put("NAME", VALUE_KEY + seq);
		rowMap2.put("VALUE", value);
		list.add(rowMap2);

		seq++;

		return list;
	}

	public Map<String, Object> parse(RfcResponse rfcResponse)
	{
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("BW_COLUMN", column(rfcResponse));
		resultMap.put("BW_DATA", data(rfcResponse));
		resultMap.put("BW_AXIS", axis(rfcResponse));

		return resultMap;
	}

	private Map<String, Object>[] column(RfcResponse rfcResponse)
	{
		DataList dataList = rfcResponse.getTable("E_T_AXIS_DATA_COLUMNS");

		if (CollectionUtils.isNotEmpty(dataList))
		{
			ListIterator<DataMap> it = dataList.listIterator();

			Map<String, Object>[] columns = new LinkedHashMap[dataList.size()];

			while (it.hasNext())
			{
				columns[it.nextIndex()] = it.next();
			}

			return columns;
		}

		return null;
	}

	private Map<String, Object>[][] data(RfcResponse rfcResponse)
	{
		DataList axisList = rfcResponse.getTable("E_T_AXIS_DATA_ROWS");

		if (CollectionUtils.isNotEmpty(axisList))
		{
			DataList dataList = rfcResponse.getTable("E_T_CELL_DATA");
			DataList columnList = rfcResponse.getTable("E_T_AXIS_DATA_COLUMNS");

			int size = dataList.size();
			int axisSize = axisList.size();
			int columnSize = columnList.size();

			if (CollectionUtils.isNotEmpty(dataList) && (size == axisSize * columnSize))
			{
				DataMap[][] rows = new DataMap[axisSize][columnSize];

				for (int i=0; i<axisSize; i++)
				{
					for (int j=0; j<columnSize; j++)
					{
						rows[i][j] = (DataMap)dataList.get(i*columnSize + j);
					}
				}

				return rows;
			}
		}

		return null;
	}

	private Map<String, Object>[] axis(RfcResponse rfcResponse)
	{
		DataList axisList = rfcResponse.getTable("E_T_AXIS_DATA_ROWS");

		if (CollectionUtils.isNotEmpty(axisList))
		{
			int size = axisList.size();

			Map<String, Object>[] axises = new HashMap[size];

			ListIterator<DataMap> it = axisList.listIterator();

			while (it.hasNext())
			{
				axises[it.nextIndex()] = it.next();
			}

			return axises;
		}

		return null;
	}
}
