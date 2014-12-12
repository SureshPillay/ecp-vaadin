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
import org.eclipse.emf.ecore.EObject;

/**
 * The Conveter for a Vaadin Selection e.g Checkbox.
 *
 * @author Dennis Melzer
 *
 */
public class SelectionConverter implements IConverter {

	private final boolean selectionEnable;

	/**
	 * Default Constructor.
	 */
	public SelectionConverter() {
		this(true);
	}

	/**
	 * Constructor.
	 * 
	 * @param selectionEnable from Objec null or not
	 */
	public SelectionConverter(boolean selectionEnable) {
		this.selectionEnable = selectionEnable;
	}

	@Override
	public Object getToType() {
		return Boolean.class;
	}

	@Override
	public Object getFromType() {
		return EObject.class;
	}

	@Override
	public Object convert(Object fromObject) {
		if (selectionEnable) {
			return fromObject != null;
		}

		return fromObject == null;
	}

}
