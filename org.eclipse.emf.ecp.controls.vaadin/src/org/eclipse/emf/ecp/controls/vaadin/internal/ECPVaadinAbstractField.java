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

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.view.model.vaadin.validator.ECPVaadinEmptyTextValidator;
import org.eclipse.emf.ecp.view.spi.model.VControl;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

public abstract class ECPVaadinAbstractField extends ECPControlFactoryVaadin {

	@Override
	public Component createControl(VControl control, Setting setting) {
		AbstractField<?> abstractField = createFieldControl(control, setting);
		if (setting.getEStructuralFeature().getLowerBound() > 0 && !control.isReadonly()) {
			componentValidator = new ECPVaadinEmptyTextValidator(abstractField);
			abstractField.addValidator(componentValidator);
		}
		return abstractField;
	}

	public abstract AbstractField<?> createFieldControl(VControl control, Setting setting);

}
