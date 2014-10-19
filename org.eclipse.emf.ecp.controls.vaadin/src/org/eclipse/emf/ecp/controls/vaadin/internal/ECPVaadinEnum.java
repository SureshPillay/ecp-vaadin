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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

public class ECPVaadinEnum extends ECPControlFactoryVaadin {

	@Override
	public Component createControl(VControl control, ViewModelContext viewContext, Setting setting) {
		ComboBox combobox = new ComboBox();
		final List<Object> inputValues = new ArrayList<Object>();
		for (final EEnumLiteral literal : EEnum.class.cast(setting.getEStructuralFeature().getEType()).getELiterals()) {
			inputValues.add(literal.getInstance());
		}

		combobox.setInvalidAllowed(false);
		combobox.setNullSelectionAllowed(false);
		combobox.addItems(inputValues);
		return combobox;
	}

}
