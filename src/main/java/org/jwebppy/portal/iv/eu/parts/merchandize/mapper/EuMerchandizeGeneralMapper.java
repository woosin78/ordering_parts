package org.jwebppy.portal.iv.eu.parts.merchandize.mapper;

import org.jwebppy.portal.iv.eu.parts.merchandize.dto.MerchandizeGeneralDto;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeGeneralEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EuMerchandizeGeneralMapper
{	
	public MerchandizeGeneralEntity findUpdateSortInfo(MerchandizeGeneralEntity entity);
	public int updateSortNumbers(MerchandizeGeneralDto dto);
	
	public int updateSortNumber(MerchandizeGeneralDto dto);
}
