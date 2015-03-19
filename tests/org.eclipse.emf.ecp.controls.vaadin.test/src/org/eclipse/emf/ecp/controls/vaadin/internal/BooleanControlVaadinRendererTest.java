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
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.TestFactory;
import test.TestPackage;
import test.User;

import com.vaadin.ui.CheckBox;

@RunWith(VaadinDatabindingClassRunner.class)
public class BooleanControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		// VaadinRendererFactory factory = mock(VaadinRendererFactory.class);
		setup(new BooleanControlVaadinRenderer());
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Active");
	}

	@Override
	protected void mockControl() {
		final User eObject = TestFactory.eINSTANCE.createUser();
		final EStructuralFeature eStructuralFeature = TestPackage.eINSTANCE.getUser_Active();
		eObject.eSet(eStructuralFeature, Boolean.TRUE);
		super.mockControl(eObject, eStructuralFeature);
	}

	@Test
	public void testDatabining() {
		mockControl();
		renderControl();
		final CheckBox checkBox = (CheckBox) renderer.getControlComponent();
		checkBox.setValue(Boolean.TRUE);
		final User user = (User) context.getDomainModel();
		assertTrue(user.isActive());
		user.setActive(false);
		assertFalse(user.isActive());
	}

	@Override
	protected Class<?> getComponentClass() {
		return CheckBox.class;
	}

}
