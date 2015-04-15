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
import org.eclipse.emf.ecp.view.spi.model.VElement;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;

/**
 * Abstract Vaadin Renderer for {@link VContainer}.
 *
 * @author Dennis Melzer
 *
 * @param <T> VContainer Element
 */
public abstract class AbstractContainerRendererVaadin<T extends VContainer> extends AbstractVaadinRenderer<T> {

	@Override
	protected Component render() {
		final T renderable = getVElement();

		final AbstractOrderedLayout layout = getAbstractOrderedLayout();
		for (final VContainedElement composite : renderable.getChildren()) {
			final Component renderResult = getRendererFactory().render(composite, getViewModelContext());
			layout.addComponent(renderResult);
		}
		final Component renderComponent = getRenderComponent(layout);
		renderComponent.setWidth(100, Unit.PERCENTAGE);
		if (renderComponent instanceof AbstractOrderedLayout) {
			final AbstractOrderedLayout abstractOrderedLayout = (AbstractOrderedLayout) renderComponent;
			abstractOrderedLayout.setMargin(isMargin());
			abstractOrderedLayout.setSpacing(isSpacing());
		}
		return renderComponent;
	}

	@Override
	protected void applyCaption() {
		if (shouldShowCaption()) {
			super.applyCaption();
		}
	}

	@Override
	public void dispose() {
		for (final VContainedElement composite : getVElement().getChildren()) {
			final AbstractVaadinRenderer<VElement> renderer = getRendererFactory().getVaadinComponentRenderer(
				composite, getViewModelContext());
			renderer.dispose();
		}
		super.dispose();
	}

	/**
	 * Show the vaadin caption.
	 *
	 * @return false = not true = show
	 */
	protected boolean shouldShowCaption() {
		return false;
	}

	/**
	 * Set a margin.
	 *
	 * @return boolean
	 */
	protected boolean isMargin() {
		return false;
	}

	/**
	 * Set a spacing.
	 *
	 * @return boolean
	 */
	protected boolean isSpacing() {
		return true;
	}

	/**
	 * Returns the render component e.g. for the caption
	 *
	 * @param orderedLayout the parent layout
	 * @return the component
	 */
	protected Component getRenderComponent(AbstractOrderedLayout orderedLayout) {
		return orderedLayout;
	}

	/**
	 * Returns the {@link AbstractOrderedLayout}.
	 *
	 * @return the layout
	 */
	protected abstract AbstractOrderedLayout getAbstractOrderedLayout();
}
