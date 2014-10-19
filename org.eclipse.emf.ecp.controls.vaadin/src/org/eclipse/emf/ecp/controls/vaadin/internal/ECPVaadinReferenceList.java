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

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.ECPControlFactoryVaadin;
import org.eclipse.emf.ecp.view.core.vaadin.TableListDiffVisitor;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.model.common.edit.provider.CustomReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.VerticalLayout;

public class ECPVaadinReferenceList extends ECPControlFactoryVaadin {

	private static final String REMOVE_COLUMN = "remove";
	private static final String LINK_COLUMN = "link";

	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	@Override
	public Component createControl(VControl control, ViewModelContext viewContext, Setting setting) {
		return null;
	}

	@Override
	public Component render(final VControl control, ViewModelContext viewContext, boolean caption) {
		final Setting setting = control.getDomainModelReference().getIterator().next();
		this.composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
				new CustomReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		this.adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(this.composedAdapterFactory);
		VerticalLayout layout = new VerticalLayout();
		final Table table = new Table();
		table.addStyleName("reference-list");
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		table.setSizeFull();
		table.setNullSelectionAllowed(false);
		table.setSelectable(false);
		IndexedContainer container = new IndexedContainer();

		// Define the properties (columns)
		container.addContainerProperty(LINK_COLUMN, Button.class, null);
		container.addContainerProperty(REMOVE_COLUMN, Button.class, null);
		table.addGeneratedColumn(LINK_COLUMN, new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				return VaadinWidgetFactory.createEditLink((EObject) itemId,
						ECPVaadinReferenceList.this.adapterFactoryItemDelegator.getText(itemId));
			}
		});
		table.addGeneratedColumn(REMOVE_COLUMN, new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				return VaadinWidgetFactory.createTableRemoveButtonFlat(setting, itemId);
			}
		});
		table.setContainerDataSource(container);
		table.setColumnWidth(REMOVE_COLUMN, 40);

		table.addItems(setting.getEObject().eGet(setting.getEStructuralFeature()));

		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(table, setting.getEObject()
				.getClass());
		targetValue.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				event.diff.accept(new TableListDiffVisitor(table));
			}
		});
		IObservableList modelValue = EMFProperties.list(setting.getEStructuralFeature()).observe(setting.getEObject());
		EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
		dataBindingContext.bindList(targetValue, modelValue);
		EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		if (caption) {
			horizontalLayout.addStyleName("table-button-toolbar");
		}
		layout.addComponent(horizontalLayout);

		Button add = VaadinWidgetFactory.createTableAddButton(setting, table);
		horizontalLayout.addComponent(add);

		layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
		layout.addComponent(table);
		layout.setData(table);
		return layout;
	}

	@Override
	public void dispose() {
		if (this.composedAdapterFactory != null) {
			this.composedAdapterFactory.dispose();
		}
		this.composedAdapterFactory = null;
		super.dispose();
	}
}
