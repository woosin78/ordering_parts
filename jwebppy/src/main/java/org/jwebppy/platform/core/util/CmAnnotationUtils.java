package org.jwebppy.platform.core.util;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotationUtils;

public class CmAnnotationUtils extends AnnotationUtils
{
	public static String getAnnotationValue(Annotation annotation)
	{
        Object value = CmAnnotationUtils.getValue(annotation, "value");

        if (value != null)
        {
            String[] values = (String[])value;

            if (values.length > 0)
            {
                return values[0];
            }
        }

        return null;
	}
}
