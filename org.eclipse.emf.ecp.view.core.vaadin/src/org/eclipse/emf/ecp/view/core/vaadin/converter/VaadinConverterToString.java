package org.eclipse.emf.ecp.view.core.vaadin.converter;

import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;

import com.vaadin.data.util.converter.Converter;

/**
 * Vaadin Converter to String.
 *
 * @author Dennis Melzer
 *
 */
public class VaadinConverterToString implements IConverter {

	private final Converter<String, Object> converter;

	/**
	 * Constructor.
	 * 
	 * @param converter the vaadin converter
	 */
	public VaadinConverterToString(Converter<String, Object> converter) {
		this.converter = converter;
	}

	@Override
	public Object getFromType() {
		return Object.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
		if (fromObject == null) {
			return null;
		}

		return converter.convertToPresentation(fromObject, converter.getPresentationType(),
			Locale.getDefault());
	}
}
