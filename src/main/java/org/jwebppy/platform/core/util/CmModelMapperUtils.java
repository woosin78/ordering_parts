package org.jwebppy.platform.core.util;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.mapper.GeneralObjectMapper;

public class CmModelMapperUtils
{
	@Deprecated
	public static <D, S> List<D> mapAll(List<S> source, Class<D> destinationType)
	{
		if (CollectionUtils.isNotEmpty(source))
		{
			return new CmModelMapper().mapAll(source, destinationType);
		}

		return null;
	}

	@Deprecated
	public static <D> D map(Object source, Class<D> destinationType)
	{
		if (source == null)
		{
			return null;
		}

		return new CmModelMapper().map(source, destinationType);
	}

	public static <D, E> D mapToDto(GeneralObjectMapper<D, E> objectMapper, E source)
	{
		if (source == null)
		{
			return null;
		}

		return objectMapper.toDto(source);
	}

	public static <D, E> E mapToEntity(GeneralObjectMapper<D, E> objectMapper, D source)
	{
		if (source == null)
		{
			return null;
		}

		return objectMapper.toEntity(source);
	}

	public static <D, E> List<D> mapToDto(GeneralObjectMapper<D, E> objectMapper, List<E> source)
	{
		if (CollectionUtils.isEmpty(source))
		{
			return null;
		}

		return objectMapper.toDtoList(source);
	}

	public static <D, E> List<E> mapAllToEntity(GeneralObjectMapper<D, E> objectMapper, List<D> source)
	{
		if (CollectionUtils.isEmpty(source))
		{
			return null;
		}

		return objectMapper.toEntityList(source);
	}
}
