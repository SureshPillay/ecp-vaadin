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
package makeithappen.vaadin.custom.control;

import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.custom.vaadin.CustomControlVaadinRenderer;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;

public class UploadCustomComponent extends CustomControlVaadinRenderer {

	private Label label;

	@Override
	protected Component render() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		this.label = new Label();
		horizontalLayout.addComponent(this.label);
		Upload upload = new Upload();
		horizontalLayout.addComponent(upload);
		return horizontalLayout;
	}

	@Override
	protected void applyCaption() {
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil
				.getItemPropertyDescriptor(getVElement().getDomainModelReference().getIterator().next());
		this.label.setCaption(itemPropertyDescriptor.getDisplayName(null));
	}
}
