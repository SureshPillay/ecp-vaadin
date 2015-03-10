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

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.controls.vaadin.AbstractVaadinSimpleControlRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract Control for a Vaadin List.
 *
 * @author Dennis Melzer
 *
 */
public abstract class AbstractVaadinList extends AbstractVaadinSimpleControlRenderer {

	/** The remove column. */
	protected static final String REMOVE_COLUMN = "remove"; //$NON-NLS-1$
	private static final String REFERENCE_LIST = "reference-list"; //$NON-NLS-1$
	private static final int TABLE_HEIGHT = 120;
	private Table table;
	private Setting setting;
	private HorizontalLayout toolbar;

	@Override
	public Component createControl() {
		return null;
	}

	@Override
	public VerticalLayout render() {
		setting = getVElement().getDomainModelReference().getIterator().next();
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		table = createTable();
		createRemoveColumn(setting);

		toolbar = createToolbar();
		renderList(layout);
		return layout;
	}

	private void createRemoveColumn(final Setting setting) {
		table.addGeneratedColumn(REMOVE_COLUMN, new ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				return VaadinWidgetFactory.createTableRemoveButtonFlat(setting, itemId);
			}
		});
		table.setColumnAlignment(REMOVE_COLUMN, Align.RIGHT);
		table.setColumnWidth(REMOVE_COLUMN, 40);
	}

	private IndexedContainer createContainer() {
		final IndexedContainer container = new IndexedContainer();
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
