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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.controls.vaadin.ECPXMLDateFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;

public class ECPVaadinXMLDate extends ECPControlFactoryVaadin {

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(final VControl control, Component component) {
		return new EMFUpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				if (value == null) {
					return null;
				}
				DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

				final XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) value;
				final Date date = gregorianCalendar.toGregorianCalendar().getTime();

				return dateInstance.format(date);
			}
		};
	}

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(VControl control, Component component) {
		return new ECPXMLDateFieldToModelUpdateValueStrategy();
	}

	@Override
	public Component createControl(VControl control, ViewModelContext viewContext, Setting setting) {
		return new DateField();
	}

}
