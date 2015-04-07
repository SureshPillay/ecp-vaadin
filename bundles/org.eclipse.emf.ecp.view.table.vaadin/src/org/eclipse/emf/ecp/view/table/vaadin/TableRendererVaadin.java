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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractControlRendererVaadin;
import org.eclipse.emf.ecp.view.core.vaadin.SingleRowFieldFactory;
import org.eclipse.emf.ecp.view.core.vaadin.TableListDiffVisitor;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Vaadin Renderer for {@link VTableControl}.
 *
 * @author Dennis Melzer
 *
 */
public class TableRendererVaadin extends AbstractControlRendererVaadin<VTableControl> {

	private static final String TABLE_BUTTON_TOOLBAR = "table-button-toolbar"; //$NON-NLS-1$
	protected Setting setting;
	protected Table table;
	protected VerticalLayout layout;

	private InternalEObject getInstanceOf(EClass clazz) {
		return InternalEObject.class.cast(clazz.getEPackage().getEFactoryInstance().create(clazz));
	}

	@Override
	protected Component render() {
		final VTableControl control = getVElement();
		final ViewModelContext viewContext = getViewModelContext();
		final Iterator<Setting> iterator = control.getDomainModelReference().getIterator();
		if (!iterator.hasNext()) {
			return null;
		}
		setting = iterator.next();

		layout = new VerticalLayout();
		table = createTable();
		layout.setData(table);

		setVisibleColumns(control, viewContext);

		bindTable(setting, table);

		if (!control.isReadonly()) {

			table.setImmediate(true);
			table.setTableFieldFactory(new SingleRowFieldFactory());

			final HorizontalLayout horizontalLayout = createButtonBar();

			layout.addComponent(horizontalLayout);
			layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
		}
		layout.addComponent(table);

		final AbstractField<Object> customField = new CustomField<Object>() {

			private static final long serialVersionUID = 3108541513496899657L;

			@Override
			protected Component initContent() {
				return layout;
			}

			@Override
			public Class<? extends Object> getType() {
				return Object.class;
			}
		};
		return customField;
	}

	protected HorizontalLayout createButtonBar() {
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		addTableToolbarStyle(horizontalLayout);
		final boolean addRemoveDisable = !getVElement().isAddRemoveDisabled();
		if (addRemoveDisable) {
			createAddButton(horizontalLayout);
		}
		VaadinWidgetFactory.createTableActionColumn(setting, table, addRemoveDisable);
		return horizontalLayout;
	}

	private void createAddButton(HorizontalLayout horizontalLayout) {
		final Button add = VaadinWidgetFactory.createTableAddButton(setting, table);
		horizontalLayout.addComponent(add);
	}

	protected void bindButtonEnable(Button button) {
		final EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy();
		strategy.setConverter(new SelectionConverter());
		final IObservableValue observeSingleSelection = VaadinObservables.observeSingleSelection(table,
			getReferenceType().getInstanceClass());
		getBindingContext().bindValue(VaadinObservables.observeEnabled(button), observeSingleSelection, null, strategy);
	}

	private void addTableToolbarStyle(HorizontalLayout horizontalLayout) {
		if (hasCaption()) {
			horizontalLayout.addStyleName(TABLE_BUTTON_TOOLBAR);
		}
	}

	private void bindTable(final Setting setting, final Table table) {
		final IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(table, setting
			.getEObject()
			.getClass());
		targetValue.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				event.diff.accept(new TableListDiffVisitor(table));
			}
		});

		final IObservableList modelValue = EMFEditProperties.list(getEditingDomain(setting),
			setting.getEStructuralFeature()).observe(
			setting.getEObject());
		getBindingContext().bindList(targetValue, modelValue);
	}

	protected EditingDomain getEditingDomain(Setting setting) {
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	private Table createTable() {
		final Table table = new Table();
		table.setSelectable(true);
		table.setSizeFull();

		final EClass clazz = getReferenceType();
		final BeanItemContainer<Object> indexedContainer = new BeanItemContainer(clazz.getInstanceClass());
		table.setContainerDataSource(indexedContainer);
		return table;
	}

	private EClass getReferenceType() {
		return ((EReference) setting.getEStructuralFeature()).getEReferenceType();
	}

	private void setVisibleColumns(VTableControl control, ViewModelContext viewContext) {
		final List<EStructuralFeature> listFeatures = VaadinRendererUtil.getColumnFeatures(control);
		final InternalEObject tempInstance = getInstanceOf(getReferenceType());
		final List<String> visibleColumnsNames = new ArrayList<String>();
		final List<String> visibleColumnsId = new ArrayList<String>();
		for (final EStructuralFeature eStructuralFeature : listFeatures) {
			final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(
				tempInstance,
				eStructuralFeature);
			String displayName = eStructuralFeature.getName();
			// String tooltipText = eStructuralFeature.getName();

			if (itemPropertyDescriptor != null) {
				displayName = itemPropertyDescriptor.getDisplayName(null);
			}
			visibleColumnsNames.add(displayName);
			visibleColumnsId.add(eStructuralFeature.getName());

		}
		// TODO: FIXME 2 gleiche coloumns?
		table.setVisibleColumns(visibleColumnsId.toArray(new Object[visibleColumnsId.size()]));
		table.setColumnHeaders(visibleColumnsNames.toArray(new String[visibleColumnsNames.size()]));
	}

	public VView getView() {
		VView detailView = getVElement().getDetailView();
		if (detailView == null) {
			detailView = ViewProviderHelper.getView((EObject) table.getValue(), null);
		}
		return EcoreUtil.copy(detailView);
	}

}
