package org.eclipse.emf.ecp.view.model.vaadin.validator;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;

public abstract class AbstractFieldValidator<T extends AbstractField<?>> implements Validator {

	private static final long serialVersionUID = 1278717813562229975L;
	protected T abstractField;
	protected String errorMessage;

	public AbstractFieldValidator(T abstractField) {
		this.abstractField = abstractField;
	}

	public void validateField() {
		abstractField.setValidationVisible(false);
		try {
			abstractField.validate();
		} catch (InvalidValueException e) {
			abstractField.setValidationVisible(true);
		}
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
