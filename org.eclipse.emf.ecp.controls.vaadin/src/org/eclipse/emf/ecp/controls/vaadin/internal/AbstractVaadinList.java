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
import org.eclipse.emf.ecp.controls.vaadin.VaadinSimpleControlRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinWidgetFactory;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractVaadinList extends VaadinSimpleControlRenderer {

	protected static final String REMOVE_COLUMN = "remove";
	protected Table table;
	protected Setting setting;
	protected HorizontalLayout toolbarLayout;

	@Override
	public Component createControl() {
		return null;
	}

	@Override
	public VerticalLayout render() {
		this.setting = getVElement().getDomainModelReference().getIterator().next();
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		this.table = createTable();
		createRemoveColumn(this.setting);

		this.toolbarLayout = createToolbar();
		renderList(layout);
		return layout;
	}

	private void createRemoveColumn(final Setting setting) {
		this.table.addGeneratedColumn(REMOVE_COLUMN, new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				return VaadinWidgetFactory.createTableRemoveButtonFlat(setting, itemId);
			}
		});
		this.table.setColumnWidth(REMOVE_COLUMN, 40);
	}

	private IndexedContainer createContainer() {
		IndexedContainer container = new IndexedContainer();
		createContainerPropery(container);
		return container;
	}

	private Table createTable() {
		final Table table = new Table();
		table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		table.setSizeFull();
		table.setSelectable(true);
		table.addStyleName("reference-list");
		IndexedContainer container = createContainer();
		table.setContainerDataSource(container);
		return table;
	}

	protected abstract void createContainerPropery(IndexedContainer container);

	protected abstract HorizontalLayout createToolbar();

	protected abstract void renderList(VerticalLayout layout);

}
