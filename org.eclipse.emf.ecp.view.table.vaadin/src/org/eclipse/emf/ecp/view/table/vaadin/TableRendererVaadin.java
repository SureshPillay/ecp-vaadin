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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.vaadin.RendererVaadin;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class TableRendererVaadin extends RendererVaadin<VTableControl> {

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

	protected EObject addItem(Setting setting) {
		final EClass clazz = ((EReference) setting.getEStructuralFeature()).getEReferenceType();
		final EObject instance = clazz.getEPackage().getEFactoryInstance().create(clazz);
		return instance;
	}

	@Override
	public Component render(VTableControl control, ViewModelContext viewContext) {
		final Setting setting = control.getDomainModelReference().getIterator().next();

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final Table table = new Table();
		setCaption(control, table);
		table.setSelectable(true);
		table.setSizeFull();
		layout.addComponent(table);

		VaadinObservables.activateRealm(UI.getCurrent());
		final Class<?> clazz = ((EReference) setting.getEStructuralFeature()).getEReferenceType().getInstanceClass();
		BeanItemContainer<Object> newDataSource = new BeanItemContainer(clazz);
		final List<Object> items = (List<Object>) setting.getEObject().eGet(setting.getEStructuralFeature());
		newDataSource.addAll(items);
		table.setContainerDataSource(newDataSource);

		IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(table, setting.getEObject()
				.getClass());

		IObservableList modelValue = EMFProperties.list(setting.getEStructuralFeature()).observe(setting.getEObject());
		EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
		bindModelToTarget(dataBindingContext, targetValue, modelValue);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		layout.addComponent(horizontalLayout);
		Button add = new Button("Add");
		horizontalLayout.addComponent(add);

		Button remove = new Button("Remove");
		horizontalLayout.addComponent(remove);

		add.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				EObject addItem = addItem(setting);
				items.add(addItem);
				table.select(addItem);

				// TODO: FIXME Databinding is correct https://vaadin.com/forum#!/thread/68419
				addItem.eAdapters().add(new TableContentUpdateAdaper(table));
			}
		});

		remove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				EObject selectedValue = (EObject) table.getValue();

				// FIXME 1. Databinding is correct https://vaadin.com/forum#!/thread/68419
				// TODO 2. better solution?
				for (Iterator<Adapter> iterator = selectedValue.eAdapters().iterator(); iterator.hasNext();) {
					if (iterator.next() instanceof TableContentUpdateAdaper) {
						iterator.remove();
					}
				}

				if (selectedValue instanceof Collection) {
					items.removeAll((Collection<?>) selectedValue);
					return;
				}
				items.remove(selectedValue);
			}
		});
		return layout;
	}

	protected Binding bindModelToTarget(DataBindingContext dataBindingContext, IObservableList target,
			IObservableList model) {
		final Binding binding = dataBindingContext.bindList(target, model);
		binding.getValidationStatus().addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus statusNew = (IStatus) event.diff.getNewValue();
				if (IStatus.ERROR == statusNew.getSeverity()) {
					binding.updateModelToTarget();
				}
			}
		});

		return binding;
	}

}
