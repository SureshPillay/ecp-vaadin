package org.eclipse.emf.ecp.view.model.vaadin;

import org.eclipse.emf.ecp.view.model.vaadin.validator.AbstractFieldValidator;

import com.vaadin.ui.Component;

public class ECPVaadinComponent {

	private Component component;
	private AbstractFieldValidator<?> componentValidator;

	public ECPVaadinComponent(Component component, AbstractFieldValidator<?> componentValidator) {
		this.component = component;
		this.componentValidator = componentValidator;
	}

	public ECPVaadinComponent(Component component) {
		this(component, null);
	}

	public Component getComponent() {
		return component;
	}

	public AbstractFieldValidator<?> getComponentValidator() {
		return componentValidator;
	}

}
