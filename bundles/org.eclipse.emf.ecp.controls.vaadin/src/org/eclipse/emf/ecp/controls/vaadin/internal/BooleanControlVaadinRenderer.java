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
import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * The Vaadin Renderer for a boolean value.
 *
 * @author Dennis Melzer
 *
 */
public class BooleanControlVaadinRenderer extends AbstractVaadinSimpleControlRenderer {

	@Override
	public Component createControl() {
		return new CheckBox();
	}

	@Override
	protected Component render() {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		final Component checkBox = createControl();
		horizontalLayout.setData(checkBox);
		createDatabinding(setting, checkBox);
		checkBox.setWidth(100, Unit.PERCENTAGE);
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		horizontalLayout.addComponent(checkBox);
		horizontalLayout.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
		// TODO: Fix Size
		horizontalLayout.addStyleName("textheight");
		if (setting.getEStructuralFeature().isUnsettable()) {
			createSetOrUnsetComponent(checkBox, horizontalLayout, setting);
		}
		return horizontalLayout;
	}

	@Override
	protected void createSetOrUnsetComponent(Component component, HorizontalLayout horizontalLayout, Setting setting) {
		super.createSetOrUnsetComponent(component, horizontalLayout, setting);
		final Component componentSet = (Component) horizontalLayout.getData();
		horizontalLayout.setComponentAlignment(componentSet, Alignment.MIDDLE_LEFT);
	}

	@Override
	protected String getUnsetLabel() {
		return VaadinRendererMessages.BooleanControlVaadinRenderer_NoBooleanSetClickToSetBoolean;
	}

}
