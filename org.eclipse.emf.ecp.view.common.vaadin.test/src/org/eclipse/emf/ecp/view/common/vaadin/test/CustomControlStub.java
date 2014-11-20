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
package org.eclipse.emf.ecp.view.common.vaadin.test;

import org.eclipse.emf.ecp.view.custom.vaadin.CustomControlVaadinRenderer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

public class CustomControlStub extends CustomControlVaadinRenderer {

	private final String LABEL_TEXT = "labelText";
	private Button label;

	@Override
	protected Component render() {
		label = new Button(LABEL_TEXT);
		return label;
	}

}
