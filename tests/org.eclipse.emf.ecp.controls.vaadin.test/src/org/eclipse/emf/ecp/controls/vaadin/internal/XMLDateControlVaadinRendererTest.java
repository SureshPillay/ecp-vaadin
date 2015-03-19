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
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
public class XMLDateControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		setup(new XMLDateControlVaadinRenderer());
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Date Of Birth");
	}

	@Override
	protected void mockControl() {
		final User eObject = TestFactory.eINSTANCE.createUser();
		final EStructuralFeature eStructuralFeature = TestPackage.eINSTANCE.getUser_DateOfBirth();
		eObject.setDateOfBirth(createXMLGregorianCalender("21/12/2012"));
		super.mockControl(eObject, eStructuralFeature);
	}

	private XMLGregorianCalendar createXMLGregorianCalender(String date) {
		try {
			final GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(createDate(date));
			final XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(
				gregorianCalendar);
			return xmlGregorianCalendar;
		} catch (final DatatypeConfigurationException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Test
	public void testDatabining() {
		mockControl();
		final DateField dateField = (DateField) renderControl();
		final User user = (User) context.getDomainModel();
		final Date date = createDate("22/12/2015");
		dateField.setValue(date);
		assertEquals(simpleDateFormat.format(date),
			simpleDateFormat.format(user.getDateOfBirth().toGregorianCalendar().getTime()));
		final XMLGregorianCalendar xmlGregorianCalendar = createXMLGregorianCalender("23/12/2015");
		user.setDateOfBirth(xmlGregorianCalendar);
		assertEquals(simpleDateFormat.format(xmlGregorianCalendar.toGregorianCalendar().getTime()),
			simpleDateFormat.format(dateField.getValue()));
	}

	@Override
	protected Class<?> getComponentClass() {
		return DateField.class;
	}

}
