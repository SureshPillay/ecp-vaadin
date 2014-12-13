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
import org.eclipse.emf.ecp.controls.vaadin.internal.VaadinRendererMessages;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractControlRendererVaadin;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

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
		final Label label = new Label();
		createDatabinding(setting, component);
		component.setWidth(100, Unit.PERCENTAGE);

		if (setting.getEStructuralFeature().isUnsettable()) {
			final HorizontalLayout horizontalLayout = new HorizontalLayout();
			horizontalLayout.setWidth(100, Unit.PERCENTAGE);
			final Button setButton = new Button();
			setButton.setEnabled(getVElement().isEnabled());
			setButton.setVisible(getVElement().isVisible());
			setButton.setReadOnly(getVElement().isReadonly());

			createSetOrUnsetComponent(component, horizontalLayout, setButton, setting, label);

			setButton.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					final Object value = setting.isSet() ? SetCommand.UNSET_VALUE : setting
						.getEStructuralFeature().getDefaultValue();
					final EditingDomain editingDomain = getEditingDomain(setting);
					editingDomain.getCommandStack().execute(
						SetCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), value));
					createSetOrUnsetComponent(component, horizontalLayout, setButton, setting, label);

				}
			});
			return horizontalLayout;
		}

		return component;
	}

	private void createSetOrUnsetComponent(final Component component, final HorizontalLayout horizontalLayout,
		final Button setButton, Setting setting, Label label) {
		horizontalLayout.removeAllComponents();
		Component addComponent = component;
		if (setting.isSet()) {
			setButton.setCaption(VaadinRendererMessages.AbstractVaadinSimpleControlRenderer_Set);
			label.setCaption(getUnsetLabel());
			addComponent = label;

		} else {
			setButton.setCaption(VaadinRendererMessages.AbstractVaadinSimpleControlRenderer_Unset);

		}
		horizontalLayout.setData(addComponent);
		horizontalLayout.addComponent(addComponent);
		horizontalLayout.setExpandRatio(addComponent, 1f);
		horizontalLayout.addComponent(setButton);
		horizontalLayout.setComponentAlignment(setButton, Alignment.BOTTOM_RIGHT);
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

	protected EditingDomain getEditingDomain(Setting setting) {
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	/**
	 * Gets the unset label
	 *
	 * @return the unset label
	 */
	protected String getUnsetLabel() {
		return VaadinRendererMessages.AbstractVaadinSimpleControlRenderer_NotSetClickToSet;
	}

	/**
	 * Creates the Vaadin control.
	 *
	 * @return the component
	 */
	protected abstract Component createControl();

}
