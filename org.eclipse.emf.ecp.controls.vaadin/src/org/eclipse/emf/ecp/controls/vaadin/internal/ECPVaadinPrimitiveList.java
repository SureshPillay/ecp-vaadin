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
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.core.vaadin.converter.StringToVaadinConverter;
import org.eclipse.emf.ecp.view.core.vaadin.converter.VaadinConverterToString;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ECPVaadinPrimitiveList extends AbstractVaadinList {

	private static final String VALUE_COLUMN = "value";

	@Override
	public Component createControl(VControl control, ViewModelContext viewContext, Setting setting) {
		return null;
	}

	@Override
	public void renderList(VerticalLayout layout) {
		final Class<?> instanceClass = this.setting.getEStructuralFeature().getEType().getInstanceClass();

		final TextField textField = createTextfield();
		final Converter<String, Object> converter = createConverter(instanceClass, this.table, textField);

		final Button add = createAddButton(this.setting, textField);
		layout.addComponent(this.table);
		layout.setData(this.table);
		layout.addComponent(this.toolbarLayout);
		bindControls(this.setting, textField, converter, add);
	}

	private Class<? extends EObject> bindTable(EStructuralFeature eStructuralFeature, Class<? extends EObject> clazz) {
		this.table.addItems(this.setting.getEObject().eGet(eStructuralFeature));
		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(this.table, clazz);
		IObservableList modelValue = EMFProperties.list(eStructuralFeature).observe(this.setting.getEObject());
		bindModelToTarget(targetValue, modelValue);
		return clazz;
	}

	private void bindControls(final Setting setting, final TextField textField,
			final Converter<String, Object> converter, final Button add) {
		Class<? extends EObject> clazz = setting.getEObject().getClass();
		EStructuralFeature eStructuralFeature = this.setting.getEStructuralFeature();
		bindTable(eStructuralFeature, clazz);
		bindAddTextfield(setting, textField, converter);
		bindTextfieldFocus(clazz, textField);
		bindVisibleAddButton(clazz, add);
	}

	private void bindVisibleAddButton(Class<? extends EObject> clazz, final Button add) {
		IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(this.table, clazz);
		UpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter(false));
		getDataBindingContext().bindValue(VaadinObservables.observeVisible(add), observeSingleSelection, null,
				emfUpdateValueStrategy);
	}

	private void bindTextfieldFocus(Class<? extends EObject> clazz, final TextField textField) {
		IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(this.table, clazz);
		UpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());
		getDataBindingContext().bindValue(VaadinObservables.observeFocus(textField), observeSingleSelection, null,
				emfUpdateValueStrategy);
	}

	private void bindAddTextfield(final Setting setting, final TextField textField,
			final Converter<String, Object> converter) {
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
					if (ECPVaadinPrimitiveList.this.table.getValue() != null
							&& !ECPVaadinPrimitiveList.this.table.getValue().equals(fieldValue)
							&& diff.getOldValue() != diff.getNewValue()) {
						List<Object> items = (List<Object>) setting.get(true);
						Object convertedValue = getConvertedValue(diff.getOldValue(), converter);
						int index = items.indexOf(convertedValue);
						if (index != -1) {
							items.remove(ECPVaadinPrimitiveList.this.table.getValue());
							Object convertToModel = getConvertedValue(diff.getNewValue(), converter);
							items.add(index, convertToModel);
						}
					}
				} catch (com.vaadin.data.util.converter.Converter.ConversionException e) {
					return;
				}

			}
		});

		IObservableValue modelValueList = VaadinObservables.observeValue(this.table);
		bindModelToTarget(modelValueList, targetValueList, emfUpdateValueStrategy, emfUpdateValueStrategyModel);
	}

	private Button createAddButton(final Setting setting, final TextField textField) {
		final Button add = VaadinWidgetFactory.createListAddButton(setting, textField);
		this.toolbarLayout.addComponent(add);
		this.toolbarLayout.setComponentAlignment(add, Alignment.TOP_RIGHT);
		return add;
	}

	private TextField createTextfield() {
		final TextField textField = new TextField();
		textField.setNullRepresentation("");
		textField.setWidth(100, Unit.PERCENTAGE);
		this.toolbarLayout.addComponent(textField);
		this.toolbarLayout.setExpandRatio(textField, 1.0f);
		return textField;
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

	@Override
	protected void createContainerPropery(IndexedContainer container) {
		container.addContainerProperty(VALUE_COLUMN, String.class, null);
		container.addContainerProperty(REMOVE_COLUMN, Button.class, null);
	}

	@Override
	protected HorizontalLayout createToolbar(boolean caption) {
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		return horizontalLayout;
	}
}
