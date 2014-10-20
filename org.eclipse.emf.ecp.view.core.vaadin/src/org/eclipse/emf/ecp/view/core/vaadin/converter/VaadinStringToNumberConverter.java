package org.eclipse.emf.ecp.view.core.vaadin.converter;

import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.util.converter.AbstractStringToNumberConverter;

public class VaadinStringToNumberConverter extends AbstractStringToNumberConverter<Number> {

	private Class<?> clazz;

	public VaadinStringToNumberConverter(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected NumberFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		NumberFormat numberInstance = NumberFormat.getNumberInstance(locale);
		numberInstance.setGroupingUsed(false);
		return numberInstance;
	}

	@Override
	public Number convertToModel(String value, Class<? extends Number> targetType, Locale locale)
			throws ConversionException {
		Number n = convertToNumber(value, targetType, locale);
		return n;
	}

	@Override
	public Class<Number> getModelType() {
		return (Class<Number>) this.clazz;
	}

}
