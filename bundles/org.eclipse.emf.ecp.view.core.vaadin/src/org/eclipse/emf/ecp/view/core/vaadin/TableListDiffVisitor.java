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
package org.eclipse.emf.ecp.view.core.vaadin;

import java.util.Iterator;

import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;

import com.vaadin.ui.Table;

/**
 * The List Diff Visitor for handling removing and adding object from a table to add the refresh row adapter.
 *
 * @author Dennis Melzer
 *
 */
public class TableListDiffVisitor extends ListDiffVisitor {

	private final Table table;

	/**
	 * Constructor.
	 *
	 * @param table the table
	 */
	public TableListDiffVisitor(Table table) {
		this.table = table;
	}

	@Override
	public void handleAdd(int index, Object element) {
		((EObject) element).eAdapters().add(new TableContentUpdateAdaper(table));
	}

	@Override
	public void handleRemove(int index, Object element) {
		if (!(element instanceof EObject)) {
			return;
		}
		removeTableContentAdapter((EObject) element);
	}

	private void removeTableContentAdapter(EObject selectedValue) {
		for (final Iterator<Adapter> iterator = selectedValue.eAdapters().iterator(); iterator.hasNext();) {
			if (iterator.next() instanceof TableContentUpdateAdaper) {
				iterator.remove();
			}
		}
	}

}