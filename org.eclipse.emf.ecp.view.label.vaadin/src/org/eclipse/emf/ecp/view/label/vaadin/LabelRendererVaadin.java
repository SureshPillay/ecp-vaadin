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

import org.apache.commons.lang3.StringUtils;
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

/**
 * The Vaadin Renderer for {@link VLabel}.
 *
 * @author Dennis Melzer
 *
 */
public class LabelRendererVaadin extends AbstractVaadinRenderer<VLabel> {

	private static final String SMALL = "small"; //$NON-NLS-1$
	private static final String HTML_SEPERATOR = "<hr/>"; //$NON-NLS-1$
	private DataBindingContext bindingContext;
	private IObservableValue modelValue;
	private final WritableValue value = new WritableValue();

	@Override
	public Component render() {
		final VLabel renderable = getVElement();
		final String name = getVElement().getName() == null ? StringUtils.EMPTY : getVElement().getName();
		Label label = new Label(name);
		if (renderable.getStyle() == VLabelStyle.SEPARATOR) {
			label = new Label(HTML_SEPERATOR, ContentMode.HTML);
		} else {
			label.addStyleName(renderable.getStyle().getName().toLowerCase());
			if (renderable.getStyle().getValue() > 6) {
				label.addStyleName(SMALL);
			}
		}
		if (getVElement().getDomainModelReference() == null) {
			return label;
		}
		final Iterator<Setting> iterator = getVElement().getDomainModelReference().getIterator();
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
		if (value != null) {
			value.dispose();
		}

		if (bindingContext != null) {
			bindingContext.dispose();
			bindingContext = null;
		}
		if (modelValue != null) {
			modelValue.dispose();
			modelValue = null;
		}
		super.dispose();
	}

	@Override
	public void init(VLabel vElement, ViewModelContext viewContext) {
		super.init(vElement, viewContext);
		bindingContext = new EMFDataBindingContext();
		updateControl();
	}

	private void createDatabinding(Setting setting, final Component component) {
		final IObservableValue targetValue = VaadinObservables.observeValue((ValueChangeNotifier) component);
		final IObservableValue modelValue = getModelValue(setting);
		bindModelToTarget(targetValue, modelValue, getTargetToModelStrategy(component),
			getModelToTargetStrategy(component));
	}

	private UpdateValueStrategy getModelToTargetStrategy(Component component) {
		return new EMFUpdateValueStrategy();
	}

	private UpdateValueStrategy getTargetToModelStrategy(Component component) {
		return new EMFUpdateValueStrategy();
	}

	private IObservableValue getModelValue(final Setting setting) {
		if (modelValue == null) {

			modelValue = EMFEditProperties.value(getEditingDomain(setting), setting.getEStructuralFeature())
				.observeDetail(value);
		}
		return modelValue;
	}

	private Binding bindModelToTarget(IObservableValue target, IObservableValue model,
		UpdateValueStrategy targetToModelStrategy, UpdateValueStrategy modelToTargetStrategy) {
		final Binding binding = bindingContext.bindValue(target, model, targetToModelStrategy,
			modelToTargetStrategy);
		return binding;
	}

	private EditingDomain getEditingDomain(Setting setting) {
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	private void updateControl() {
		if (getVElement().getDomainModelReference() == null) {
			return;
		}
		final Iterator<Setting> settings = getVElement().getDomainModelReference().getIterator();
		if (settings.hasNext()) {
			value.setValue(settings.next().getEObject());
		} else {
			value.setValue(null);
		}
	}

}
