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
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VContainer;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;

public abstract class AbstractContainerRendererVaadin<T extends VContainer> extends AbstractVaadinRenderer<T> {

	@Override
	protected Component render(T renderable, final ViewModelContext viewContext) {
		AbstractOrderedLayout layout = getAbstractOrderedLayout(renderable);
		for (VContainedElement composite : renderable.getChildren()) {
			Component renderResult = this.rendererFactory.render(composite, viewContext);
			layout.addComponent(renderResult);
		}
		Component renderComponent = getRenderComponent(renderable, layout);
		renderComponent.setWidth(100, Unit.PERCENTAGE);
		if (renderComponent instanceof AbstractOrderedLayout) {
			AbstractOrderedLayout abstractOrderedLayout = (AbstractOrderedLayout) renderComponent;
			abstractOrderedLayout.setMargin(isMargin(renderable));
			abstractOrderedLayout.setSpacing(isSpacing(renderable));
		}
		return renderComponent;
	}

	@Override
	protected void applyCaption(T renderable, Component controlComponent, ViewModelContext viewContext) {
		if (shouldShowCaption(renderable)) {
			super.applyCaption(renderable, controlComponent, viewContext);
		}
	}

	protected boolean shouldShowCaption(T renderable) {
		return false;
	}

	protected boolean isMargin(T renderable) {
		return false;
	}

	protected boolean isSpacing(T renderable) {
		return true;
	}

	protected Component getRenderComponent(T renderable, AbstractOrderedLayout orderedLayout) {
		return orderedLayout;
	}

	protected abstract AbstractOrderedLayout getAbstractOrderedLayout(T renderable);
}
