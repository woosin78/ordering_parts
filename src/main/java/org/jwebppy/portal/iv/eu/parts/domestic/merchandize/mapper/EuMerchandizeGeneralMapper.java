package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeGeneralDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeGeneralEntity;

@Mapper
public interface EuMerchandizeGeneralMapper
{	
	public MerchandizeGeneralEntity findUpdateSortInfo(MerchandizeGeneralEntity entity);
	public int updateSortNumbers(MerchandizeGeneralDto dto);
	
	public int updateSortNumber(MerchandizeGeneralDto dto);
}
