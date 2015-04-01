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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

/**
 * Refresh a Vaadin row for the databinding. There is currently no other way to refresh a attribute in a table if the
 * attribute changed
 *
 * @author Dennis Melzer
 *
 */
public class TableContentUpdateAdaper extends AdapterImpl {

	private final Table table;

	/**
	 * Constrcutor.
	 *
	 * @param table the table to be refreshed
	 */
	public TableContentUpdateAdaper(Table table) {
		this.table = table;
	}

	@Override
	public void notifyChanged(Notification msg) {
		if (table.isEditable()) {
			return;
		}
		final UI ui = table.getUI();
		if (ui == null) {
			return;
		}
		ui.access(new Runnable() {

			@Override
			public void run() {
				table.refreshRowCache();

			}
		});

	}
}