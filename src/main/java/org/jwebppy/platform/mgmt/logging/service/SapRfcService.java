package org.jwebppy.platform.mgmt.logging.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.common.service.MgmtGeneralService;
import org.jwebppy.platform.mgmt.logging.dto.SapRfcDto;
import org.jwebppy.platform.mgmt.logging.mapper.SapRfcMapper;
import org.jwebppy.platform.mgmt.logging.mapper.SapRfcObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SapRfcService extends MgmtGeneralService
{
	@Autowired
	private SapRfcMapper sapRfcMapper;

	public SapRfcDto getRfcByName(String name)
	{
		return CmModelMapperUtils.mapToDto(SapRfcObjectMapper.INSTANCE, sapRfcMapper.findByName(name));
	}
}
