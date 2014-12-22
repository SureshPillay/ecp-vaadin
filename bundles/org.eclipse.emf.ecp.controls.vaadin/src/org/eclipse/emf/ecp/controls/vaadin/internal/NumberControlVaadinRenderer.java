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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.core.vaadin.converter.VaadinConverterToString;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

/**
 * The Vaadin Renderer for a number value.
 *
 * @author Dennis Melzer
 *
 */
public class NumberControlVaadinRenderer extends AbstractVaadinSimpleControlRenderer {

	@Override
	public Component createControl() {
		final TextField component = new TextField();
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		VaadinRendererUtil.setConverterToTextField(setting.getEStructuralFeature(), component, getVElement(),
			getViewModelContext());
		component.setNullRepresentation(StringUtils.EMPTY);
		return component;
	}

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(Component component) {
		final EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new VaadinConverterToString((AbstractTextField) component));

		return emfUpdateValueStrategy;
	}

	@Override
	protected String getUnsetLabel() {
		return VaadinRendererMessages.NumberControlVaadinRenderer_NoNumberClickToSetNumber;
	}

}
