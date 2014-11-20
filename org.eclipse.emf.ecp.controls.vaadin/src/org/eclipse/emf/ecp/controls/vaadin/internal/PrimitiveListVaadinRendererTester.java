package org.eclipse.emf.ecp.controls.vaadin.internal;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Tester for a Vaadin Reference.
 *
 * @author Dennis Melzer
 *
 */
public class PrimitiveListVaadinRendererTester extends AbstractListVaadinRendererTester {

	@Override
	protected boolean checkReference(EStructuralFeature feature) {
		return EReference.class.isInstance(feature);
	}
}