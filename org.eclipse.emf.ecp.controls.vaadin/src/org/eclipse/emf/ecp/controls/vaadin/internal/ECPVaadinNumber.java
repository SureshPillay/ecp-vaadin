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

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.controls.vaadin.ECPTextFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.view.spi.model.VControl;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class ECPVaadinNumber extends ECPControlFactoryVaadin {

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(VControl control) {
		return new ECPTextFieldToModelUpdateValueStrategy();
	}

	@Override
	public Component createControl(VControl control, Setting setting) {
		final TextField component = new TextField();
		component.setConverter(setting.getEStructuralFeature().getEType().getInstanceClass());
		return component;
	}

}
