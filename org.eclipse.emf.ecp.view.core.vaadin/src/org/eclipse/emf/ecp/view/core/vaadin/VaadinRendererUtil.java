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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public final class VaadinRendererUtil {

	private VaadinRendererUtil() {

	}

	public static IItemPropertyDescriptor getItemPropertyDescriptor(Setting setting) {
		return getItemPropertyDescriptor(setting.getEObject(), setting.getEStructuralFeature());
	}

	public static IItemPropertyDescriptor getItemPropertyDescriptor(EObject object, EStructuralFeature structuralFeature) {
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = getAdapterFactory();
		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
				object, structuralFeature);
		return itemPropertyDescriptor;
	}

	public static AdapterFactoryItemDelegator getAdapterFactory() {
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
				composedAdapterFactory);
		return adapterFactoryItemDelegator;
	}

	public static VTableDomainModelReference getTableDomainModelReference(VControl control) {
		final VDomainModelReference vdmr = control.getDomainModelReference();
		if (VTableDomainModelReference.class.isInstance(vdmr)) {
			return VTableDomainModelReference.class.cast(vdmr);
		}

		final EList<EObject> contents = vdmr.eContents();
		for (final EObject eObject : contents) {
			if (VTableDomainModelReference.class.isInstance(eObject)) {
				return VTableDomainModelReference.class.cast(eObject);
			}
		}
		return null;
	}

	public static List<EStructuralFeature> getColumnFeatures(VControl control) {
		VTableDomainModelReference domainModelReference = getTableDomainModelReference(control);
		List<EStructuralFeature> listFeatures = new ArrayList<EStructuralFeature>();
		// TODO FIXME: NPE
		for (final VDomainModelReference column : domainModelReference.getColumnDomainModelReferences()) {
			for (Iterator<EStructuralFeature> iterator = column.getEStructuralFeatureIterator(); iterator.hasNext();) {
				listFeatures.add(iterator.next());
			}

		}
		return listFeatures;
	}

}
