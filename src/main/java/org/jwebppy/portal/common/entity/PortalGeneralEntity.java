package org.jwebppy.portal.common.entity;

import java.io.Serializable;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class PortalGeneralEntity extends GeneralEntity implements Serializable
{
	private static final long serialVersionUID = -3378770967914954346L;
	protected String corp;
}
