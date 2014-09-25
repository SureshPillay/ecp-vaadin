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

import org.apache.commons.lang3.ClassUtils;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.view.core.vaadin.converter.VaadinConverterToString;
import org.eclipse.emf.ecp.view.spi.model.VControl;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class ECPVaadinNumber extends ECPControlFactoryVaadin {

	@Override
	public Component createControl(VControl control, Setting setting) {
		final TextField component = new TextField();

		Class<?> instanceClass = setting.getEStructuralFeature().getEType().getInstanceClass();
		if (instanceClass.isPrimitive()) {
			instanceClass = ClassUtils.primitiveToWrapper(instanceClass);
		}
		component.setConverter(instanceClass);
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
	protected UpdateValueStrategy getModelToTargetStrategy(VControl control, Component component) {
		EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new VaadinConverterToString(((AbstractField<String>) component)
				.getConverter()));

		return emfUpdateValueStrategy;
	}

}
