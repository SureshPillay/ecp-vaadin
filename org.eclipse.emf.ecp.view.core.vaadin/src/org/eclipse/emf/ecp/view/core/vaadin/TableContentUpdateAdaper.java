package org.eclipse.emf.ecp.view.core.vaadin;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

public class TableContentUpdateAdaper extends AdapterImpl {

	private Table table;

	public TableContentUpdateAdaper(Table table) {
		this.table = table;
	}

	@Override
	public void notifyChanged(Notification msg) {
		UI ui = this.table.getUI();
		if (ui == null) {
			return;
		}
		ui.access(new Runnable() {

			@Override
			public void run() {
				TableContentUpdateAdaper.this.table.refreshRowCache();

			}
		});

	}
}