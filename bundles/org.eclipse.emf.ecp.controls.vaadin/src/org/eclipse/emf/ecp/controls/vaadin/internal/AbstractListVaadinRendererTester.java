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
package org.eclipse.emf.ecp.controls.vaadin.internal;

import java.util.Iterator;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * Abstract Tester for a Vaadin List Control.
 *
 * @author Dennis Melzer
 *
 */
public abstract class AbstractListVaadinRendererTester implements ECPRendererTester {

	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}

		final VControl vControl = (VControl) vElement;
		final VDomainModelReference domainModelReference = vControl.getDomainModelReference();
		if (domainModelReference == null) {
			return NOT_APPLICABLE;
		}
		EStructuralFeature feature = null;
		int count = 0;
		final Iterator<EStructuralFeature> structuralFeatureIterator = domainModelReference
			.getEStructuralFeatureIterator();
		while (structuralFeatureIterator.hasNext()) {
			feature = structuralFeatureIterator.next();
			count++;
		}
		if (count != 1) {
			return NOT_APPLICABLE;
		}
		if (!feature.isMany()) {
			return NOT_APPLICABLE;
		}

		if (checkReference(feature)) {
			return NOT_APPLICABLE;
		}
		return 1;
	}

	/**
	 * Checks the feature.
	 *
	 * @param feature the feature
	 * @return is reference or not
	 */
	protected abstract boolean checkReference(EStructuralFeature feature);
}