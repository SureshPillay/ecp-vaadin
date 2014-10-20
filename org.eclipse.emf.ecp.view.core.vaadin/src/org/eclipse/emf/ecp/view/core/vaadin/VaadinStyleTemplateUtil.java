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
package org.eclipse.emf.ecp.view.core.vaadin;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecp.view.core.vaadin.internal.Activator;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.template.model.VTStyleProperty;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;

public final class VaadinStyleTemplateUtil {

	private VaadinStyleTemplateUtil() {

	}

	public static <T extends VTStyleProperty> T getVTStyleProperty(EClass eClass, VElement vElement,
			ViewModelContext viewContext) {

		final VTViewTemplateProvider vtViewTemplateProvider = Activator.getDefault().getVTViewTemplateProvider();
		if (vtViewTemplateProvider == null) {
			return null;
		}
		final Set<VTStyleProperty> styleProperties = vtViewTemplateProvider.getStyleProperties(vElement, viewContext);
		for (final VTStyleProperty styleProperty : styleProperties) {
			if (styleProperty.eClass().isSuperTypeOf(eClass)) {
				return (T) styleProperty;
			}

		}
		return null;
	}

}
