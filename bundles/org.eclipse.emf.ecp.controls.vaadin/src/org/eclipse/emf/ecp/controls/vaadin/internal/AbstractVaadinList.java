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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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

	/** The action column id. */
	protected static final String ACTION_COLUMN = "actions"; //$NON-NLS-1$

	/** The style name for the action button layout. */
	private static final String ACTION_BUTTONS = "action-buttons"; //$NON-NLS-1$

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

	private boolean isOrdered() {
		return getSetting().getEStructuralFeature().isOrdered();
	}

	@Override
	public VerticalLayout render() {
		setting = getVElement().getDomainModelReference().getIterator().next();
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		table = createTable();
		createActionColumn(setting);

		toolbar = createToolbar();
		renderList(layout);
		return layout;
	}

	private void createActionColumn(final Setting setting) {
		table.addGeneratedColumn(ACTION_COLUMN, new ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final int index = container.getItemIds().indexOf(itemId);
				final HorizontalLayout buttons = new HorizontalLayout();
				buttons.setStyleName(ACTION_BUTTONS);
				if (isOrdered()) {
					final Button moveUp = VaadinWidgetFactory
						.createTableMoveUpButtonOverlay(setting, itemId, index - 1);
					buttons.addComponent(moveUp);
					buttons.setComponentAlignment(moveUp, Alignment.MIDDLE_RIGHT);
					final Button moveDown = VaadinWidgetFactory.createTableMoveDownButtonOverlay(setting, itemId,
						index + 1);
					buttons.addComponent(moveDown);
					buttons.setComponentAlignment(moveDown, Alignment.MIDDLE_RIGHT);
				}
				final Button remove = VaadinWidgetFactory.createTableRemoveButtonOverlay(setting, itemId);
				buttons.addComponent(remove);
				buttons.setComponentAlignment(remove, Alignment.MIDDLE_RIGHT);
				return buttons;
			}
		});
		table.setColumnAlignment(ACTION_COLUMN, Align.RIGHT);
		table.setColumnWidth(ACTION_COLUMN, 0);
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
