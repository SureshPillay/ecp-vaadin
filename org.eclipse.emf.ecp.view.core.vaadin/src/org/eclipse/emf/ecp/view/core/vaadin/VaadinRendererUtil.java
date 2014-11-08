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

import org.apache.commons.lang3.ClassUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.converter.VaadinStringToNumberConverter;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.template.style.mandatory.model.VTMandatoryPackage;
import org.eclipse.emf.ecp.view.template.style.mandatory.model.VTMandatoryStyleProperty;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.ui.AbstractTextField;

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
		// TODO FIXME: NPE and other ModelReferences?
		for (final VDomainModelReference column : domainModelReference.getColumnDomainModelReferences()) {

			final Iterator<EStructuralFeature> eStructuralFeatureIterator = column.getEStructuralFeatureIterator();
			if (eStructuralFeatureIterator == null || !eStructuralFeatureIterator.hasNext()) {
				continue;
			}
			final EStructuralFeature eStructuralFeature = eStructuralFeatureIterator.next();
			listFeatures.add(eStructuralFeature);

		}
		return listFeatures;
	}

	public static void setConverterToTextField(EStructuralFeature eStructuralFeature, AbstractTextField component,
			VElement vElement, ViewModelContext viewContext) {
		Class<?> instanceClass = eStructuralFeature.getEType().getInstanceClass();
		if (instanceClass.isPrimitive()) {
			instanceClass = ClassUtils.primitiveToWrapper(instanceClass);
		}

		if (!(Number.class.isAssignableFrom(instanceClass))) {
			return;
		}

		component.setConverter(instanceClass);
		// TODO: Test change to new style property
		VTMandatoryStyleProperty mandatoryStyleProperty = VaadinStyleTemplateUtil.getVTStyleProperty(
				VTMandatoryPackage.Literals.MANDATORY_STYLE_PROPERTY, vElement, viewContext);
		if (mandatoryStyleProperty != null) {
			component.setConverter(new VaadinStringToNumberConverter(instanceClass));
		}
	}
}
