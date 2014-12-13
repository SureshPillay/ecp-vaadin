/*******************************************************************************
 * Copyright (c) 2013 EclipseSource.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.view.core.vaadin;

import org.eclipse.osgi.util.NLS;

public final class VaadinRendererMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.emf.ecp.view.core.vaadin.messages"; //$NON-NLS-1$

	public static String ConversionError;

	private VaadinRendererMessages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, VaadinRendererMessages.class);
	}

}
