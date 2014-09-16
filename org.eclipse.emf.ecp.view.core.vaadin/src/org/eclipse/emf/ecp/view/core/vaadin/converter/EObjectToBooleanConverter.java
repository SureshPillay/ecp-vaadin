package org.eclipse.emf.ecp.view.core.vaadin.converter;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.ecore.EObject;

public class EObjectToBooleanConverter implements IConverter {

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
		return fromObject != null;
	}

}
