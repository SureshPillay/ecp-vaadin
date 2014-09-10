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
package org.eclipse.emf.ecp.view.model.vaadin;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.vaadin.validator.AbstractFieldValidator;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.ui.Component;

public abstract class AbstractControlRendererVaadin<T extends VControl> extends AbstractVaadinRenderer<T> {

	protected void setCaption(T control, Component component) {
		if (component == null || LabelAlignment.NONE == control.getLabelAlignment()) {
			return;
		}

		Setting setting = control.getDomainModelReference().getIterator().next();
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
				composedAdapterFactory);

		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
				setting.getEObject(), setting.getEStructuralFeature());

		if (itemPropertyDescriptor == null) {
			return;
		}
		String extra = "";

		if (setting.getEStructuralFeature().getLowerBound() > 0) {
			extra = "*"; //$NON-NLS-1$
		}
		component.setCaption(itemPropertyDescriptor.getDisplayName(setting.getEObject()) + extra);
	}

	protected void applyValidation(T control, ECPVaadinComponent component) {
		applyDiagnostic(control, component);
	}

	private void applyComponentValidation(T control, ECPVaadinComponent vaadinComponent) {
		AbstractFieldValidator<?> componentValidator = vaadinComponent.getComponentValidator();
		if (componentValidator == null) {
			return;
		}
		componentValidator.setErrorMessage(control.getDiagnostic().getMessage());
		componentValidator.validateField();
	}

	private void applyDiagnostic(T control, ECPVaadinComponent component) {
		if (control.getDiagnostic() == null) {
			return;
		}
		if (Diagnostic.ERROR == control.getDiagnostic().getHighestSeverity()) {
			component.getComponent().setId("error");
			applyComponentValidation(control, component);
		}
	}

	@Override
	public Component render(T renderable, final ViewModelContext viewContext) {
		ECPVaadinComponent vaadinComponent = renderComponent(renderable, viewContext);
		Component component = vaadinComponent.getComponent();
		setCaption(renderable, component);

		renderable.eAdapters().add(new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				if (msg.getFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
					applyValidation(renderable, vaadinComponent);
				}
			}

		});

		applyValidation(renderable, vaadinComponent);

		return component;
	}
}
