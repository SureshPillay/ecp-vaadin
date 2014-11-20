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
package org.eclipse.emf.ecp.view.horizontal.vaadin;

import org.eclipse.emf.ecp.view.common.vaadin.test.AbstractLayoutRendererVaadinTest;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalFactory;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalLayout;

import com.vaadin.ui.HorizontalLayout;

public class HorizontalLayoutRendererVaadinTest extends AbstractLayoutRendererVaadinTest {

	@Override
	protected VHorizontalLayout createLayout() {
		return VHorizontalFactory.eINSTANCE.createHorizontalLayout();
	}

	@Override
	protected Class<HorizontalLayout> getUILayouClass() {
		return HorizontalLayout.class;
	}

}
