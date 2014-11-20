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

import java.util.Iterator;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractControlRendererVaadin;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;

/**
 * Abstract Class for a Vaadin control.
 *
 * @author Dennis Melzer
 *
 */
public abstract class AbstractVaadinSimpleControlRenderer extends AbstractControlRendererVaadin<VControl> {

	private IObservableValue modelValue;
	private final WritableValue value = new WritableValue();

	@Override
	protected void dispose() {
		if (value != null) {
			value.dispose();
		}

		if (modelValue != null) {
			modelValue.dispose();
			modelValue = null;
		}
		super.dispose();
	}

	/**
	 * Bind the model to target.
	 *
	 * @param target the target
	 * @param model the model
	 * @param targetToModelStrategy the targetToModelStrategy
	 * @param modelToTargetStrategy the modelToTargetStrategy
	 * @return binding
	 */
	protected Binding bindModelToTarget(IObservableValue target, IObservableValue model,
		UpdateValueStrategy targetToModelStrategy, UpdateValueStrategy modelToTargetStrategy) {
		final Binding binding = getBindingContext().bindValue(target, model, targetToModelStrategy,
			modelToTargetStrategy);
		return binding;
	}

	/**
	 * Bind the model to target.
	 *
	 * @param target the target
	 * @param model the model
	 * @return the binding
	 */
	protected Binding bindModelToTarget(IObservableList target, IObservableList model) {
		final Binding binding = getBindingContext().bindList(target, model);
		return binding;
	}

	@Override
	protected Component render() {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final Component component = createControl();
		createDatabinding(setting, component);
		component.setWidth(100, Unit.PERCENTAGE);
		return component;
	}

	private void createDatabinding(Setting setting, final Component component) {
		final IObservableValue targetValue = VaadinObservables.observeValue((ValueChangeNotifier) component);
		final IObservableValue modelValue = getModelValue(setting);
		bindModelToTarget(targetValue, modelValue, getTargetToModelStrategy(component),
			getModelToTargetStrategy(component));
	}

	/**
	 * Return the model to target strategy.
	 *
	 * @param component the Vaadin component
	 * @return the strategy
	 */
	protected UpdateValueStrategy getModelToTargetStrategy(Component component) {
		return new EMFUpdateValueStrategy();
	}

	/**
	 * Return the target to model strategy.
	 *
	 * @param component the Vaadin component
	 * @return the strategy
	 */
	protected UpdateValueStrategy getTargetToModelStrategy(Component component) {
		return new EMFUpdateValueStrategy();
	}

	@Override
	public void init(VControl vElement, ViewModelContext viewContext) {
		super.init(vElement, viewContext);
		updateControl();
	}

	/**
	 * Return the {@link IItemPropertyDescriptor} describing this {@link Setting}.
	 *
	 * @param setting the {@link Setting} to use for identifying the {@link IItemPropertyDescriptor}.
	 * @return the {@link IItemPropertyDescriptor}
	 */
	protected final IItemPropertyDescriptor getItemPropertyDescriptor(Setting setting) {
		if (setting == null) {
			return null;
		}
		final IItemPropertyDescriptor descriptor = getAdapterFactoryItemDelegator().getPropertyDescriptor(
			setting.getEObject(), setting.getEStructuralFeature());
		return descriptor;
	}

	private IObservableValue getModelValue(final Setting setting) {
		if (modelValue == null) {

			modelValue = EMFEditProperties.value(getEditingDomain(setting), setting.getEStructuralFeature())
				.observeDetail(value);
		}
		return modelValue;
	}

	private void updateControl() {
		final Iterator<Setting> settings = getVElement().getDomainModelReference().getIterator();
		if (settings.hasNext()) {
			value.setValue(settings.next().getEObject());
		} else {
			value.setValue(null);
		}
	}

	private EditingDomain getEditingDomain(Setting setting) {
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	/**
	 * Creates the Vaadin control.
	 *
	 * @return the component
	 */
	protected abstract Component createControl();

}
