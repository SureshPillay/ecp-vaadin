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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

public class ECPVaadinReferenceList extends ECPControlFactoryVaadin {

	@Override
	public Component createControl(VControl control, Setting setting) {
		return null;
	}

	@Override
	public Component render(final VControl control) {
		final Setting setting = control.getDomainModelReference().getIterator().next();
		final EClass clazz = ((EReference) setting.getEStructuralFeature()).getEReferenceType();

		VerticalLayout layout = new VerticalLayout();
		final ListSelect listSelect = new ListSelect();
		listSelect.setSizeFull();
		listSelect.setNullSelectionAllowed(true);

		listSelect.addItems(setting.getEObject().eGet(setting.getEStructuralFeature()));

		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(listSelect, setting
				.getEObject().getClass());
		IObservableList modelValue = EMFProperties.list(setting.getEStructuralFeature()).observe(setting.getEObject());
		EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
		dataBindingContext.bindList(targetValue, modelValue);
		EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addStyleName("table-button-toolbar");
		layout.addComponent(horizontalLayout);

		IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(listSelect,
				clazz.getInstanceClass());
		Button add = VaadinWidgetFactory.createTableAddButton(setting, listSelect);
		horizontalLayout.addComponent(add);
		Button remove = VaadinWidgetFactory.createTableRemoveButton(setting, listSelect);
		horizontalLayout.addComponent(remove);
		dataBindingContext.bindValue(VaadinObservables.observeEnabled(remove), observeSingleSelection, null,
				emfUpdateValueStrategy);

		layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
		layout.addComponent(listSelect);
		layout.setData(listSelect);
		return layout;
	}
}
