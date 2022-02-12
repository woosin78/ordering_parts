package org.jwebppy.platform.core.dao.sap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.dao.AbstractDaoRequest;
import org.jwebppy.platform.core.util.CmStringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RfcRequest extends AbstractDaoRequest
{
	private String connectorName;
	private String functionName;

	private final Map<String, Map<String, Object>> PARAM_MAP = new HashMap<>();

	private final String DEFAULT_FIELD_KEY = "_FIELD";
	private final String DEFAULT_STRUCTURE_KEY = "_STRUCTURE";
	private final String DEFAULT_TABLE_KEY = "_TABLE";

	private boolean isOnField = false;
	private String structureName;
	private String tableName;

	public RfcRequest() {}

	public RfcRequest(String functionName)
	{
		this.functionName = functionName;
	}

	public RfcRequest(String connectorName, String functionName)
	{
		this.connectorName = connectorName;
		this.functionName = functionName;
	}

	public RfcRequest with(Map paramMap)
	{
		if (isOnField)
		{
			PARAM_MAP.put(DEFAULT_FIELD_KEY, paramMap);
		}
		else if (CmStringUtils.isNotEmpty(structureName))
		{
			PARAM_MAP.put(structureName, paramMap);
		}
		else if (CmStringUtils.isNotEmpty(tableName))
		{
			PARAM_MAP.put(tableName, paramMap);
		}

		return this;
	}

	public RfcRequest field()
	{
		reset();

		isOnField = true;

		return this;
	}

	public RfcRequest field(String name, Object value)
	{
		isOnField = true;

		addToRequest(name, value);

		return this;
	}

	public RfcRequest field(Object[][] fields)
	{
		if (ArrayUtils.isNotEmpty(fields))
		{
			isOnField = true;

			for (Object[] field: fields)
			{
				if (ArrayUtils.isNotEmpty(field) && field.length == 2)
				{
					if (field[1] != null)
					{
						addToRequest((String)field[0], field[1]);
					}
				}
			}
		}

		return this;
	}

	public RfcRequest structure()
	{
		return structure(DEFAULT_STRUCTURE_KEY);
	}

	public RfcRequest structure(String structureName)
	{
		this.structureName = structureName;

		return this;
	}

	public RfcRequest table()
	{
		return table(DEFAULT_TABLE_KEY);
	}

	public RfcRequest table(String tableName)
	{
		this.tableName = tableName;

		return this;
	}

	private void addToRequest(String name, Object value)
	{
		if (value != null)
		{
			if (CmStringUtils.isNotEmpty(tableName))
			{
				if (value instanceof Map)
				{
					addTable(name, (Map)value);
				}
				else if (value instanceof List)
				{
					addTable(name, (List)value);
				}
				else
				{
					Map<String, Object> rowMap = new HashMap<>();
					rowMap.put(name, value);

					List<Map<String, Object>> tableList = new LinkedList<>();
					tableList.add(rowMap);

					addTable(tableName, tableList);
				}
			}
			else if (CmStringUtils.isNotEmpty(structureName))
			{
				if (value instanceof Map)
				{
					addStructure(name, value);
				}
				else
				{
					addStructure(structureName, name, value);
				}
			}
			else if (isOnField)
			{
				addField(name, value);
			}
		}
	}

	public RfcRequest add(String name, Object value)
	{
		addToRequest(name, value);

		return this;
	}

	public RfcRequest add(Object[][] fields)
	{
		if (ArrayUtils.isNotEmpty(fields))
		{
			for (Object[] field: fields)
			{
				if (ArrayUtils.isNotEmpty(field) && field.length == 2)
				{
					if (field[1] != null)
					{
						addToRequest((String)field[0], field[1]);
					}
				}
			}
		}

		return this;
	}

	public RfcRequest add(Map<String, Object> valueMap)
	{
		if (MapUtils.isNotEmpty(valueMap))
		{
			if (CmStringUtils.isNotEmpty(tableName))
			{
				List<Map<String, Object>> tableList = new LinkedList<>();
				tableList.add(valueMap);

				addTable(tableName, tableList);
			}
			else if (CmStringUtils.isNotEmpty(structureName))
			{
				addStructure(structureName, valueMap);
			}
			else if (isOnField)
			{
				addField(valueMap);
			}
		}

		return this;
	}

	public RfcRequest add(List<Map<String, Object>> tableList)
	{
		if (CmStringUtils.isNotEmpty(tableName))
		{
			addTable(tableName, tableList);
		}

		return this;
	}

	public RfcRequest addByKey(String to, String from)
	{
		addByKeyToRequest(to, from);

		return this;
	}

	public RfcRequest addByKey(Object[][] fields)
	{
		if (ArrayUtils.isNotEmpty(fields))
		{
			for (Object[] field: fields)
			{
				if (ArrayUtils.isNotEmpty(field) && field.length == 2)
				{
					if (CmStringUtils.isNotEmpty(field[0]) && CmStringUtils.isNotEmpty(field[1]))
					{
						addByKeyToRequest((String)field[0], (String)field[1]);
					}
				}
			}
		}

		return this;
	}

	public void addByKeyToRequest(String to, String from)
	{
		if (CmStringUtils.isNotEmpty(tableName))
		{
			Map valueMap = PARAM_MAP.get(tableName);

			if (MapUtils.isNotEmpty(valueMap))
			{
				Object value = valueMap.get(from);

				if (value instanceof Map)
				{
					Map<String, Object> tableValueMap = (Map)value;

					if (MapUtils.isNotEmpty(tableValueMap))
					{
						Map<String, Object> rowMap = new HashMap<>();
						rowMap.put(to, tableValueMap.get(from));

						List<Map<String, Object>> tableList = new LinkedList<>();
						tableList.add(rowMap);

						addTable(to, tableList);
					}
				}
				else if (value instanceof List)
				{
					addTable(to, (List)value);
				}
				else
				{
					addToRequest(to, value);
				}
			}
		}
		else if (CmStringUtils.isNotEmpty(structureName))
		{
			if (DEFAULT_STRUCTURE_KEY.equals(structureName))
			{
				Map strucureMap = PARAM_MAP.get(structureName);

				if (MapUtils.isNotEmpty(strucureMap))
				{
					addStructure(to, strucureMap.get(from));
				}
			}
			else
			{
				Map strucureMap = PARAM_MAP.get(structureName);

				if (MapUtils.isNotEmpty(strucureMap))
				{
					addStructure(structureName, to, PARAM_MAP.get(structureName).get(from));
				}
			}
		}
		else if (isOnField)
		{
			Map<String, Object> fieldValueMap = PARAM_MAP.get(DEFAULT_FIELD_KEY);

			if (MapUtils.isNotEmpty(fieldValueMap))
			{
				addField(to, fieldValueMap.get(from));
			}
		}
	}

	public RfcRequest row()
	{
		this.tableName = null;

		return this;
	}

	public RfcRequest and()
	{
		reset();

		return this;
	}

	private void reset()
	{
		isOnField = false;
		structureName = null;
		tableName = null;
	}

	public RfcRequest output(String name)
	{
		addOutputParameter(name);

		return this;
	}

	public RfcRequest output(String[] names)
	{
		if (ArrayUtils.isNotEmpty(names))
		{
			for (String name: names)
			{
				addOutputParameter(name);
			}
		}

		return this;
	}
}
