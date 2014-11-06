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

import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VContainer;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;

public abstract class AbstractContainerRendererVaadin<T extends VContainer> extends AbstractVaadinRenderer<T> {

	@Override
	protected Component render() {
		T renderable = getVElement();

		AbstractOrderedLayout layout = getAbstractOrderedLayout();
		for (VContainedElement composite : renderable.getChildren()) {
			Component renderResult = this.rendererFactory.render(composite, getViewModelContext());
			layout.addComponent(renderResult);
		}
		Component renderComponent = getRenderComponent(layout);
		renderComponent.setWidth(100, Unit.PERCENTAGE);
		if (renderComponent instanceof AbstractOrderedLayout) {
			AbstractOrderedLayout abstractOrderedLayout = (AbstractOrderedLayout) renderComponent;
			abstractOrderedLayout.setMargin(isMargin());
			abstractOrderedLayout.setSpacing(isSpacing());
		}
		return renderComponent;
	}

	@Override
	protected void applyCaption(Component controlComponent) {
		if (shouldShowCaption()) {
			super.applyCaption(controlComponent);
		}
	}

	protected boolean shouldShowCaption() {
		return false;
	}

	protected boolean isMargin() {
		return false;
	}

	protected boolean isSpacing() {
		return true;
	}

	protected Component getRenderComponent(AbstractOrderedLayout orderedLayout) {
		return orderedLayout;
	}

	protected abstract AbstractOrderedLayout getAbstractOrderedLayout();
}
