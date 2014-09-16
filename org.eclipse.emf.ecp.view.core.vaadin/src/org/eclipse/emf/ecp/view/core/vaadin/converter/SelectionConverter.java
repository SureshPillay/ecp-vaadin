package org.eclipse.emf.ecp.view.core.vaadin.converter;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.ecore.EObject;

public class SelectionConverter implements IConverter {

	private boolean selectionEnable;

	public SelectionConverter() {
		this(true);
	}

	public SelectionConverter(boolean selectionEnable) {
		this.selectionEnable = selectionEnable;
	}

	@Override
	public Object getToType() {
		return Boolean.class;
	}

	@Override
	public Object getFromType() {
		return EObject.class;
	}

	@Override
	public Object convert(Object fromObject) {
		if (selectionEnable) {
			return fromObject != null;
		}

		return fromObject == null;
	}

}
