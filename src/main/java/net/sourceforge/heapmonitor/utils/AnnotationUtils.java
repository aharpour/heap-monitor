package net.sourceforge.heapmonitor.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class AnnotationUtils {

	private AnnotationUtils() {
	}

	public static <T extends Annotation> T getPropertyAnnotation(PropertyDescriptor propertyDescriptor,
			Class<?> beanClass, Class<T> annotationClass) {
		T result = null;
		try {
			Field field = beanClass.getDeclaredField(propertyDescriptor.getName());
			result = field.getAnnotation(annotationClass);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		Method readMethod = propertyDescriptor.getReadMethod();
		if ((result == null) && (readMethod != null)) {
			result = readMethod.getAnnotation(annotationClass);
		}
		Method writeMethod = propertyDescriptor.getWriteMethod();
		if ((result == null) && (writeMethod != null)) {
			result = writeMethod.getAnnotation(annotationClass);
		}
		return result;
	}
}
