/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * dme - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.controls.vaadin;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecp.view.model.common.XMLDateControlRendererTester;

/**
 * TODO: Remove https://bugs.eclipse.org/bugs/show_bug.cgi?id=453853
 *
 * @author dme
 *
 */
public class XMLVaadinDateControlRendererTester extends XMLDateControlRendererTester {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.XMLDateControlRendererTester#checkFeatureETypeAnnotations(org.eclipse.emf.common.util.EList)
	 */
	@Override
	protected boolean checkFeatureETypeAnnotations(EList<EAnnotation> eAnnotations) {
		return true;
	}
}
