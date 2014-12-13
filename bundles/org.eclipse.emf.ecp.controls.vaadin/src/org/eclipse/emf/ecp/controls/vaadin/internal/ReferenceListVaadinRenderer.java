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
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.TableListDiffVisitor;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.model.common.edit.provider.CustomReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

/**
 * The Vaadin Renderer for a reference (ERefernce).
 *
 * @author Dennis Melzer
 *
 */
public class ReferenceListVaadinRenderer extends AbstractVaadinList {

	private static final String TABLE_BUTTON_TOOLBAR_STYLE = "table-button-toolbar"; //$NON-NLS-1$

	private static final String LINK_COLUMN = "link"; //$NON-NLS-1$

	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;

	@Override
	public void renderList(VerticalLayout layout) {
		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new CustomReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
		createLinkColumn();
		bindTable();
		layout.addComponent(getToolbar());
		layout.addComponent(getTable());
		layout.setData(getTable());
		layout.setComponentAlignment(getToolbar(), Alignment.TOP_RIGHT);
	}

	private void bindTable() {
		final Setting setting = getSetting();
		final IObservableList targetValue = VaadinObservables.observeContainerItemSetContents(getTable(), setting
			.getEObject().getClass());
		targetValue.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				event.diff.accept(new TableListDiffVisitor(ReferenceListVaadinRenderer.this.getTable()));
			}
		});
		final IObservableList modelValue = EMFEditProperties.list(getEditingDomain(setting),
			setting.getEStructuralFeature())
			.observe(
				setting.getEObject());
		final EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
		dataBindingContext.bindList(targetValue, modelValue);
		final EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());
	}

	private void createLinkColumn() {
		getTable().addGeneratedColumn(LINK_COLUMN, new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				if (!(itemId instanceof EObject)) {
					return null;
				}
				final VView view = ViewProviderHelper.getView((EObject) itemId, null);
				final String text = adapterFactoryItemDelegator.getText(itemId);
				if (view == null) {
					return text;
				}

				return VaadinWidgetFactory.createEditLink((EObject) itemId, text);
			}
		});
	}

	@Override
	protected HorizontalLayout createToolbar() {
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		if (hasCaption()) {
			horizontalLayout.addStyleName(TABLE_BUTTON_TOOLBAR_STYLE);
		}

		final Button add = VaadinWidgetFactory.createTableAddButton(getSetting(), getTable());
		horizontalLayout.addComponent(add);
		return horizontalLayout;
	}

	@Override
	public void dispose() {
		if (composedAdapterFactory != null) {
			composedAdapterFactory.dispose();
		}
		composedAdapterFactory = null;
		super.dispose();
	}

	@Override
	protected void createContainerProperty(IndexedContainer container) {
		container.addContainerProperty(LINK_COLUMN, Button.class, null);
		container.addContainerProperty(REMOVE_COLUMN, Button.class, null);

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer#getUnsetLabel()
	 */
	@Override
	protected String getUnsetLabel() {
		// TODO Auto-generated method stub
		return super.getUnsetLabel();
	}
}
