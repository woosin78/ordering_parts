package org.jwebppy.platform.core.util;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class CmModelMapperUtils
{
	public static <D, S> List<D> mapAll(List<S> source, Class<D> destinationType)
	{
		if (CollectionUtils.isNotEmpty(source))
		{
			return new CmModelMapper().mapAll(source, destinationType);
		}

		return null;
	}

	public static <D> D map(Object source, Class<D> destinationType)
	{
		if (source == null)
		{
			return null;
		}

		return new CmModelMapper().map(source, destinationType);
	}
}
