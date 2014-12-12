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
package org.eclipse.emf.ecp.controls.vaadin.internal;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;
import org.eclipse.emf.ecp.controls.vaadin.ECPXMLDateFieldToModelUpdateValueStrategy;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;

/**
 * The Vaadin Renderer for a xml date value.
 *
 * @author Dennis Melzer
 *
 */
public class XMLDateControlVaadinRenderer extends AbstractVaadinSimpleControlRenderer {

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(Component component) {
		return new EMFUpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				if (value == null) {
					return null;
				}
				final DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

				final XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) value;
				final Date date = gregorianCalendar.toGregorianCalendar().getTime();

				return dateInstance.format(date);
			}
		};
	}

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(Component component) {
		return new ECPXMLDateFieldToModelUpdateValueStrategy();
	}

	@Override
	public Component createControl() {
		return new DateField();
	}

}
