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
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import test.TestFactory;
import test.TestPackage;
import test.User;

import com.vaadin.data.util.converter.DefaultConverterFactory;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

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
	protected void assertComponentValue(Component renderComponent, Object value) {
		super.assertComponentValue(renderComponent, value.toString());
	}

	@Override
	protected void mockControl() {
		final User eObject = TestFactory.eINSTANCE.createUser();
		final EStructuralFeature eStructuralFeature = TestPackage.eINSTANCE.getUser_Heigth();
		eObject.setHeigth(3);
		final VaadinSession vaadinSession = Mockito.mock(VaadinSession.class);
		Mockito.when(vaadinSession.getConverterFactory()).thenReturn(new DefaultConverterFactory());
		VaadinSession.setCurrent(vaadinSession);
		final UI ui = Mockito.mock(UI.class);
		Mockito.when(ui.getLocale()).thenReturn(Locale.getDefault());
		UI.setCurrent(ui);
		super.mockControl(eObject, eStructuralFeature);
	}

	@Test
	public void testDatabining() {
		mockControl();
		final TextField textField = (TextField) renderControl();
		textField.setValue("5");
		final User user = (User) context.getDomainModel();
		assertEquals(5, user.getHeigth());
		user.setHeigth(8);
		assertEquals(8, textField.getConvertedValue());
		assertEquals("" + 8, textField.getValue());
		System.out.println(textField.getValue());
		textField.setValue("5ABC");
		assertNotNull(textField.getConversionError());

	}

	@Override
	protected Class<?> getComponentClass() {
		return TextField.class;
	}

}
