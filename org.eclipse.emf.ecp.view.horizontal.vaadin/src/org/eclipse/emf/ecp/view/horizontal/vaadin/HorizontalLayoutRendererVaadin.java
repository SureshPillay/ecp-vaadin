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

package org.eclipse.emf.ecp.view.horizontal.vaadin;

import org.eclipse.emf.ecp.view.model.vaadin.ECPVaadinComponent;
import org.eclipse.emf.ecp.view.model.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.model.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalLayout;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class HorizontalLayoutRendererVaadin extends AbstractVaadinRenderer<VHorizontalLayout> {

	@Override
	public ECPVaadinComponent renderComponent(VHorizontalLayout renderable, final ViewModelContext viewContext) {
		HorizontalLayout layout = new HorizontalLayout();
		for (VContainedElement composite : renderable.getChildren()) {
			Component renderResult = VaadinRendererFactory.INSTANCE.render(composite, viewContext);
			layout.addComponent(renderResult);
		}
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.setMargin(true);

		return new ECPVaadinComponent(layout);
	}

}
