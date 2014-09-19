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
import java.util.Locale;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.core.vaadin.converter.StringToVaadinConverter;
import org.eclipse.emf.ecp.view.core.vaadin.converter.VaadinConverterToString;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ECPVaadinPrimitiveList extends ECPControlFactoryVaadin {

	private static final String VALUE_COLUMN = "value";
	private static final String REMOVE_COLUMN = "remove";

	@Override
	public Component createControl(VControl control, Setting setting) {
		return null;
	}

	@Override
	public Component render(final VControl control, boolean caption) {
		final Setting setting = control.getDomainModelReference().getIterator().next();
		final Class<?> instanceClass = setting.getEStructuralFeature().getEType().getInstanceClass();
		EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final Table table = new Table();
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		table.setSizeFull();
		layout.addComponent(table);
		table.setSelectable(true);
		table.addStyleName("reference-list");

		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(VALUE_COLUMN, String.class, null);
		container.addContainerProperty(REMOVE_COLUMN, Button.class, null);

		final TextField textField = new TextField();
		final Converter<String, Object> converter = createConverter(instanceClass, table, textField);
		table.addGeneratedColumn(REMOVE_COLUMN, new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				return VaadinWidgetFactory.createTableRemoveButtonFlat(setting, itemId);
			}
		});
		table.setColumnWidth(REMOVE_COLUMN, 40);
		table.setContainerDataSource(container);

		table.addItems(setting.getEObject().eGet(eStructuralFeature));

		Class<? extends EObject> clazz = setting.getEObject().getClass();
		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(table, clazz);

		IObservableList modelValue = EMFProperties.list(eStructuralFeature).observe(setting.getEObject());
		bindModelToTarget(targetValue, modelValue);

		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		layout.addComponent(horizontalLayout);

		textField.setNullRepresentation("");
		horizontalLayout.addComponent(textField);
		textField.setWidth(100, Unit.PERCENTAGE);
		final Button add = VaadinWidgetFactory.createListAddButton(setting, textField);
		horizontalLayout.addComponent(add);
		horizontalLayout.setComponentAlignment(add, Alignment.TOP_RIGHT);
		horizontalLayout.setExpandRatio(textField, 1.0f);

		IObservableValue targetValueList = VaadinObservables.observeValue(textField);
		UpdateValueStrategy emfUpdateValueStrategy = new UpdateValueStrategy();
		UpdateValueStrategy emfUpdateValueStrategyModel = new UpdateValueStrategy();
		if (converter != null) {
			emfUpdateValueStrategy.setConverter(new StringToVaadinConverter(converter));
			emfUpdateValueStrategy.setConverter(new VaadinConverterToString(converter));
		}
		targetValueList.addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(org.eclipse.core.databinding.observable.value.ValueChangeEvent event) {
				try {
					Object fieldValue = getConvertedValue(textField);
					// TODO FIXME: Better solution for changing String and primitive types in List?
					ValueDiff diff = event.diff;
					if (table.getValue() != null && !table.getValue().equals(fieldValue)
							&& diff.getOldValue() != diff.getNewValue()) {
						List<Object> items = (List<Object>) setting.get(true);
						Object convertedValue = getConvertedValue(diff.getOldValue(), converter);
						int index = items.indexOf(convertedValue);
						if (index != -1) {
							items.remove(table.getValue());
							Object convertToModel = getConvertedValue(diff.getNewValue(), converter);
							items.add(index, convertToModel);
						}
					}
				} catch (com.vaadin.data.util.converter.Converter.ConversionException e) {
					return;
				}

			}
		});

		IObservableValue modelValueList = VaadinObservables.observeValue(table);
		bindModelToTarget(modelValueList, targetValueList, emfUpdateValueStrategy, emfUpdateValueStrategyModel);
		IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(table, clazz);

		emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());
		getDataBindingContext().bindValue(VaadinObservables.observeFocus(textField), observeSingleSelection, null,
				emfUpdateValueStrategy);

		emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter(false));
		getDataBindingContext().bindValue(VaadinObservables.observeVisible(add), observeSingleSelection, null,
				emfUpdateValueStrategy);
		layout.setData(table);
		return layout;
	}

	private Object getConvertedValue(TextField textField) {
		Converter<String, Object> converter = textField.getConverter();
		if (converter == null) {
			return textField.getValue();
		}

		return textField.getConvertedValue();
	}

	private Object getConvertedValue(Object value, Converter<String, Object> converter) {
		if (converter == null) {
			return value;
		}
		try {
			return converter.convertToModel(String.valueOf(value), converter.getModelType(), Locale.getDefault());
		} catch (com.vaadin.data.util.converter.Converter.ConversionException e) {
			return null;
		}
	}

	private Converter<String, Object> createConverter(final Class<?> instanceClass, final Table table,
			final TextField textField) {
		if (Number.class.isAssignableFrom(instanceClass)) {
			textField.setConverter(instanceClass);
		}
		final Converter<String, Object> converter = textField.getConverter();
		table.addGeneratedColumn(VALUE_COLUMN, new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				if (converter == null) {
					return itemId;
				}
				return converter.convertToPresentation(itemId, String.class, Locale.getDefault());
			}
		});
		return converter;
	}
}
