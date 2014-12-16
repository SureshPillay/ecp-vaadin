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
package org.eclipse.emf.ecp.view.custom.vaadin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewComponent;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.core.vaadin.test.VaadinTestHelper;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomControl;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomDomainModelReference;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomFactory;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;

@RunWith(VaadinDatabindingClassRunner.class)
public class CustomControlVaadinRendererTest {

	public class TestHandel {

		private final VView view;

		private final VControl customControl;

		/**
		 * @param view
		 * @param customControl
		 */

		public TestHandel(VView view, VControl customControl) {
			this.view = view;
			this.customControl = customControl;
		}

		/**
		 * @return the view
		 */
		public VView getView() {
			return view;
		}

		/**
		 * @return the customControl
		 */

		public VControl getCustomControl() {
			return customControl;
		}

	}

	private static final String BUNDLE_ID = "org.eclipse.emf.ecp.view.common.vaadin.test";
	private final VaadinRendererFactory rendererFactory = VaadinTestHelper.getVaadinRendererFactory();
	private EObject domainElement;

	private VView createCustomControlInView() {
		final VView view = VViewFactory.eINSTANCE.createView();

		final VCustomControl customControl = createCustomControl();

		view.getChildren().add(customControl);
		customControl.setBundleName(BUNDLE_ID);
		customControl.setClassName("org.eclipse.emf.ecp.view.common.vaadin.test.CustomControlStub");
		// TODO check id
		final VCustomDomainModelReference domainModelReference = VCustomFactory.eINSTANCE
			.createCustomDomainModelReference();
		domainModelReference.setBundleName(BUNDLE_ID);
		domainModelReference.setClassName("org.eclipse.emf.ecp.view.common.vaadin.test.CustomControlStub");
		customControl.setDomainModelReference(domainModelReference);
		return view;
	}

	@Before
	public void init() {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.getESuperTypes().add(EcorePackage.eINSTANCE.getEClass());
		domainElement = eClass;
	}

	@Test
	public void testCustomControlinViewWithoutClass() {
		final VView view = VViewFactory.eINSTANCE.createView();

		final VCustomControl customControl = createCustomControl();

		view.getChildren().add(customControl);
		customControl.setBundleName(BUNDLE_ID);
		customControl.setClassName("org.eclipse.emf.ecp.view.custom.vaadin.NoExisting");
		// TODO check id
		final VCustomDomainModelReference domainModelReference = VCustomFactory.eINSTANCE
			.createCustomDomainModelReference();
		domainModelReference.setBundleName(BUNDLE_ID);
		domainModelReference.setClassName("org.eclipse.emf.ecp.view.custom.vaadin.NoExisting");
		customControl.setDomainModelReference(domainModelReference);

		final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE.createViewModelContext(view,
			domainElement);
		try {
			rendererFactory.render(view, viewContext);
			fail();
		} catch (final IllegalStateException e) {
		}
	}

	@Test
	public void testCustomControlInit() {
		final VView view = createCustomControlInView();
		final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE
			.createViewModelContext(view, domainElement);

		final ECPVaadinViewComponent component = (ECPVaadinViewComponent) rendererFactory.render(view, viewContext);
		final AbstractOrderedLayout layout = (AbstractOrderedLayout) component.getContent();
		assertEquals(1, layout.getComponentCount());
		assertTrue(Button.class.isInstance(layout.getComponent(0)));
	}

	private VCustomControl createCustomControl() {
		return VCustomFactory.eINSTANCE.createCustomControl();

	}
}
