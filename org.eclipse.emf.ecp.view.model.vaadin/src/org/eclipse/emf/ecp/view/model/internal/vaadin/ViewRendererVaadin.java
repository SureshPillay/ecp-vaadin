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

import org.eclipse.emf.ecp.view.model.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.model.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VView;

import com.vaadin.ui.Component;

public class ViewRendererVaadin extends AbstractVaadinRenderer<VView> {

	@Override
	public Component render(VView renderable, ViewModelContext viewModelContext) {
		ECPVaadinViewComponent customComponent = new ECPVaadinViewComponent();
		customComponent.setSizeFull();
		for (VContainedElement composite : renderable.getChildren()) {
			Component renderResult = VaadinRendererFactory.INSTANCE.render(composite, viewModelContext);
			customComponent.addComponent(renderResult);

		}

		return customComponent;
	}

}
