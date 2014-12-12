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
package org.eclipse.emf.ecp.controls.vaadin.internal;

import org.eclipse.osgi.util.NLS;

public final class VaadinRendererMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.emf.ecp.controls.vaadin.messages"; //$NON-NLS-1$

	public static String AbstractVaadinSimpleControlRenderer_Set;
	public static String AbstractVaadinSimpleControlRenderer_Unset;

	public static String BooleanControlVaadinRenderer_NoBooleanSetClickToSetBoolean;
	public static String DateTimeControlVaadinRenderer_NoDateSetClickToSetDate;
	public static String EnumComboViewerVaadinRenderer_NoValueSetClickToSetValue;
	public static String NumberControlVaadinRenderer_NoNumberClickToSetNumber;
	public static String AbstractVaadinSimpleControlRenderer_NotSetClickToSet;
	public static String TextControlVaadinRenderer_NoTextSetClickToSetText;
	public static String XMLDateControlVaadinRenderer_NoDateSetClickToSetDate;
	public static String ReferenceListVaadinRenderer_NoLinkSetClickToSetLink;

	private VaadinRendererMessages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, VaadinRendererMessages.class);
	}

}
