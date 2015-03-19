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

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vaadin.ui.TextField;

@RunWith(VaadinDatabindingClassRunner.class)
public class TextControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		setup(new TextControlVaadinRenderer());
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Name");
	}

	@Override
	protected void mockControl() {
		final EStructuralFeature eObject = EcoreFactory.eINSTANCE.createEAttribute();
		final EStructuralFeature eStructuralFeature = EcorePackage.eINSTANCE.getENamedElement_Name();
		eObject.eSet(eStructuralFeature, "Hallo");
		super.mockControl(eObject, eStructuralFeature);
	}

	@Test
	public void testDatabining() {
		mockControl();
		final TextField textField = (TextField) renderControl();
		textField.setValue("5");
		final EAttribute user = (EAttribute) context.getDomainModel();
		assertEquals("" + 5, user.getName());
		user.setName("" + 8);
		assertEquals("" + 8, textField.getValue());

		textField.setValue("5ABC");
		assertEquals("5ABC", textField.getValue());
		assertEquals("5ABC", user.getName());

	}

	@Override
	protected Class<?> getComponentClass() {
		return TextField.class;
	}

}
