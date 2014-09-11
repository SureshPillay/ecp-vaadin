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

package org.eclipse.emf.ecp.view.label.vaadin;

import org.eclipse.emf.ecp.view.model.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.label.model.VLabel;
import org.eclipse.emf.ecp.view.spi.label.model.VLabelStyle;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelRendererVaadin extends AbstractVaadinRenderer<VLabel> {

	@Override
	public Component render(VLabel renderable, final ViewModelContext viewContext) {
		Label label;

		if (renderable.getStyle() == VLabelStyle.SEPARATOR) {
			label = new Label("<hr/>", ContentMode.HTML);
		} else {
			label = new Label(renderable.getName());
			label.addStyleName(renderable.getStyle().getName().toLowerCase());
			if (renderable.getStyle().getValue() > 6) {
				label.addStyleName("small");
			}
		}

		return label;
	}

}
