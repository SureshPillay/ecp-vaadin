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
package org.eclipse.emf.ecp.view.core.vaadin.internal;

import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;

import com.vaadin.ui.Component;

/**
 * The implementation {@link ECPVaadinView}.
 *
 * @author Dennis Melzer
 *
 */
public class ECPVaadinViewImpl implements ECPVaadinView {

	private final Component component;
	private final ViewModelContext viewModelContext;

	/**
	 * Constructor.
	 *
	 * @param component the vaadin component
	 * @param viewModelContext the context
	 */
	public ECPVaadinViewImpl(Component component, ViewModelContext viewModelContext) {
		this.component = component;
		this.viewModelContext = viewModelContext;
	}

	@Override
	public Component getComponent() {
		return component;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public ViewModelContext getViewModelContext() {
		return viewModelContext;
	}

}
