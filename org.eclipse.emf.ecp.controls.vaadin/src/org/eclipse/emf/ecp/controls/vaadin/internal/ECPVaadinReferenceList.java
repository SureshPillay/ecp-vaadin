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
import org.eclipse.emf.ecp.view.core.vaadin.TableListDiffVisitor;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.model.common.edit.provider.CustomReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
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
import com.vaadin.ui.VerticalLayout;

public class ECPVaadinReferenceList extends AbstractVaadinList {

	private static final String LINK_COLUMN = "link";

	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	@Override
	public Component createControl(VControl control, ViewModelContext viewContext, Setting setting) {
		return null;
	}

	@Override
	public void renderList(VerticalLayout layout) {
		this.composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
				new CustomReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		this.adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(this.composedAdapterFactory);
		createLinkColumn();
		bindTable();
		layout.addComponent(this.toolbarLayout);
		layout.addComponent(this.table);
		layout.setData(this.table);
		layout.setComponentAlignment(this.toolbarLayout, Alignment.TOP_RIGHT);
	}

	private void bindTable() {
		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(this.table, this.setting
				.getEObject().getClass());
		targetValue.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				event.diff.accept(new TableListDiffVisitor(ECPVaadinReferenceList.this.table));
			}
		});
		IObservableList modelValue = EMFProperties.list(this.setting.getEStructuralFeature()).observe(
				this.setting.getEObject());
		EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
		dataBindingContext.bindList(targetValue, modelValue);
		EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());
	}

	private void createLinkColumn() {
		this.table.addGeneratedColumn(LINK_COLUMN, new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				if (!(itemId instanceof EObject)) {
					return null;
				}
				VView view = ViewProviderHelper.getView((EObject) itemId, null);
				String text = ECPVaadinReferenceList.this.adapterFactoryItemDelegator.getText(itemId);
				if (view == null) {
					return text;
				}

				return VaadinWidgetFactory.createEditLink((EObject) itemId, text);
			}
		});
	}

	@Override
	protected HorizontalLayout createToolbar(boolean caption) {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		if (caption) {
			horizontalLayout.addStyleName("table-button-toolbar");
		}

		Button add = VaadinWidgetFactory.createTableAddButton(this.setting, this.table);
		horizontalLayout.addComponent(add);
		return horizontalLayout;
	}

	@Override
	public void dispose() {
		if (this.composedAdapterFactory != null) {
			this.composedAdapterFactory.dispose();
		}
		this.composedAdapterFactory = null;
		super.dispose();
	}

	@Override
	protected void createContainerPropery(IndexedContainer container) {
		container.addContainerProperty(LINK_COLUMN, Button.class, null);
		container.addContainerProperty(REMOVE_COLUMN, Button.class, null);

	}
}
