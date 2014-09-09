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
package org.eclipse.emf.ecp.controls.vaadin;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.spi.ECPAbstractControl;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public abstract class ECPControlFactoryVaadin extends ECPAbstractControl {

	public abstract Component createControl(VControl control, Setting setting);

	protected Binding bindModelToTarget(IObservableValue target, IObservableValue model,
			UpdateValueStrategy targetToModelStrategy, UpdateValueStrategy modelToTargetStrategy) {
		final Binding binding = getDataBindingContext().bindValue(target, model, targetToModelStrategy,
				modelToTargetStrategy);
		binding.getValidationStatus().addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus statusNew = (IStatus) event.diff.getNewValue();
				if (IStatus.ERROR == statusNew.getSeverity()) {
					binding.updateModelToTarget();
				}
			}
		});

		return binding;
	}

	protected Binding bindModelToTarget(IObservableList target, IObservableList model) {
		final Binding binding = getDataBindingContext().bindList(target, model);
		binding.getValidationStatus().addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus statusNew = (IStatus) event.diff.getNewValue();
				if (IStatus.ERROR == statusNew.getSeverity()) {
					binding.updateModelToTarget();
				}
			}
		});

		return binding;
	}

	protected void applyValidation(VControl control, Component component) {
		if (control.getDiagnostic() == null) {
			component.setId("ok");
			return;
		}
		switch (control.getDiagnostic().getHighestSeverity()) {
		case Diagnostic.ERROR:
			component.setId("error");
			break;
		case Diagnostic.OK:
			component.setId("ok");
			break;
		}
	}

	public Component render(final VControl control) {
		Setting setting = control.getDomainModelReference().getIterator().next();
		final Component component = createControl(control, setting);

		component.setEnabled(!control.isReadonly());

		VaadinObservables.activateRealm(UI.getCurrent());
		IObservableValue targetValue = VaadinObservables.observeValue((ValueChangeNotifier) component);
		IObservableValue modelValue = EMFProperties.value(setting.getEStructuralFeature())
				.observe(setting.getEObject());
		bindModelToTarget(targetValue, modelValue, getTargetToModelStrategy(control), getModelToTargetStrategy(control));

		control.eAdapters().add(new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				if (msg.getFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
					applyValidation(control, component);
				}
			}

		});

		applyValidation(control, component);
		component.setWidth(100, Unit.PERCENTAGE);

		return component;
	}

	protected UpdateValueStrategy getModelToTargetStrategy(VControl control) {
		return new EMFUpdateValueStrategy();
	}

	protected UpdateValueStrategy getTargetToModelStrategy(VControl control) {
		return new EMFUpdateValueStrategy();
	}

}
