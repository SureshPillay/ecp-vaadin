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
import org.eclipse.emf.ecp.controls.vaadin.ECPTextFieldToTargetUpdateValueStrategy;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class ECPVaadinText extends ECPControlFactoryVaadin {

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(VControl control) {
		return new ECPTextFieldToTargetUpdateValueStrategy();
	}

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(VControl control) {
		return new ECPTextFieldToModelUpdateValueStrategy();
	}

	@Override
	public Component createControl(VControl control, Setting setting) {
		IItemPropertyDescriptor itemPropertyDescriptor = getItemPropertyDescriptor(setting);
		if (itemPropertyDescriptor.isMultiLine(null)) {
			return new TextArea();
		}
		return new TextField();
	}

}
