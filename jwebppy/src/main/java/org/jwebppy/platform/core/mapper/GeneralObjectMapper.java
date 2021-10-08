package org.jwebppy.platform.core.mapper;

import java.util.List;

public interface GeneralObjectMapper <D, E>
{
	E toEntity(D dto);
	D toDto(E entity);
	List<E> toEntityList(List<D> dtoList);
	List<D> toDtoList(List<E> entityList);
}