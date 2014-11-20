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
package org.eclipse.emf.ecp.view.core.vaadin.internal;

import java.util.Set;

import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * A class describing a rendererClass.
 *
 * @author Dennis Melzer
 *
 */
public class ECPRendererDescription {

	private final Class<AbstractVaadinRenderer<VElement>> rendererClass;
	private final Set<ECPRendererTester> tester;

	/**
	 * The constructor of the ControlDescription.
	 *
	 * @param rendererClass the rendererClass
	 * @param tester the class testing whether the rendererClass is applicable for the current view model context
	 */
	public ECPRendererDescription(Class<AbstractVaadinRenderer<VElement>> rendererClass, Set<ECPRendererTester> tester) {
		super();
		this.rendererClass = rendererClass;
		this.tester = tester;
	}

	/**
	 * The rendererClass. It extends the {@link AbstractVaadinRenderer}.
	 *
	 * @return the class implementing this rendererClass
	 */
	public Class<AbstractVaadinRenderer<VElement>> getRenderer() {
		return rendererClass;
	}

	/**
	 * The tester for this rendererClass. The tester is used to check whether this rendererClass is usable on a specific
	 * view model context.
	 *
	 * @return the {@link ECPRendererTester} implementation
	 */
	public Set<ECPRendererTester> getTester() {
		return tester;
	}
}
