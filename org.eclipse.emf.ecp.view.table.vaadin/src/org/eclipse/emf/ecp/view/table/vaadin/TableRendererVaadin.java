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
package org.eclipse.emf.ecp.view.table.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractControlRendererVaadin;
import org.eclipse.emf.ecp.view.core.vaadin.TableListDiffVisitor;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class TableRendererVaadin extends AbstractControlRendererVaadin<VTableControl> {

	protected EObject addItem(Setting setting) {
		final EClass clazz = ((EReference) setting.getEStructuralFeature()).getEReferenceType();
		final EObject instance = clazz.getEPackage().getEFactoryInstance().create(clazz);
		return instance;
	}

	private InternalEObject getInstanceOf(EClass clazz) {
		return InternalEObject.class.cast(clazz.getEPackage().getEFactoryInstance().create(clazz));
	}

	@Override
	protected Component renderControl(VTableControl control, ViewModelContext viewContext) {
		final Setting setting = control.getDomainModelReference().getIterator().next();

		VerticalLayout layout = new VerticalLayout();
		final Table table = new Table();
		layout.setData(table);
		table.setSelectable(true);
		table.setSizeFull();
		// table.setPageLength(10);

		final EClass clazz = ((EReference) setting.getEStructuralFeature()).getEReferenceType();
		BeanItemContainer<Object> indexedContainer = new BeanItemContainer(clazz.getInstanceClass());

		setVisibleColumns(control, table, clazz, indexedContainer, viewContext);
		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(table, setting.getEObject()
				.getClass());

		targetValue.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				// TODO: FIXME Databinding is correct https://vaadin.com/forum#!/thread/68419
				event.diff.accept(new TableListDiffVisitor(table));
			}
		});

		IObservableList modelValue = EMFProperties.list(setting.getEStructuralFeature()).observe(setting.getEObject());
		EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
		dataBindingContext.bindList(targetValue, modelValue);

		if (control.isReadonly()) {
			layout.addComponent(table);
			return layout;
		}
		EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		if (hasCaption(control)) {
			horizontalLayout.addStyleName("table-button-toolbar");
		}
		IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(table,
				clazz.getInstanceClass());
		if (!control.isAddRemoveDisabled()) {
			Button add = VaadinWidgetFactory.createTableAddButton(setting, table);
			horizontalLayout.addComponent(add);
			Button remove = VaadinWidgetFactory.createTableRemoveButton(setting, table);
			horizontalLayout.addComponent(remove);
			dataBindingContext.bindValue(VaadinObservables.observeEnabled(remove), observeSingleSelection, null,
					emfUpdateValueStrategy);
		}

		switch (control.getDetailEditing()) {
		case WITH_DIALOG:
			Button edit = VaadinWidgetFactory.createTableEditButton(table, control.getDetailView());
			edit.setEnabled(control.getDetailView() != null);
			horizontalLayout.addComponent(edit);
			dataBindingContext.bindValue(VaadinObservables.observeEnabled(edit), observeSingleSelection, null,
					emfUpdateValueStrategy);
			break;
		case WITH_PANEL:
			// TODO Master/Detail Panel
			throw new UnsupportedOperationException("WITH_PANEL is not implmented yet");
		default:
			break;
		}

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
		layout.addComponent(table);
		return layout;

	}

	private void setVisibleColumns(VTableControl control, final Table table, final EClass clazz,
			BeanItemContainer<Object> indexedContainer, ViewModelContext viewContext) {
		List<EStructuralFeature> listFeatures = VaadinRendererUtil.getColumnFeatures(control);
		final InternalEObject tempInstance = getInstanceOf(clazz);
		List<String> visibleColumnsNames = new ArrayList<String>();
		List<String> visibleColumnsId = new ArrayList<String>();
		for (final EStructuralFeature eStructuralFeature : listFeatures) {
			IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(tempInstance,
					eStructuralFeature);
			if (itemPropertyDescriptor == null) {
				continue;
			}
			String displayName = itemPropertyDescriptor.getDisplayName(null);
			visibleColumnsNames.add(displayName);
			visibleColumnsId.add(eStructuralFeature.getName());
			final TextField converter = new TextField();
			VaadinRendererUtil.setConverterToTextField(eStructuralFeature, converter, control, viewContext);
			if (converter.getConverter() == null) {
				return;
			}
			table.addGeneratedColumn(eStructuralFeature.getName(), new ColumnGenerator() {
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					EObject eObject = (EObject) itemId;
					return converter.getConverter().convertToPresentation(eObject.eGet(eStructuralFeature),
							String.class, Locale.getDefault());
				}
			});

		}
		table.setContainerDataSource(indexedContainer);
		table.setVisibleColumns(visibleColumnsId.toArray(new Object[visibleColumnsId.size()]));
		table.setColumnHeaders(visibleColumnsNames.toArray(new String[visibleColumnsNames.size()]));
	}

	private List<Object> getItems(final Setting setting) {
		return (List<Object>) setting.getEObject().eGet(setting.getEStructuralFeature());
	}

}
