package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeMainDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeMainEntity;

@Mapper
public interface EuMerchandizeMainMapper
{
	public MerchandizeMainEntity findMainManagementInfo(MerchandizeMainDto mainDto);	
	public void mergeCategoryInfo(MerchandizeMainEntity mainInfo);
}
