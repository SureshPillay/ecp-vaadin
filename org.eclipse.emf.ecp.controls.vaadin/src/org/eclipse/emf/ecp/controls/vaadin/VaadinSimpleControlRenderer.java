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

public abstract class VaadinSimpleControlRenderer extends AbstractControlRendererVaadin<VControl> {

	private IObservableValue modelValue;
	private final WritableValue value = new WritableValue();

	@Override
	protected void dispose() {
		if (this.value != null) {
			this.value.dispose();
		}

		if (this.modelValue != null) {
			this.modelValue.dispose();
			this.modelValue = null;
		}
		super.dispose();
	}

	protected Binding bindModelToTarget(IObservableValue target, IObservableValue model,
			UpdateValueStrategy targetToModelStrategy, UpdateValueStrategy modelToTargetStrategy) {
		final Binding binding = this.bindingContext.bindValue(target, model, targetToModelStrategy,
				modelToTargetStrategy);
		return binding;
	}

	protected Binding bindModelToTarget(IObservableList target, IObservableList model) {
		final Binding binding = this.bindingContext.bindList(target, model);
		return binding;
	}

	@Override
	protected Component render() {
		Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final Component component = createControl();
		createDatabinding(setting, component);
		component.setWidth(100, Unit.PERCENTAGE);
		return component;
	}

	protected void createDatabinding(Setting setting, final Component component) {
		IObservableValue targetValue = VaadinObservables.observeValue((ValueChangeNotifier) component);
		IObservableValue modelValue = getModelValue(setting);
		bindModelToTarget(targetValue, modelValue, getTargetToModelStrategy(component),
				getModelToTargetStrategy(component));
	}

	protected UpdateValueStrategy getModelToTargetStrategy(Component component) {
		return new EMFUpdateValueStrategy();
	}

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
		final IItemPropertyDescriptor descriptor = this.adapterFactoryItemDelegator.getPropertyDescriptor(
				setting.getEObject(), setting.getEStructuralFeature());
		return descriptor;
	}

	protected final IObservableValue getModelValue(final Setting setting) {
		if (this.modelValue == null) {

			this.modelValue = EMFEditProperties.value(getEditingDomain(setting), setting.getEStructuralFeature())
					.observeDetail(this.value);
		}
		return this.modelValue;
	}

	private void updateControl() {
		final Iterator<Setting> settings = getVElement().getDomainModelReference().getIterator();
		if (settings.hasNext()) {
			this.value.setValue(settings.next().getEObject());
		} else {
			this.value.setValue(null);
		}
	}

	protected final EditingDomain getEditingDomain(Setting setting) {
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	protected abstract Component createControl();

}
