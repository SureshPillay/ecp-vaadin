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

import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.util.converter.AbstractStringToNumberConverter;

/**
 * String to Number converter.
 *
 * @author Dennis Melzer
 *
 */
public class VaadinStringToNumberConverter extends AbstractStringToNumberConverter<Number> {

	private final Class<?> clazz;

	/**
	 * Constructor.
	 *
	 * @param clazz the number class
	 */
	public VaadinStringToNumberConverter(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected NumberFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		final NumberFormat numberInstance = NumberFormat.getNumberInstance(locale);
		numberInstance.setGroupingUsed(false);
		return numberInstance;
	}

	@Override
	public Number convertToModel(String value, Class<? extends Number> targetType, Locale locale)
		throws ConversionException {
		final Number n = convertToNumber(value, targetType, locale);
		return n;
	}

	@Override
	public Class<Number> getModelType() {
		return (Class<Number>) clazz;
	}

}
