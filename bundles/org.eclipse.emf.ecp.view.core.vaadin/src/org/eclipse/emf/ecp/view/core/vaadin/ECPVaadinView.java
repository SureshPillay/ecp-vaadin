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

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;

import com.vaadin.ui.Component;

/**
 * This is the result of a Rendering call. It contains the created Vaadin Control and the {@link ViewModelContext}.
 *
 * @author Dennis Melzer
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ECPVaadinView {

	/**
	 * Returns the Vaadin Component.
	 *
	 * @return the {@link Component}
	 */
	Component getComponent();

	/**
	 * Dispose Databining
	 */
	void dispose();

	/**
	 * Return the {@link ViewModelContext}.
	 *
	 * @return the context
	 */
	ViewModelContext getViewModelContext();
}
