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

import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

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

}
