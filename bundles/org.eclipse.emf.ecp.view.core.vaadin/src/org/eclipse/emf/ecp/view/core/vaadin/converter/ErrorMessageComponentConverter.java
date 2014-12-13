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

import com.vaadin.ui.AbstractField;

/**
 * The Conveter for a Vaadin Selection e.g Checkbox.
 *
 * @author Dennis Melzer
 *
 */
public class ErrorMessageComponentConverter implements IConverter {

	private final AbstractField<?> abstractField;

	/**
	 * Constructor.
	 *
	 * @param abstractField the textfield for the error message
	 */
	public ErrorMessageComponentConverter(AbstractField<?> abstractField) {
		this.abstractField = abstractField;
	}

	@Override
	public Object getToType() {
		return Boolean.class;
	}

	@Override
	public Object getFromType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
		return abstractField.getErrorMessage() == null;
	}

}
