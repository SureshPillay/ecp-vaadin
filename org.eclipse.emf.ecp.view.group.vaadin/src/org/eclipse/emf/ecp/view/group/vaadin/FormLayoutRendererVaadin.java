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
package org.eclipse.emf.ecp.view.group.vaadin;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecp.view.model.vaadin.AbstractContainerRendererVaadin;
import org.eclipse.emf.ecp.view.spi.group.model.VGroup;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.FormLayout;

public class FormLayoutRendererVaadin extends AbstractContainerRendererVaadin<VGroup> {

	@Override
	protected AbstractOrderedLayout getComponentContainer(VGroup renderable) {
		FormLayout formLayout = new FormLayout();
		if (!renderable.isContainerLayoutEmbedding()) {
			formLayout.addStyleName("outlined");
		}
		String name = renderable.getName();
		if (!StringUtils.isEmpty(name)) {
			formLayout.setCaption(name);
		}
		return formLayout;
	}
}
