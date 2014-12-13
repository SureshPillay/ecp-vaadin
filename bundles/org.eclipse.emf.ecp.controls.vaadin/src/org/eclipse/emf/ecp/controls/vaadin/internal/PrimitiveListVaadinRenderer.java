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

import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
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
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.core.vaadin.converter.StringToVaadinConverter;
import org.eclipse.emf.ecp.view.core.vaadin.converter.VaadinConverterToString;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * The Vaadin Renderer for a primitive reference (EAttribute).
 *
 * @author Dennis Melzer
 *
 */
public class PrimitiveListVaadinRenderer extends AbstractVaadinList {

	private static final String VALUE_COLUMN = "value"; //$NON-NLS-1$

	@Override
	public void renderList(VerticalLayout layout) {
		final TextField textField = createTextfield();
		final Converter<String, Object> converter = createConverter(getSetting(), getTable(), textField);

		final Button add = createAddButton(getSetting(), textField);
		layout.addComponent(getTable());
		layout.setData(getTable());
		layout.addComponent(getToolbar());
		bindControls(textField, converter, add);
	}

	private Class<? extends EObject> bindTable(EStructuralFeature eStructuralFeature, Class<? extends EObject> clazz) {
		getTable().addItems(getSetting().getEObject().eGet(eStructuralFeature));
		final IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(getTable(), clazz);
		final IObservableList modelValue = EMFProperties.list(eStructuralFeature).observe(getSetting().getEObject());
		bindModelToTarget(targetValue, modelValue);
		return clazz;
	}

	private void bindControls(final TextField textField,
		final Converter<String, Object> converter, final Button add) {
		final Class<? extends EObject> clazz = getSetting().getEObject().getClass();
		final EStructuralFeature eStructuralFeature = getSetting().getEStructuralFeature();
		bindTable(eStructuralFeature, clazz);
		bindAddTextfield(getSetting(), textField, converter);
		bindTextfieldFocus(clazz, textField);
		bindVisibleAddButton(clazz, add);
	}

	private void bindVisibleAddButton(Class<? extends EObject> clazz, final Button add) {
		final IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(getTable(), clazz);
		final UpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter(false));
		getBindingContext().bindValue(VaadinObservables.observeVisible(add), observeSingleSelection, null,
			emfUpdateValueStrategy);
	}

	private void bindTextfieldFocus(Class<? extends EObject> clazz, final TextField textField) {
		final IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(getTable(), clazz);
		final UpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());
		getBindingContext().bindValue(VaadinObservables.observeFocus(textField), observeSingleSelection, null,
			emfUpdateValueStrategy);
	}

	private void bindAddTextfield(final Setting setting, final TextField textField,
		final Converter<String, Object> converter) {
		final IObservableValue targetValueList = VaadinObservables.observeValue(textField);
		final UpdateValueStrategy emfUpdateValueStrategy = new UpdateValueStrategy();
		final UpdateValueStrategy emfUpdateValueStrategyModel = new UpdateValueStrategy();
		if (converter != null) {
			emfUpdateValueStrategy.setConverter(new StringToVaadinConverter(converter));
			emfUpdateValueStrategy.setConverter(new VaadinConverterToString(converter));
		}
		targetValueList.addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(org.eclipse.core.databinding.observable.value.ValueChangeEvent event) {
				try {
					final Object fieldValue = getConvertedValue(textField);
					final ValueDiff diff = event.diff;
					final EditingDomain editingDomain = getEditingDomain(setting);

					if (PrimitiveListVaadinRenderer.this.getTable().getValue() != null
						&& !PrimitiveListVaadinRenderer.this.getTable().getValue().equals(fieldValue)
						&& diff.getOldValue() != diff.getNewValue()) {
						final Object convertedValue = getConvertedValue(diff.getOldValue(), converter);
						final Object convertToModel = getConvertedValue(diff.getNewValue(), converter);
						editingDomain.getCommandStack().execute(
							ReplaceCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(),
								convertedValue, Arrays.asList(convertToModel)));
					}
				} catch (final com.vaadin.data.util.converter.Converter.ConversionException e) {
					return;
				}

			}
		});

		final IObservableValue modelValueList = VaadinObservables.observeValue(getTable());
		bindModelToTarget(modelValueList, targetValueList, emfUpdateValueStrategy, emfUpdateValueStrategyModel);
	}

	private Button createAddButton(final Setting setting, final TextField textField) {
		final Button add = VaadinWidgetFactory.createListAddButton(setting, textField);
		getToolbar().addComponent(add);
		getToolbar().setComponentAlignment(add, Alignment.TOP_RIGHT);
		return add;
	}

	private TextField createTextfield() {
		final TextField textField = new TextField();
		textField.setNullRepresentation(StringUtils.EMPTY);
		textField.setWidth(100, Unit.PERCENTAGE);
		getToolbar().addComponent(textField);
		getToolbar().setExpandRatio(textField, 1.0f);
		return textField;
	}

	private Object getConvertedValue(TextField textField) {
		final Converter<String, Object> converter = textField.getConverter();
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
		} catch (final com.vaadin.data.util.converter.Converter.ConversionException exception) {
			return null;
		}
	}

	private Converter<String, Object> createConverter(Setting setting, final Table table,
		final TextField textField) {
		VaadinRendererUtil.setConverterToTextField(setting.getEStructuralFeature(), textField, getVElement(),
			getViewModelContext());

		if (textField.getConverter() == null) {
			return null;
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
	protected void createContainerProperty(IndexedContainer container) {
		container.addContainerProperty(VALUE_COLUMN, String.class, null);
		container.addContainerProperty(REMOVE_COLUMN, Button.class, null);
	}

	@Override
	protected HorizontalLayout createToolbar() {
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		return horizontalLayout;
	}
}
