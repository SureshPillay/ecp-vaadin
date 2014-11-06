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
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.VaadinSimpleControlRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.core.vaadin.converter.VaadinConverterToString;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class NumberControlVaadinRenderer extends VaadinSimpleControlRenderer {

	@Override
	public Component createControl() {
		final TextField component = new TextField();
		Setting setting = getVElement().getDomainModelReference().getIterator().next();
		VaadinRendererUtil.setConverterToTextField(setting.getEStructuralFeature(), component, getVElement(),
				getViewModelContext());
		component.setNullRepresentation("");
		return component;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// protected UpdateValueStrategy getTargetToModelStrategy(VControl control, Component component) {
	// EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
	// emfUpdateValueStrategy.setConverter(new StringToVaadinConverter(((AbstractField<String>) component)
	// .getConverter()));
	//
	// return emfUpdateValueStrategy;
	// }

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(Component component) {
		EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new VaadinConverterToString(((AbstractField<String>) component)
				.getConverter()));

		return emfUpdateValueStrategy;
	}

}
