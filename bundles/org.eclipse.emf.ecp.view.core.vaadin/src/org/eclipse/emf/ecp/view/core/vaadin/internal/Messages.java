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

import org.eclipse.osgi.util.NLS;

/**
 * The Bundle message class for l10n.
 *
 * @author Dennis Melzer
 *
 */
public final class Messages {
	private static final String BUNDLE_NAME = "OSGI-INF.l10n.messages"; //$NON-NLS-1$

	private Messages() {

	}

	/**
	 * The ok label.
	 */
	public static String ok;
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}