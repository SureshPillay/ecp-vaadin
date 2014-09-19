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

import org.eclipse.emf.ecp.view.core.vaadin.AbstractContainerRendererVaadin;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalLayout;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.HorizontalLayout;

public class HorizontalLayoutRendererVaadin extends AbstractContainerRendererVaadin<VHorizontalLayout> {

	@Override
	protected AbstractOrderedLayout getComponentContainer(VHorizontalLayout renderable) {
		return new HorizontalLayout();
	}

}
