package org.eclipse.emf.ecp.view.model.vaadin.validator;

import org.apache.commons.lang.StringUtils;

import com.vaadin.ui.AbstractField;

public class ECPVaadinEmptyTextValidator extends AbstractFieldValidator<AbstractField<?>> {
	private static final long serialVersionUID = 8461910467127110816L;

	public ECPVaadinEmptyTextValidator(AbstractField<?> abstractField) {
		super(abstractField);
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String stringValue = value.toString();
		if (StringUtils.isEmpty(stringValue) || stringValue.length() < 1) {
			throw new InvalidValueException(errorMessage);
		}

	}

}
