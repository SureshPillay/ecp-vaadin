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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.common.vaadin.test.HierarchyViewModelHandle;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.core.vaadin.test.VaadinTestHelper;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.vertical.model.VVerticalFactory;
import org.eclipse.emf.ecp.view.spi.vertical.model.VVerticalLayout;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@RunWith(VaadinDatabindingClassRunner.class)
public class VerticalLayoutRendererVaadinTest {

	private EObject domainElement;

	@Before
	public void init() {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.getESuperTypes().add(EcorePackage.eINSTANCE.getEClass());
		eClass.setInstanceClassName("Test");
		this.domainElement = eClass;
	}

	@Test
	public void testVerticalWithoutChildren() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final HierarchyViewModelHandle handle = createVerticalWithoutChildren();
		final Component render = renderComponent(handle);
		assertTrue(render instanceof VerticalLayout);
		VerticalLayout layout = (VerticalLayout) render;
		assertEquals(0, layout.getComponentCount());
	}

	private Component renderComponent(final HierarchyViewModelHandle handle) {
		final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE.createViewModelContext(handle.getRoot(),
				this.domainElement);
		final Component render = VaadinTestHelper.getVaadinRendererFactory().render(handle.getRoot(), viewContext);
		return render;
	}

	@Test
	public void testVerticalWithTwoControlsAsChildren() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		// setup model
		final HierarchyViewModelHandle handle = createVerticalWithTwoControlsAsChildren();
		final Component render = renderComponent(handle);
		assertTrue(render instanceof VerticalLayout);
		VerticalLayout layout = (VerticalLayout) render;
		assertEquals(2, layout.getComponentCount());
		assertTrue(layout.getComponent(0) instanceof TextField);
		assertTrue(layout.getComponent(1) instanceof TextField);
	}

	@Test
	public void testVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		// setup model
		final HierarchyViewModelHandle handle = createVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren();
		final Component render = renderComponent(handle);
		assertTrue(render instanceof VerticalLayout);
		final VerticalLayout composite = (VerticalLayout) render;
		assertEquals(2, composite.getComponentCount());
		final VerticalLayout firstVertical = (VerticalLayout) composite.getComponent(0);
		final VerticalLayout secondVertical = (VerticalLayout) composite.getComponent(1);

		assertEquals(2, firstVertical.getComponentCount());
		assertEquals(2, secondVertical.getComponentCount());

		assertTrue(firstVertical.getComponent(0) instanceof TextField);
		assertTrue(firstVertical.getComponent(1) instanceof TextField);
		assertTrue(secondVertical.getComponent(0) instanceof TextField);
		assertTrue(secondVertical.getComponent(1) instanceof TextField);
	}

	private static HierarchyViewModelHandle createVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren() {
		final HierarchyViewModelHandle verticalHandle = createVerticalWithoutChildren();
		verticalHandle.addFirstChildToRoot(createVertical());
		verticalHandle.addSecondChildToRoot(createVertical());
		verticalHandle.addFirstChildToFirstChild(createControl());
		verticalHandle.addSecondChildToFirstChild(createControl());
		verticalHandle.addFirstChildToSecondChild(createControl());
		verticalHandle.addSecondChildToSecondChild(createControl());
		return verticalHandle;
	}

	private static HierarchyViewModelHandle createVerticalWithTwoControlsAsChildren() {
		final HierarchyViewModelHandle verticalHandle = createVerticalWithoutChildren();
		final VControl control1 = createControl();
		verticalHandle.addFirstChildToRoot(control1);
		final VControl control2 = createControl();
		verticalHandle.addSecondChildToRoot(control2);
		return verticalHandle;
	}

	private static VControl createControl() {
		final VControl control = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(EcorePackage.eINSTANCE.getEClassifier_InstanceClassName());
		control.setDomainModelReference(domainModelReference);
		return control;
	}

	private static HierarchyViewModelHandle createVerticalWithoutChildren() {
		final VElement vertical = createVertical();
		return new HierarchyViewModelHandle(vertical);
	}

	private static VVerticalLayout createVertical() {
		return VVerticalFactory.eINSTANCE.createVerticalLayout();
	}
}
