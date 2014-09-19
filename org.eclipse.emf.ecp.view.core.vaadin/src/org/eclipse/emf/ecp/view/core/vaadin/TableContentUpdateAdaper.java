package org.eclipse.emf.ecp.view.core.vaadin;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import com.vaadin.ui.Table;

public class TableContentUpdateAdaper extends AdapterImpl {

	private Table table;

	public TableContentUpdateAdaper(Table table) {
		this.table = table;
	}

	@Override
	public void notifyChanged(Notification msg) {
		this.table.refreshRowCache();
	}
}