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
package org.eclipse.emf.ecp.view.core.vaadin.test;

import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.core.vaadin.internal.VaadinRendererFactoryImpl;

/**
 * A Helper class for the JUnit tests.
 *
 * @author Dennis Melzer
 *
 */
public final class VaadinTestHelper {

	private VaadinTestHelper() {

	}

	/**
	 * Returns the {@link VaadinRendererFactoryImpl}.
	 *
	 * @return the vaadin factory
	 */
	public static VaadinRendererFactory getVaadinRendererFactory() {
		return new VaadinRendererFactoryImpl();
	}

}
