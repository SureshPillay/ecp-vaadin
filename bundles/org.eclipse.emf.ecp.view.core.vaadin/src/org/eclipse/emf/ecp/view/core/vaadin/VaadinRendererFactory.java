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
import org.eclipse.emf.ecp.view.spi.model.VElement;

import com.vaadin.ui.Component;

/**
 * A RendererFactory for Vaadin Renderer.
 *
 * @author Dennis Melzer
 *
 */
public interface VaadinRendererFactory {

	/**
	 * Execute the renderer directly.
	 *
	 * @param vElement the {@link VElement} to render
	 * @param viewContext the {@link ViewModelContext} to use
	 * @return the component
	 */
	Component render(VElement vElement, ViewModelContext viewContext);

	/**
	 * Searches for a fitting renderer for the passed {@link VElement}.
	 *
	 * @param vElement the {@link VElement} to render
	 * @param viewContext the {@link ViewModelContext} to use
	 * @return the {@link AbstractVaadinRenderer} the fitting render or null
	 */
	AbstractVaadinRenderer<VElement> getVaadinComponentRenderer(VElement vElement, ViewModelContext viewContext);
}