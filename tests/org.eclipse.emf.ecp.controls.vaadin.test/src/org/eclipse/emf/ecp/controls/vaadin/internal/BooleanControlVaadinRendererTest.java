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

import static org.junit.Assert.assertFalse;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

@RunWith(VaadinDatabindingClassRunner.class)
public class BooleanControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		// VaadinRendererFactory factory = mock(VaadinRendererFactory.class);
		setup(new BooleanControlVaadinRenderer());
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Interface");
	}

	@Override
	protected Component assertControl(Component render) {
		final CheckBox checkBox = (CheckBox) super.assertControl(render);
		assertFalse(checkBox.getValue());
		return checkBox;
	}

	@Override
	protected void mockControl() {
		final EClass eObject = EcoreFactory.eINSTANCE.createEClass();
		final EStructuralFeature eStructuralFeature = EcorePackage.eINSTANCE.getEClass_Interface();
		super.mockControl(eObject, eStructuralFeature);
	}

	@Override
	protected Class<?> getComponentClass() {
		return CheckBox.class;
	}

}
