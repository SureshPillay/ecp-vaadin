package org.eclipse.emf.ecp.controls.vaadin.internal;

import java.util.Iterator;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;

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
		return 5;
	}

	protected abstract boolean checkReference(EStructuralFeature feature);
}