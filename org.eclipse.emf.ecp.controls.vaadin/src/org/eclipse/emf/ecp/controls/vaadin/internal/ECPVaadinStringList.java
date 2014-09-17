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
package org.eclipse.emf.ecp.controls.vaadin.internal;

import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ECPVaadinStringList extends ECPControlFactoryVaadin {

	@Override
	public Component createControl(VControl control, Setting setting) {
		return null;
	}

	@Override
	public Component render(final VControl control) {
		final Setting setting = control.getDomainModelReference().getIterator().next();
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final ListSelect listSelect = new ListSelect();
		EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
		listSelect.setSizeFull();
		layout.addComponent(listSelect);
		listSelect.setNullSelectionAllowed(true);

		listSelect.addItems(setting.getEObject().eGet(eStructuralFeature));

		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(listSelect, setting
				.getEObject().getClass());

		IObservableList modelValue = EMFProperties.list(eStructuralFeature).observe(setting.getEObject());
		bindModelToTarget(targetValue, modelValue);

		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		layout.addComponent(horizontalLayout);

		final TextField textField = new TextField();
		horizontalLayout.addComponent(textField);
		textField.setWidth(100, Unit.PERCENTAGE);
		final Button add = VaadinWidgetFactory.createListAddButton(setting, textField);
		horizontalLayout.addComponent(add);
		horizontalLayout.setComponentAlignment(add, Alignment.TOP_RIGHT);
		final Button remove = VaadinWidgetFactory.createListRemoveButton(setting, listSelect, textField);
		horizontalLayout.addComponent(remove);
		horizontalLayout.setComponentAlignment(remove, Alignment.TOP_RIGHT);
		horizontalLayout.setExpandRatio(textField, 1.0f);

		IObservableValue targetValueList = VaadinObservables.observeValue(textField);

		targetValueList.addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(org.eclipse.core.databinding.observable.value.ValueChangeEvent event) {
				// TODO FIXME: Better solution for changing String in List?
				if (listSelect.getValue() != null && !listSelect.getValue().equals(textField.getValue())
						&& event.diff.getOldValue() != event.diff.getNewValue()) {
					List<Object> items = (List<Object>) setting.get(true);
					int index = items.indexOf(event.diff.getOldValue());
					if (index != -1) {
						items.remove(listSelect.getValue());
						items.add(index, event.diff.getNewValue());
					}
				}

			}
		});

		IObservableValue modelValueList = VaadinObservables.observeValue(listSelect);
		bindModelToTarget(targetValueList, modelValueList, null, null);
		IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(listSelect, setting
				.getEObject().getClass());

		EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());
		getDataBindingContext().bindValue(VaadinObservables.observeVisible(remove), observeSingleSelection, null,
				emfUpdateValueStrategy);
		getDataBindingContext().bindValue(VaadinObservables.observeFocus(textField), observeSingleSelection, null,
				emfUpdateValueStrategy);

		emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter(false));
		getDataBindingContext().bindValue(VaadinObservables.observeVisible(add), observeSingleSelection, null,
				emfUpdateValueStrategy);
		layout.setData(listSelect);
		return layout;
	}
}
