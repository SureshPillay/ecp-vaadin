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

import org.eclipse.emf.ecp.view.model.vaadin.AbstractContainerRendererVaadin;
import org.eclipse.emf.ecp.view.spi.group.model.VGroup;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;

public class GroupLayoutRendererVaadin extends AbstractContainerRendererVaadin<VGroup> {

	@Override
	protected AbstractOrderedLayout getComponentContainer(VGroup renderable) {
		VerticalLayout formLayout = new VerticalLayout();
		if (!renderable.isContainerLayoutEmbedding()) {
			formLayout.addStyleName("group");
		}
		return formLayout;
	}

	@Override
	protected boolean isMargin(VGroup renderable) {
		return !renderable.isContainerLayoutEmbedding();
	}
}
