package org.eclipse.emf.ecp.view.core.vaadin.converter;

import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.Converter.ConversionException;

public class StringToVaadinConverter implements IConverter {

	private Converter<String, Object> converter;

	public StringToVaadinConverter(Converter<String, Object> converter) {
		this.converter = converter;
	}

	@Override
	public Object getFromType() {
		return String.class;
	}

	@Override
	public Object getToType() {
		return Object.class;
	}

	@Override
	public Object convert(Object fromObject) {
		if (fromObject == null) {
			return null;
		}

		try {
			return this.converter.convertToModel((String) fromObject, this.converter.getModelType(),
					Locale.getDefault());
		} catch (ConversionException e) {
			return null;
		}

	}
}
