package org.jwebppy.platform.mgmt.conn_resource.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceSearchDto;
import org.jwebppy.platform.mgmt.conn_resource.entity.SapConnResourceEntity;
import org.jwebppy.platform.mgmt.conn_resource.mapper.SapConnResourceMapper;
import org.jwebppy.platform.mgmt.conn_resource.mapper.SapConnResourceObjectMapper;
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
		if (CmStringUtils.isNotEmpty(sapConnResource.getPassword()))
		{
			sapConnResource.setPassword(AES256Cipher.getInstance().encode(sapConnResource.getPassword()));
		}

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
		SapConnResourceEntity sapConnResourceEntity = CmModelMapperUtils.mapToEntity(SapConnResourceObjectMapper.INSTANCE, sapConnResource);

		sapConnResourceMapper.insert(sapConnResourceEntity);

		return sapConnResourceEntity.getScrSeq();
	}

	public int modify(SapConnResourceDto sapConnResource)
	{
		return sapConnResourceMapper.update(CmModelMapperUtils.mapToEntity(SapConnResourceObjectMapper.INSTANCE, sapConnResource));
	}

	public int modifyFgUse(List<Integer> scrSeqs, String fgUse)
	{
		int result = 0;

		for (Integer scrSeq: ListUtils.emptyIfNull(scrSeqs))
		{
			SapConnResourceEntity sapConnResource = new SapConnResourceEntity(scrSeq);
			sapConnResource.setFgUse(fgUse);

			result += sapConnResourceMapper.updateFgUse(sapConnResource);
		}

		return result;
	}

	public int delete(Integer scrSeq)
	{
		return sapConnResourceMapper.updateFgDelete(new SapConnResourceEntity(scrSeq));
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
		return CmModelMapperUtils.mapToDto(SapConnResourceObjectMapper.INSTANCE, sapConnResourceMapper.findSapConnResource(scrSeq));
	}

	public SapConnResourceDto getSapConnResourceByName(String name)
	{
		return CmModelMapperUtils.mapToDto(SapConnResourceObjectMapper.INSTANCE, sapConnResourceMapper.findSapConnResourceByName(name));
	}

	public List<SapConnResourceDto> getPageableSapConnResources(SapConnResourceSearchDto sapConnResourceSearch)
	{
		return CmModelMapperUtils.mapToDto(SapConnResourceObjectMapper.INSTANCE, sapConnResourceMapper.findPageSapConnResources(sapConnResourceSearch));
	}

	public List<SapConnResourceDto> getAvailableSapConnResources()
	{
		return CmModelMapperUtils.mapToDto(SapConnResourceObjectMapper.INSTANCE, sapConnResourceMapper.findAvailableSapConnResources());
	}
}
