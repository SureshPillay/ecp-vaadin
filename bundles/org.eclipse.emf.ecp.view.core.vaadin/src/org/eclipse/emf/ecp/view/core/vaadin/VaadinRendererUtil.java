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
import org.eclipse.osgi.util.NLS;

import com.vaadin.ui.AbstractTextField;

/**
 * Helper class for util staff.
 *
 * @author Dennis Melzer
 *
 */
public final class VaadinRendererUtil {

	private VaadinRendererUtil() {

	}

	/**
	 * Gets the {@link IItemPropertyDescriptor}.
	 *
	 * @param setting the setting
	 * @return the {@link IItemPropertyDescriptor}
	 */
	public static IItemPropertyDescriptor getItemPropertyDescriptor(Setting setting) {
		return getItemPropertyDescriptor(setting.getEObject(), setting.getEStructuralFeature());
	}

	/**
	 * Gets the {@link IItemPropertyDescriptor}.
	 *
	 * @param object the object
	 * @param structuralFeature the feature
	 * @return the {@link IItemPropertyDescriptor}
	 */
	public static IItemPropertyDescriptor getItemPropertyDescriptor(EObject object, EStructuralFeature structuralFeature) {
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = getAdapterFactory();
		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
			object, structuralFeature);
		return itemPropertyDescriptor;
	}

	/**
	 * Gets the Factory.
	 *
	 * @return the factory
	 */
	public static AdapterFactoryItemDelegator getAdapterFactory() {
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);
		return adapterFactoryItemDelegator;
	}

	/**
	 * Returns the domain model reference for a {@link VControl}.
	 *
	 * @param control the control
	 * @return the {@link VTableDomainModelReference}
	 */
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

	/**
	 * Returns the columns feature for {@link VControl}.
	 *
	 * @param control the control
	 * @return the list of {@link EStructuralFeature}
	 */
	public static List<EStructuralFeature> getColumnFeatures(VControl control) {
		final VTableDomainModelReference domainModelReference = getTableDomainModelReference(control);
		final List<EStructuralFeature> listFeatures = new ArrayList<EStructuralFeature>();
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

	/**
	 * Set a Converter for a field.
	 *
	 * @param eStructuralFeature the feature
	 * @param component the component
	 * @param vElement the elment
	 * @param viewContext the context
	 */
	public static void setConverterToTextField(EStructuralFeature eStructuralFeature, AbstractTextField component,
		VElement vElement, ViewModelContext viewContext) {
		Class<?> instanceClass = eStructuralFeature.getEType().getInstanceClass();
		if (instanceClass.isPrimitive()) {
			instanceClass = ClassUtils.primitiveToWrapper(instanceClass);
		}

		if (!Number.class.isAssignableFrom(instanceClass)) {
			return;
		}
		component.setConversionError(NLS.bind(VaadinRendererMessages.ConversionError,
			instanceClass.getSimpleName()));
		component.setConverter(instanceClass);
		// TODO: Test change to new style property
		final VTMandatoryStyleProperty mandatoryStyleProperty = VaadinStyleTemplateUtil.getVTStyleProperty(
			VTMandatoryPackage.Literals.MANDATORY_STYLE_PROPERTY, vElement, viewContext);
		if (mandatoryStyleProperty != null) {
			component.setConverter(new VaadinStringToNumberConverter(instanceClass));
		}
	}
}
