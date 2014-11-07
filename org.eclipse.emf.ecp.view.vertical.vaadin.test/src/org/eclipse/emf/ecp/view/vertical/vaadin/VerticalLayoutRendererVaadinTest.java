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
package org.eclipse.emf.ecp.view.vertical.vaadin;

import org.eclipse.emf.ecp.view.common.vaadin.test.AbstractLayoutRendererVaadinTest;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.vertical.model.VVerticalFactory;
import org.eclipse.emf.ecp.view.spi.vertical.model.VVerticalLayout;
import org.junit.runner.RunWith;

import com.vaadin.ui.VerticalLayout;

@RunWith(VaadinDatabindingClassRunner.class)
public class VerticalLayoutRendererVaadinTest extends AbstractLayoutRendererVaadinTest {

	@Override
	protected VVerticalLayout createLayout() {
		return VVerticalFactory.eINSTANCE.createVerticalLayout();
	}

	@Override
	protected Class<VerticalLayout> getUILayouClass() {
		return VerticalLayout.class;
	}
}
