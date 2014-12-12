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
import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

/**
 * The Vaadin Renderer for a enum value.
 *
 * @author Dennis Melzer
 *
 */
public class EnumComboViewerVaadinRenderer extends AbstractVaadinSimpleControlRenderer {

	@Override
	public Component createControl() {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final ComboBox combobox = new ComboBox();
		final List<Object> inputValues = new ArrayList<Object>();
		for (final EEnumLiteral literal : EEnum.class.cast(setting.getEStructuralFeature().getEType())
			.getELiterals()) {
			inputValues.add(literal.getInstance());
		}

		combobox.setInvalidAllowed(false);
		combobox.setNullSelectionAllowed(false);
		combobox.addItems(inputValues);
		return combobox;
	}

	@Override
	protected String getUnsetLabel() {
		return VaadinRendererMessages.EnumComboViewerVaadinRenderer_NoValueSetClickToSetValue;
	}

}
