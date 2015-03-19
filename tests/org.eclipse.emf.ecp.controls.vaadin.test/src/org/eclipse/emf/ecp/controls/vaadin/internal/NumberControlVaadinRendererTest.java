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

import static org.junit.Assert.assertNull;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.TestFactory;
import test.TestPackage;
import test.User;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

@RunWith(VaadinDatabindingClassRunner.class)
public class NumberControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		setup(new NumberControlVaadinRenderer());
	}

	@Test
	public void renderControlLabelAlignmentNone() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.NONE);
		final Component render = renderControl();
		assertControl(render);
	}

	@Test
	public void renderControlLabelAlignmentLeft() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		final Component render = renderControl();

		assertControl(render);
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Heigth");
	}

	@Override
	protected Component assertControl(Component render) {
		final TextField field = (TextField) super.assertControl(render);
		assertNull(field.getConverter());
		return field;
	}

	@Override
	protected void assertComponentValue(Component renderComponent, Object value) {
		super.assertComponentValue(renderComponent, value.toString());
	}

	@Override
	protected void mockControl() {
		final User eObject = TestFactory.eINSTANCE.createUser();
		final EStructuralFeature eStructuralFeature = TestPackage.eINSTANCE.getUser_Heigth();
		eObject.setHeigth(3);
		super.mockControl(eObject, eStructuralFeature);
	}

	@Override
	protected Class<?> getComponentClass() {
		return TextField.class;
	}

}
