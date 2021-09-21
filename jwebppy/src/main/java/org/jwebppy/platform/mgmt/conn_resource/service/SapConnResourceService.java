package org.jwebppy.platform.mgmt.conn_resource.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceSearchDto;
import org.jwebppy.platform.mgmt.conn_resource.entity.SapConnResourceEntity;
import org.jwebppy.platform.mgmt.conn_resource.mapper.SapConnResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SapConnResourceService extends GeneralService
{
	@Autowired
	private SapConnResourceMapper sapConnResourceMapper;

	public int save(SapConnResourceDto sapConnResource)
	{
		if (sapConnResource.getScrSeq() == null)
		{
			return create(sapConnResource);
		}
		else
		{
			return modify(sapConnResource);
		}
	}

	public int create(SapConnResourceDto sapConnResource)
	{
		SapConnResourceEntity sapConnResourceEntity = CmModelMapperUtils.map(sapConnResource, SapConnResourceEntity.class);

		sapConnResourceMapper.insert(sapConnResourceEntity);

		return sapConnResourceEntity.getScrSeq();
	}

	public int modify(SapConnResourceDto sapConnResource)
	{
		return sapConnResourceMapper.update(CmModelMapperUtils.map(sapConnResource, SapConnResourceEntity.class));
	}

	public int modifyFgUse(Integer scrSeq, String fgUse)
	{
		return sapConnResourceMapper.updateFgUse(scrSeq, fgUse);
	}

	public int delete(Integer scrSeq)
	{
		return sapConnResourceMapper.updateFgDelete(scrSeq);
	}

	public int delete(List<Integer> scrSeqs)
	{
		int result = 0;

		if (CollectionUtils.isNotEmpty(scrSeqs))
		{
			for (Integer scrSeq: scrSeqs)
			{
				result += delete(scrSeq);
			}
		}

		return result;
	}

	public SapConnResourceDto getSapConnResource(Integer scrSeq)
	{
		return CmModelMapperUtils.map(sapConnResourceMapper.findSapConnResource(scrSeq), SapConnResourceDto.class);
	}

	public List<SapConnResourceDto> getPageableSapConnResources(SapConnResourceSearchDto sapConnResourceSearch)
	{
		return CmModelMapperUtils.mapAll(sapConnResourceMapper.findPageSapConnResources(sapConnResourceSearch), SapConnResourceDto.class);
	}

	public List<SapConnResourceDto> getAvailableSapConnResources()
	{
		return CmModelMapperUtils.mapAll(sapConnResourceMapper.findAvailableSapConnResources(), SapConnResourceDto.class);
	}
}
