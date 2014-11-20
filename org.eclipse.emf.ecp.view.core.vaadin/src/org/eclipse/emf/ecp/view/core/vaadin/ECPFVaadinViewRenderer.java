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
package org.eclipse.emf.ecp.view.core.vaadin;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.core.vaadin.internal.ECPFVaadinViewRendererImpl;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VView;

/**
 * Renders a view which displays the attributes of an domain objects.
 *
 * @author Dennis Melzer
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ECPFVaadinViewRenderer {

	/**
	 * The {@link ECPFVaadinViewRendererImpl} instance.
	 */
	ECPFVaadinViewRenderer INSTANCE = new ECPFVaadinViewRendererImpl();

	/**
	 * Creates a view with the attributes of the domain object. The layout of the view can either be describes by a
	 * registered view model, or, if none view model is registered for the domainObject, will be the default layout.
	 *
	 * @param domainObject The domainObject to show in the view
	 * @return an ECPVaadinView providing an interface to the rendered view
	 */
	ECPVaadinView render(EObject domainObject);

	/**
	 * Creates a view with the attributes of the domain object. The layout of the view is specified by the given view
	 * model.
	 *
	 * @param domainObject The domainObject to show in the view
	 * @param viewModel the view model describing the layout of the view
	 * @return an ECPVaadinView providing an interface to the rendered view
	 */
	ECPVaadinView render(EObject domainObject, VView viewModel);

	/**
	 * Creates a view with the attributes of the domain object. The layout of the view is specified by the view
	 * model set in the view model context.
	 *
	 * @param viewModelContext the {@link ViewModelContext} to use
	 * @return an ECPVaadinView providing an interface to the rendered view
	 */
	ECPVaadinView render(ViewModelContext viewModelContext);

}
