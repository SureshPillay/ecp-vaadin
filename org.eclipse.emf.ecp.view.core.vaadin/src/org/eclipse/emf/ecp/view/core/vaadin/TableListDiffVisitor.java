package org.eclipse.emf.ecp.view.core.vaadin;

import java.util.Iterator;

import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;

import com.vaadin.ui.Table;

public class TableListDiffVisitor extends ListDiffVisitor {

	private Table table;

	public TableListDiffVisitor(Table table) {
		this.table = table;
	}

	@Override
	public void handleAdd(int index, Object element) {
		((EObject) element).eAdapters().add(new TableContentUpdateAdaper(this.table));
	}

	@Override
	public void handleRemove(int index, Object element) {
		if (!(element instanceof EObject)) {
			return;
		}
		removeTableContentAdapter((EObject) element);
	}

	private void removeTableContentAdapter(EObject selectedValue) {
		for (Iterator<Adapter> iterator = selectedValue.eAdapters().iterator(); iterator.hasNext();) {
			if (iterator.next() instanceof TableContentUpdateAdaper) {
				iterator.remove();
			}
		}
	}

	public static boolean containsTableContentAdapter(EObject selectedValue) {
		for (Iterator<Adapter> iterator = selectedValue.eAdapters().iterator(); iterator.hasNext();) {
			if (iterator.next() instanceof TableContentUpdateAdaper) {
				return true;
			}
		}
		return false;
	}

}