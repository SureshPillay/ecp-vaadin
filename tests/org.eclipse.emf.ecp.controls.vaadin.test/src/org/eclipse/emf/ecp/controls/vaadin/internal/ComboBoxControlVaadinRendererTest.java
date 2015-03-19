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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.Nationality;
import test.TestFactory;
import test.TestPackage;
import test.User;

import com.vaadin.ui.ComboBox;

@RunWith(VaadinDatabindingClassRunner.class)
public class ComboBoxControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		setup(new EnumComboViewerVaadinRenderer());
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Nationality");
	}

	@Override
	protected void mockControl() {
		final User eObject = TestFactory.eINSTANCE.createUser();
		final EStructuralFeature eStructuralFeature = TestPackage.eINSTANCE.getUser_Nationality();

		eObject.setNationality(Nationality.GERMAN);
		super.mockControl(eObject, eStructuralFeature);
	}

	@Test
	public void testDatabining() {
		mockControl();
		final ComboBox comboBox = (ComboBox) renderControl();
		assertEquals(Nationality.GERMAN, comboBox.getValue());
		comboBox.select(Nationality.FRENCH);
		assertEquals(Nationality.FRENCH, comboBox.getValue());
		final User user = (User) context.getDomainModel();
		assertEquals(Nationality.FRENCH, user.getNationality());
		user.setNationality(Nationality.RUSSIAN);
		assertEquals(Nationality.RUSSIAN, user.getNationality());
	}

	@Override
	protected Class<?> getComponentClass() {
		return ComboBox.class;
	}

}
