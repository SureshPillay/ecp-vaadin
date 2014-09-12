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
package org.eclipse.emf.ecp.view.model.vaadin;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VContainer;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;

public abstract class AbstractContainerRendererVaadin<T extends VContainer> extends AbstractVaadinRenderer<T> {

	@Override
	public Component render(T renderable, final ViewModelContext viewContext) {
		AbstractOrderedLayout layout = getComponentContainer(renderable);
		for (VContainedElement composite : renderable.getChildren()) {
			Component renderResult = VaadinRendererFactory.INSTANCE.render(composite, viewContext);
			layout.addComponent(renderResult);
		}
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.setMargin(isMargin(renderable));
		layout.setSpacing(isSpacing(renderable));
		return layout;
	}

	protected boolean isMargin(T renderable) {
		return true;
	}

	protected boolean isSpacing(T renderable) {
		return true;
	}

	protected abstract AbstractOrderedLayout getComponentContainer(T renderable);
}
