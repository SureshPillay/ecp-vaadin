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
import org.eclipse.emf.ecp.controls.vaadin.ECPTextFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.vaadin.ECPTextFieldToTargetUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.vaadin.VaadinSimpleControlRenderer;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class TextControlVaadinRenderer extends VaadinSimpleControlRenderer {

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(Component component) {
		return new ECPTextFieldToTargetUpdateValueStrategy();
	}

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(Component component) {
		return new ECPTextFieldToModelUpdateValueStrategy();
	}

	@Override
	protected Component createControl() {
		Setting setting = getVElement().getDomainModelReference().getIterator().next();
		IItemPropertyDescriptor itemPropertyDescriptor = getItemPropertyDescriptor(setting);
		if (itemPropertyDescriptor.isMultiLine(null)) {
			TextArea textArea = new TextArea();
			textArea.setNullRepresentation("");
			return textArea;
		}
		TextField textField = new TextField();
		textField.setNullRepresentation("");
		return textField;
	}

}