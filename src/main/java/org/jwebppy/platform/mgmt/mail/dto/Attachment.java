package org.jwebppy.platform.mgmt.mail.dto;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Attachment
{
	private String name;
	private File file;
}
