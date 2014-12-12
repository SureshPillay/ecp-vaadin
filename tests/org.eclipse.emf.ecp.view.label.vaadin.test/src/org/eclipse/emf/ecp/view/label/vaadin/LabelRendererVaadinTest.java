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
package org.eclipse.emf.ecp.view.label.vaadin;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.core.vaadin.test.VaadinTestHelper;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.label.model.VLabel;
import org.eclipse.emf.ecp.view.spi.label.model.VLabelFactory;
import org.eclipse.emf.ecp.view.spi.label.model.VLabelStyle;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vaadin.ui.Label;

@RunWith(VaadinDatabindingClassRunner.class)
public class LabelRendererVaadinTest {
	private VaadinRendererFactory rendererFactory = VaadinTestHelper.getVaadinRendererFactory();
	private static final String LABEL_NAME = "label";
	private EObject domainElement;

	@Before
	public void init() {
		EClass eObject = EcoreFactory.eINSTANCE.createEClass();
		eObject.setInstanceClassName("Test");
		this.domainElement = eObject;
	}

	private static VLabel createLabelWithText() {
		final VLabel vLabel = VLabelFactory.eINSTANCE.createLabel();
		vLabel.setName(LABEL_NAME);
		vLabel.setStyle(VLabelStyle.H0);
		return vLabel;
	}

	private static VLabel createLabelSeperator() {
		final VLabel vLabel = createLabelWithText();
		vLabel.setStyle(VLabelStyle.SEPARATOR);
		return vLabel;
	}

	private VLabel createLabel() {
		final VLabel control = createLabelWithText();
		mockControl(this.domainElement, EcorePackage.eINSTANCE.getEClassifier_InstanceClassName(), control);
		return control;
	}

	@Test
	public void testLabelWithText() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		VLabel vLabel = createLabelWithText();
		final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE.createViewModelContext(vLabel,
				this.domainElement);
		Label label = (Label) this.rendererFactory.render(vLabel, viewContext);
		assertEquals(LABEL_NAME, label.getValue());
	}

	@Test
	public void testLabelWithSeperator() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		VLabel vLabel = createLabelSeperator();
		final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE.createViewModelContext(vLabel,
				this.domainElement);
		Label label = (Label) this.rendererFactory.render(vLabel, viewContext);
		assertEquals("<hr/>", label.getValue());
	}

	@Test
	public void testLabelWithDomainRef() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		VLabel vLabel = createLabel();
		final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE.createViewModelContext(vLabel,
				this.domainElement);
		Label label = (Label) this.rendererFactory.render(vLabel, viewContext);
		assertEquals("Test", label.getValue());
	}

	protected void mockControl(EObject eObject, final EStructuralFeature eStructuralFeature, VLabel control) {
		VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(eStructuralFeature);
		control.setDomainModelReference(domainModelReference);
	}

}
