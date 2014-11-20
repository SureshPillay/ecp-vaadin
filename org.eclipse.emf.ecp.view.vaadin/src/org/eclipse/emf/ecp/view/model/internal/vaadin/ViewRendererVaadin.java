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

package org.eclipse.emf.ecp.view.model.internal.vaadin;

import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewComponent;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.model.vaadin.ViewLayoutProvider;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Vaadin Renderer for {@link VView}.
 *
 * @author Dennis Melzer
 *
 */
public class ViewRendererVaadin extends AbstractVaadinRenderer<VView> {

	private static final String BORDERLESS = "borderless"; //$NON-NLS-1$

	/**
	 * Default Constructor.
	 */
	public ViewRendererVaadin() {
		super();
	}

	/**
	 * Constructor for testing purpose.
	 *
	 * @param factory the factory to use
	 */
	protected ViewRendererVaadin(VaadinRendererFactory factory) {
		super(factory);
	}

	@Override
	public Component render() {
		final AbstractOrderedLayout layout = getLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setSizeFull();
		for (final VContainedElement composite : getVElement().getChildren()) {
			final Component renderResult = getRendererFactory().render(composite, getViewModelContext());
			layout.addComponent(renderResult);

		}
		final ECPVaadinViewComponent ecpVaadinViewComponent = new ECPVaadinViewComponent();
		ecpVaadinViewComponent.addStyleName(BORDERLESS);
		ecpVaadinViewComponent.setContent(layout);
		return ecpVaadinViewComponent;
	}

	private AbstractOrderedLayout getLayout() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
		if (bundleContext == null) {
			return new VerticalLayout();
		}

		final ServiceReference<ViewLayoutProvider> reference = bundleContext
			.getServiceReference(ViewLayoutProvider.class);
		if (reference == null) {
			return new VerticalLayout();
		}

		final ViewLayoutProvider service = bundleContext.getService(reference);
		return service == null ? new VerticalLayout() : service.getViewLayout();
	}

}
