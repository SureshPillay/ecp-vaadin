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
package org.eclipse.emf.ecp.view.model.vaadin;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;

import com.vaadin.ui.Component;

public abstract class AbstractVaadinRenderer<T extends VElement> {

	public Component renderComponent(final T renderable, final ViewModelContext viewContext) {
		final Component component = render(renderable, viewContext);
		ModelChangeListener listener = new ModelChangeListener() {

			@Override
			public void notifyChange(ModelChangeNotification notification) {
				if (notification.getRawNotification().isTouch()) {
					return;
				}
				if (notification.getNotifier() != renderable) {
					return;
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Visible()) {
					applyVisible(renderable, getControlComponent(component));
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Enabled()
						&& !renderable.isReadonly()) {
					applyEnable(renderable, getControlComponent(component));
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
					applyValidation(renderable, getControlComponent(component));
				}
			}

		};
		viewContext.registerViewChangeListener(listener);
		applyVisible(renderable, getControlComponent(component));
		applyEnable(renderable, getControlComponent(component));
		applyValidation(renderable, getControlComponent(component));
		return component;
	}

	protected void applyValidation(T renderable, Component component) {

	}

	protected void applyEnable(T renderable, Component component) {
		component.setEnabled(!renderable.isReadonly() && renderable.isEnabled());
	}

	protected void applyVisible(T renderable, Component component) {
		component.setVisible(renderable.isVisible());
	}

	protected Component getControlComponent(Component component) {
		return component;
	}

	protected abstract Component render(T renderable, final ViewModelContext viewContext);

}
