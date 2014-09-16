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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.core.vaadin.converter.EObjectToBooleanConverter;
import org.eclipse.emf.ecp.view.model.vaadin.AbstractControlRendererVaadin;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class TableRendererVaadin extends AbstractControlRendererVaadin<VTableControl> {

	private static class TableContentUpdateAdaper extends AdapterImpl {

		private Table table;

		public TableContentUpdateAdaper(Table table) {
			this.table = table;
		}

		@Override
		public void notifyChanged(Notification msg) {
			table.refreshRowCache();
		}
	}

	private class TableListDiffVisitor extends ListDiffVisitor {

		private Table table;

		public TableListDiffVisitor(Table table) {
			this.table = table;
		}

		@Override
		public void handleAdd(int index, Object element) {
			((EObject) element).eAdapters().add(new TableContentUpdateAdaper(table));
		}

		@Override
		public void handleRemove(int index, Object element) {
			removeTableContentAdapter((EObject) element);
		}

	}

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
		layout.setSizeFull();

		final Table table = new Table();
		table.setSelectable(true);
		table.setSizeFull();

		final EClass clazz = ((EReference) setting.getEStructuralFeature()).getEReferenceType();
		BeanItemContainer<Object> indexedContainer = new BeanItemContainer(clazz.getInstanceClass());

		setVisibleColumns(control, table, clazz, indexedContainer);
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
		emfUpdateValueStrategy.setConverter(new EObjectToBooleanConverter());

		HorizontalLayout horizontalLayout = new HorizontalLayout();

		IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(table,
				clazz.getInstanceClass());
		if (!control.isAddRemoveDisabled()) {
			Button add = createAddButton(setting, table);
			horizontalLayout.addComponent(add);
			Button remove = createRemoveButton(setting, table);
			horizontalLayout.addComponent(remove);
			dataBindingContext.bindValue(VaadinObservables.observeEnabled(remove), observeSingleSelection, null,
					emfUpdateValueStrategy);
		}

		if (control.isEnableDetailEditingDialog()) {
			Button edit = createEditButton(control, table);
			horizontalLayout.addComponent(edit);
			dataBindingContext.bindValue(VaadinObservables.observeEnabled(edit), observeSingleSelection, null,
					emfUpdateValueStrategy);
		}
		layout.addComponent(horizontalLayout, 0);
		layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
		layout.addComponent(table, 1);
		return layout;

	}

	private void setVisibleColumns(VTableControl control, final Table table, final EClass clazz,
			BeanItemContainer<Object> indexedContainer) {
		List<EStructuralFeature> listFeatures = VaadinRendererUtil.getColumnFeatures(control);
		final InternalEObject tempInstance = getInstanceOf(clazz);
		List<String> visibleColumnsNames = new ArrayList<String>();
		List<String> visibleColumnsId = new ArrayList<String>();
		for (EStructuralFeature eStructuralFeature : listFeatures) {
			IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(tempInstance,
					eStructuralFeature);
			String displayName = itemPropertyDescriptor.getDisplayName(null);
			visibleColumnsNames.add(displayName);
			visibleColumnsId.add(eStructuralFeature.getName());
		}
		table.setContainerDataSource(indexedContainer);
		table.setVisibleColumns(visibleColumnsId.toArray(new Object[visibleColumnsId.size()]));
		table.setColumnHeaders(visibleColumnsNames.toArray(new String[visibleColumnsNames.size()]));
	}

	private Button createEditButton(final VTableControl control, final Table table) {
		Button edit = new Button();
		edit.addStyleName("table-edit");
		edit.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				EditDialog editDialog = new EditDialog((EObject) table.getValue(), control);
				UI.getCurrent().addWindow(editDialog);
			}
		});
		return edit;
	}

	private List<Object> getItems(final Setting setting) {
		return (List<Object>) setting.getEObject().eGet(setting.getEStructuralFeature());
	}

	private void removeTableContentAdapter(EObject selectedValue) {
		for (Iterator<Adapter> iterator = selectedValue.eAdapters().iterator(); iterator.hasNext();) {
			if (iterator.next() instanceof TableContentUpdateAdaper) {
				iterator.remove();
			}
		}
	}

	private Button createRemoveButton(final Setting setting, final Table table) {
		Button remove = new Button();
		remove.addStyleName("table-remove");
		final List<Object> items = getItems(setting);
		remove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				EObject selectedValue = (EObject) table.getValue();
				if (selectedValue instanceof Collection) {
					items.removeAll((Collection<?>) selectedValue);
					return;
				}
				items.remove(selectedValue);
			}

		});

		return remove;
	}

	@Override
	protected Component getControlComponent(Component component) {
		VerticalLayout layout = (VerticalLayout) component;
		if (layout.getComponentCount() > 1) {

			return layout.getComponent(1);
		}
		return layout.getComponent(0);
	}

	private Button createAddButton(final Setting setting, final Table table) {
		Button add = new Button();
		add.addStyleName("table-add");
		final List<Object> items = getItems(setting);
		add.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				EObject addItem = addItem(setting);
				items.add(addItem);
				table.select(addItem);
			}
		});
		return add;
	}

}
