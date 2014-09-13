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
package org.eclipse.emf.ecp.view.model.internal.vaadin;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;

@StyleSheet({ "default.css" })
public class ECPVaadinViewComponent extends CustomComponent {

	public ECPVaadinViewComponent(Layout layout) {
		setCompositionRoot(layout);
	}

	@Override
	public Layout getCompositionRoot() {
		return (Layout) super.getCompositionRoot();
	}
}
