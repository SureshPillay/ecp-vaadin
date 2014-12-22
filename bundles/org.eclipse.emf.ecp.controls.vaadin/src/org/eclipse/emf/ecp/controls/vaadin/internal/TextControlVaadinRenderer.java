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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;
import org.eclipse.emf.ecp.controls.vaadin.ECPTextFieldToModelUpdateValueStrategy;
import org.eclipse.emf.ecp.controls.vaadin.ECPTextFieldToTargetUpdateValueStrategy;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * The Vaadin Renderer for a string value.
 *
 * @author Dennis Melzer
 *
 */
public class TextControlVaadinRenderer extends AbstractVaadinSimpleControlRenderer {

	@Override
	protected UpdateValueStrategy getTargetToModelStrategy(Component component, Setting setting) {
		return new ECPTextFieldToTargetUpdateValueStrategy();
	}

	@Override
	protected UpdateValueStrategy getModelToTargetStrategy(Component component) {
		return new ECPTextFieldToModelUpdateValueStrategy();
	}

	@Override
	protected Component createControl() {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = getItemPropertyDescriptor(setting);
		if (itemPropertyDescriptor.isMultiLine(null)) {
			final TextArea textArea = new TextArea();
			textArea.setNullRepresentation(StringUtils.EMPTY);
			return textArea;
		}
		final TextField textField = new TextField();
		textField.setNullRepresentation(StringUtils.EMPTY);
		return textField;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer#getUnsetLabel()
	 */
	@Override
	protected String getUnsetLabel() {
		return VaadinRendererMessages.TextControlVaadinRenderer_NoTextSetClickToSetText;
	}

}
