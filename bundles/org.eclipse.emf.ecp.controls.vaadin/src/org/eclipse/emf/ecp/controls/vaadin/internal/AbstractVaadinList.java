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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract Control for a Vaadin List.
 *
 * @author Dennis Melzer
 *
 */
public abstract class AbstractVaadinList extends AbstractVaadinSimpleControlRenderer {

	/** The style name for the table. */
	private static final String REFERENCE_LIST = "reference-list"; //$NON-NLS-1$
	private static final int TABLE_HEIGHT = 120;
	private Table table;
	private Setting setting;
	private HorizontalLayout toolbar;
	private IndexedContainer container;

	@Override
	public Component createControl() {
		return null;
	}

	@Override
	public Component render() {
		setting = getVElement().getDomainModelReference().getIterator().next();
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		table = createTable();
		if (getVElement().isReadonly()) {
			layout.addComponent(getTable());
			layout.setData(getTable());
			return layout;
		}

		VaadinWidgetFactory.createTableActionColumn(setting, table, true, isEditable());

		toolbar = createToolbar();
		renderList(layout);
		final AbstractField<Object> customField = new CustomField<Object>() {

			private static final long serialVersionUID = 7233527336146620279L;

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

	private boolean isEditable() {
		return EAttribute.class.isInstance(setting.getEStructuralFeature());
	}

	private IndexedContainer createContainer() {
		container = new IndexedContainer();
		createContainerProperty(container);
		return container;
	}

	private Table createTable() {
		final Table table = new Table();
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		table.setWidth(100, Unit.PERCENTAGE);
		table.setHeight(TABLE_HEIGHT, Unit.PIXELS);
		table.setSelectable(true);
		table.addStyleName(REFERENCE_LIST);
		final IndexedContainer container = createContainer();
		table.setContainerDataSource(container);
		return table;
	}

	/**
	 * Returns the setting.
	 *
	 * @return the setting
	 */
	public Setting getSetting() {
		return setting;
	}

	/**
	 * Returns the table.
	 *
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Returns the toolbar.
	 *
	 * @return the toolbar
	 */
	public HorizontalLayout getToolbar() {
		return toolbar;
	}

	/**
	 * Creates the container property for columns.
	 *
	 * @param container the table container
	 */
	protected abstract void createContainerProperty(IndexedContainer container);

	/**
	 * Creates the List toolbar.
	 *
	 * @return the layout
	 */
	protected abstract HorizontalLayout createToolbar();

	/**
	 * Render the list control.
	 *
	 * @param layout the parent layout
	 */
	protected abstract void renderList(VerticalLayout layout);

}
