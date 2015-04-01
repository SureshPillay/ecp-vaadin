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

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.SingleRowFieldFactory;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.ErrorMessageComponentConverter;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
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

		final Button add = createAddButton(getSetting(), textField);
		layout.addComponent(getTable());
		layout.setData(getTable());
		layout.addComponent(getToolbar());
		bindControls(textField, add);

		getTable().addShortcutListener(new ShortcutListener("Edit item", KeyCode.ENTER, null) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				if (target instanceof Table) {
					((Table) target).setEditable(true);
				}
			}
		});
		getTable().addShortcutListener(new ShortcutListener("Stop item editing", KeyCode.ESCAPE, null) {
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				getTable().setEditable(false);
			}
		});
		getTable().addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				getTable().setEditable(false);
			}
		});
		getTable().setTableFieldFactory(new SingleRowFieldFactory());
		getTable().setImmediate(true);
	}

	private Class<? extends EObject> bindTable(EStructuralFeature eStructuralFeature, Class<? extends EObject> clazz) {
		getTable().addItems(getSetting().getEObject().eGet(eStructuralFeature));
		// final IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(getTable(), clazz);
		// targetValue.addListChangeListener(new IListChangeListener() {
		//
		// @Override
		// public void handleListChange(ListChangeEvent event) {
		// event.diff.accept(new TableListDiffVisitor(getTable()));
		// }
		// });
		// final IObservableList modelValue = EMFEditProperties.list(getEditingDomain(getSetting()), eStructuralFeature)
		// .observe(getSetting().getEObject());
		// bindModelToTarget(targetValue, modelValue);
		final IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(getTable(), getSetting()
			.getEObject()
			.getClass());
		targetValue.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				// event.diff.accept(new TableListDiffVisitor(getTable()));
				event.diff.accept(new ListDiffVisitor() {

					@Override
					public void handleRemove(int index, Object element) {

					}

					@SuppressWarnings("unchecked")
					@Override
					public void handleAdd(int index, Object element) {
						getTable().getContainerDataSource().getItem(element).getItemProperty(VALUE_COLUMN)
						.setValue(element);

					}
				});

			}
		});

		final IObservableList modelValue = EMFEditProperties.list(getEditingDomain(getSetting()),
			getSetting().getEStructuralFeature()).observe(
				getSetting().getEObject());
		getBindingContext().bindList(targetValue, modelValue);

		return clazz;
	}

	private void bindControls(final TextField textField, final Button add) {
		final Class<? extends EObject> clazz = getSetting().getEObject().getClass();
		final EStructuralFeature eStructuralFeature = getSetting().getEStructuralFeature();
		bindTable(eStructuralFeature, clazz);
		bindEnableAddButton(textField, add);

	}

	private void bindEnableAddButton(final TextField textField, final Button add) {
		if (textField.getConverter() == null) {
			return;
		}
		final IObservableValue model = VaadinObservables.observeValue(textField);
		final IObservableValue target = VaadinObservables.observeEnabled(add);
		final UpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new ErrorMessageComponentConverter(textField));
		bindModelToTarget(target, model, null, emfUpdateValueStrategy);
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
		VaadinRendererUtil.setConverterToTextField(getSetting().getEStructuralFeature(), textField, getVElement(),
			getViewModelContext());
		return textField;
	}

	@Override
	protected void createContainerProperty(IndexedContainer container) {
		Class<?> clazz = ((EAttribute) getSetting().getEStructuralFeature()).getEAttributeType()
			.getInstanceClass();

		if (clazz.isPrimitive()) {
			clazz = ClassUtils.primitiveToWrapper(clazz);
		}

		container.addContainerProperty(VALUE_COLUMN, clazz, null);
	}

	@Override
	protected HorizontalLayout createToolbar() {
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(100, Unit.PERCENTAGE);
		return horizontalLayout;
	}
}
