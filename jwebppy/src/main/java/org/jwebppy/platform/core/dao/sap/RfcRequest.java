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

	private final Map<String, Map<String, Object>> valueMap = new HashMap<>();

	private final String FIELD_KEY = "FIELD";
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
			valueMap.put(FIELD_KEY, paramMap);
		}
		else if (CmStringUtils.isNotEmpty(structureName))
		{
			valueMap.put(structureName, paramMap);
		}
		else if (CmStringUtils.isNotEmpty(tableName))
		{
			valueMap.put(tableName, paramMap);
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
		addToRequest(name, value);

		return this;
	}

	public RfcRequest field(Object[][] fields)
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

	public RfcRequest structure(String structureName)
	{
		this.structureName = structureName;

		return this;
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
			Map<String, Object> tableValueMap = valueMap.get(tableName);

			if (MapUtils.isNotEmpty(tableValueMap))
			{
				Map<String, Object> rowMap = new HashMap<>();
				rowMap.put(to, tableValueMap.get(from));

				List<Map<String, Object>> tableList = new LinkedList<>();
				tableList.add(rowMap);

				addTable(tableName, tableList);
			}
		}
		else if (CmStringUtils.isNotEmpty(structureName))
		{
			Map<String, Object> structureValueMap = valueMap.get(structureName);

			if (MapUtils.isNotEmpty(structureValueMap))
			{
				addStructure(this.structureName, to, structureValueMap.get(from));
			}
		}
		else if (isOnField)
		{
			Map<String, Object> fieldValueMap = valueMap.get(FIELD_KEY);

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
