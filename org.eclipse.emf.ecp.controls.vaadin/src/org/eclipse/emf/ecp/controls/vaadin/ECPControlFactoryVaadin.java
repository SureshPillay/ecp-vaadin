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
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.spi.ECPAbstractControl;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;

public abstract class ECPControlFactoryVaadin extends ECPAbstractControl {

	public abstract Component createControl(VControl control, Setting setting);

	protected Binding bindModelToTarget(IObservableValue target, IObservableValue model,
			UpdateValueStrategy targetToModelStrategy, UpdateValueStrategy modelToTargetStrategy) {
		final Binding binding = getDataBindingContext().bindValue(target, model, targetToModelStrategy,
				modelToTargetStrategy);
		return binding;
	}

	protected Binding bindModelToTarget(IObservableList target, IObservableList model) {
		final Binding binding = getDataBindingContext().bindList(target, model);
		return binding;
	}

	public Component render(final VControl control) {
		Setting setting = control.getDomainModelReference().getIterator().next();
		final Component component = createControl(control, setting);
		component.setEnabled(!control.isReadonly());

		IObservableValue targetValue = VaadinObservables.observeValue((ValueChangeNotifier) component);
		IObservableValue modelValue = EMFProperties.value(setting.getEStructuralFeature())
				.observe(setting.getEObject());
		bindModelToTarget(targetValue, modelValue, getTargetToModelStrategy(control), getModelToTargetStrategy(control));

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
