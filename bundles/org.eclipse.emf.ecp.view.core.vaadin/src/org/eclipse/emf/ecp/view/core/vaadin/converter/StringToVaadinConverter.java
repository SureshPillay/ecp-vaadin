/*******************************************************************************
 * Copyright (c) 2014 Dennis Melzer and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dennis - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.core.vaadin.converter;

import org.eclipse.core.databinding.conversion.IConverter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.UI;

/**
 * String to Object Converter for a Vaadin Converter.
 *
 * @author Dennis Melzer
 *
 */
public class StringToVaadinConverter implements IConverter {

	private final Converter<String, Object> converter;

	/**
	 * Constructor.
	 *
	 * @param converter the vaadin converter
	 */
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

		if (fromObject == null)
		{
			return null;
		}

		try {
			return converter.convertToModel((String) fromObject, converter.getModelType(),
				UI.getCurrent().getLocale());
		} catch (final ConversionException e) {
			return null;
		}

	}
}
