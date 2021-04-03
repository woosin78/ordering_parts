package org.jwebppy.portal;

import java.io.Serializable;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PortalGeneralEntity extends GeneralEntity implements Serializable
{
	private static final long serialVersionUID = -3378770967914954346L;
	private String corp;
}
