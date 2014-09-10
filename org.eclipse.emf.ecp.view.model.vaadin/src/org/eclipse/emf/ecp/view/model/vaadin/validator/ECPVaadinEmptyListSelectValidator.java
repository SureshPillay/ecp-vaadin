package org.eclipse.emf.ecp.view.model.vaadin.validator;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.vaadin.ui.AbstractSelect;

public class ECPVaadinEmptyListSelectValidator extends AbstractFieldValidator<AbstractSelect> {

	private static final long serialVersionUID = -4503856440101855575L;
	private EStructuralFeature eStructuralFeature;

	public ECPVaadinEmptyListSelectValidator(AbstractSelect listSelect, EStructuralFeature eStructuralFeature) {
		super(listSelect);
		this.eStructuralFeature = eStructuralFeature;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		int items = abstractField.getItemIds().size();
		int lowerBound = eStructuralFeature.getLowerBound();
		int upperBound = eStructuralFeature.getUpperBound() == -1 ? Integer.MAX_VALUE : eStructuralFeature
				.getUpperBound();

		if (lowerBound > items || items > upperBound) {
			throw new InvalidValueException(errorMessage);
		}
	}

}
