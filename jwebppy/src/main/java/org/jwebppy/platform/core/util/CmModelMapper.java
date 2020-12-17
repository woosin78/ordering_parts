package org.jwebppy.platform.core.util;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class CmModelMapper extends ModelMapper
{
	public CmModelMapper()
	{
		super();

		getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	public <D, S> List<D> mapAll(List<S> source, Class<D> destinationType)
	{
		if (source != null)
		{
			List<D> destination = new ArrayList<>();

			for (S element : source)
			{
				destination.add(map(element, destinationType));
			}

			return destination;
		}

		return null;
	}
}
