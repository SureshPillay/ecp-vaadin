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

package org.eclipse.emf.ecp.view.label.vaadin;

import java.util.Iterator;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.label.model.VLabel;
import org.eclipse.emf.ecp.view.spi.label.model.VLabelStyle;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelRendererVaadin extends AbstractVaadinRenderer<VLabel> {

	private static final String HTML_SEPERATOR = "<hr/>";
	private DataBindingContext bindingContext;
	private IObservableValue modelValue;
	private final WritableValue value = new WritableValue();

	@Override
	public Component render() {
		VLabel renderable = getVElement();
		String name = getVElement().getName() == null ? "" : getVElement().getName();
		Label label = new Label(name);
		if (renderable.getStyle() == VLabelStyle.SEPARATOR) {
			label = new Label(HTML_SEPERATOR, ContentMode.HTML);
		} else {
			label.addStyleName(renderable.getStyle().getName().toLowerCase());
			if (renderable.getStyle().getValue() > 6) {
				label.addStyleName("small");
			}
		}
		if (getVElement().getDomainModelReference() == null) {
			return label;
		}
		Iterator<Setting> iterator = getVElement().getDomainModelReference().getIterator();
		if (iterator.hasNext()) {
			createDatabinding(iterator.next(), label);
		}
		return label;
	}

	@Override
	protected void applyCaption() {
	}

	@Override
	protected void dispose() {
		if (this.value != null) {
			this.value.dispose();
		}

		if (this.bindingContext != null) {
			this.bindingContext.dispose();
			this.bindingContext = null;
		}
		if (this.modelValue != null) {
			this.modelValue.dispose();
			this.modelValue = null;
		}
		super.dispose();
	}

	@Override
	public void init(VLabel vElement, ViewModelContext viewContext) {
		super.init(vElement, viewContext);
		this.bindingContext = new EMFDataBindingContext();
		updateControl();
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

	protected final IObservableValue getModelValue(final Setting setting) {
		if (this.modelValue == null) {

			this.modelValue = EMFEditProperties.value(getEditingDomain(setting), setting.getEStructuralFeature())
					.observeDetail(this.value);
		}
		return this.modelValue;
	}

	protected Binding bindModelToTarget(IObservableValue target, IObservableValue model,
			UpdateValueStrategy targetToModelStrategy, UpdateValueStrategy modelToTargetStrategy) {
		final Binding binding = this.bindingContext.bindValue(target, model, targetToModelStrategy,
				modelToTargetStrategy);
		return binding;
	}

	protected final EditingDomain getEditingDomain(Setting setting) {
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	private void updateControl() {
		if (getVElement().getDomainModelReference() == null) {
			return;
		}
		final Iterator<Setting> settings = getVElement().getDomainModelReference().getIterator();
		if (settings.hasNext()) {
			this.value.setValue(settings.next().getEObject());
		} else {
			this.value.setValue(null);
		}
	}

}
