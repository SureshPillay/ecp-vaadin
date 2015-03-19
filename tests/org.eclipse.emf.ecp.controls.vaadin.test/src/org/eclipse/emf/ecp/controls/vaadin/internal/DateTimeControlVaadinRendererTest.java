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

import java.util.Date;

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

import com.vaadin.ui.DateField;

@RunWith(VaadinDatabindingClassRunner.class)
public class DateTimeControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		setup(new DateTimeControlVaadinRenderer());
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Time Of Registration");
	}

	@Override
	protected void mockControl() {
		final User eObject = TestFactory.eINSTANCE.createUser();
		final EStructuralFeature eStructuralFeature = TestPackage.eINSTANCE.getUser_TimeOfRegistration();

		eObject.setTimeOfRegistration(createDate("21/12/2012"));
		super.mockControl(eObject, eStructuralFeature);
	}

	@Test
	public void testDatabining() {
		mockControl();
		final DateField dateField = (DateField) renderControl();
		final User user = (User) context.getDomainModel();
		Date date = createDate("22/12/2015");
		dateField.setValue(date);
		assertEquals(date, user.getTimeOfRegistration());
		date = createDate("23/12/2015");
		user.setTimeOfRegistration(date);
		assertEquals(date, dateField.getValue());
	}

	@Override
	protected Class<?> getComponentClass() {
		return DateField.class;
	}

}
