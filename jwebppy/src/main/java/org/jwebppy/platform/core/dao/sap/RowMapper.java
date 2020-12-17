package org.jwebppy.platform.core.dao.sap;

import java.util.Map;

public interface RowMapper
{
    public Map<String, Object> mapRow(Object rs);
}
