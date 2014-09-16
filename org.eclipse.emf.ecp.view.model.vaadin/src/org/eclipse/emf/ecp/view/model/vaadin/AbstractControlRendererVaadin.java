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

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererUtil;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

public abstract class AbstractControlRendererVaadin<T extends VControl> extends AbstractVaadinRenderer<T> {

	protected void setCaption(T control, Component component) {
		if (component == null || LabelAlignment.NONE == control.getLabelAlignment()) {
			return;
		}

		Setting setting = control.getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(setting);

		if (itemPropertyDescriptor == null) {
			return;
		}
		String extra = "";

		if (setting.getEStructuralFeature().getLowerBound() > 0) {
			extra = "*"; //$NON-NLS-1$
		}
		component.setCaption(itemPropertyDescriptor.getDisplayName(setting.getEObject()) + extra);
		String description = itemPropertyDescriptor.getDescription(setting.getEObject());
		if (component instanceof AbstractComponent && !StringUtils.isEmpty(description)) {
			((AbstractComponent) component).setDescription(description);
		}
	}
	
	@Override
	protected void applyCaption(T renderable, Component controlComponent) {
	}

	@Override
	protected Component getControlComponent(Component component) {
		if (component instanceof ComponentContainer) {
			return getControlComponent(((ComponentContainer) component).iterator().next());
		}
		return component;
	}

	@Override
	protected void applyValidation(T control, Component component) {
		AbstractComponent abstractComponent = (AbstractComponent) component;
		abstractComponent.setComponentError(null);

		if (control.getDiagnostic() == null) {
			return;
		}
		if (Diagnostic.ERROR == control.getDiagnostic().getHighestSeverity()) {
			abstractComponent.setComponentError(new UserError(control.getDiagnostic().getMessage()));
		}
	}

	@Override
	protected Component render(T renderable, final ViewModelContext viewContext) {
		Component component = renderControl(renderable, viewContext);
		Component controlComponent = getControlComponent(component);
		setCaption(renderable, controlComponent);
		return component;
	}

	protected abstract Component renderControl(T renderable, ViewModelContext viewContext);
}
