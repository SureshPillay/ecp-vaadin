package org.eclipse.emf.ecp.view.core.vaadin.converter;

import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractTextField;

/**
 * Vaadin Converter to String.
 *
 * @author Dennis Melzer
 *
 */
public class VaadinConverterToString implements IConverter {

	private final Converter<String, Object> converter;
	private final AbstractTextField component;

	/**
	 * Constructor.
	 *
	 * @param converter the vaadin converter
	 */
	public VaadinConverterToString(AbstractTextField component) {

		this.component = component;
		converter = component.getConverter();
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
			return component.getNullRepresentation();
		}

		return converter.convertToPresentation(fromObject, converter.getPresentationType(),
			Locale.getDefault());
	}
}
