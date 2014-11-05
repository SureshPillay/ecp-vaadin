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
package org.eclipse.emf.ecp.view.core.vaadin;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.style.mandatory.model.VTMandatoryPackage;
import org.eclipse.emf.ecp.view.template.style.mandatory.model.VTMandatoryStyleProperty;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

public abstract class AbstractControlRendererVaadin<T extends VControl> extends AbstractVaadinRenderer<T> {

	private static final String DEFAULT_MANADORY_MARKER = "*";

	@Override
	protected void applyCaption(T control, Component component, ViewModelContext viewContext) {
		Setting setting = control.getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(setting);

		if (!hasCaption(control, itemPropertyDescriptor)) {
			return;
		}

		String extra = StringUtils.EMPTY;

		extra = getMandatoryText(control, viewContext, setting, extra);
		component.setCaption(itemPropertyDescriptor.getDisplayName(setting.getEObject()) + extra);

		String description = itemPropertyDescriptor.getDescription(setting.getEObject());
		if (component instanceof AbstractComponent && !StringUtils.isEmpty(description)) {
			((AbstractComponent) component).setDescription(description);
		}
	}

	private String getMandatoryText(T control, ViewModelContext viewContext, Setting setting, String extra) {
		if (setting.getEStructuralFeature().getLowerBound() > 0) {
			VTMandatoryStyleProperty styleProperty = VaadinStyleTemplateUtil.getVTStyleProperty(
					VTMandatoryPackage.Literals.MANDATORY_STYLE_PROPERTY, control, viewContext);
			if (styleProperty == null) {
				extra = DEFAULT_MANADORY_MARKER; //$NON-NLS-1$
			} else {
				extra = styleProperty.getMandatoryMarker();
			}

		}
		return extra;
	}

	protected boolean hasCaption(T control, IItemPropertyDescriptor itemPropertyDescriptor) {
		return itemPropertyDescriptor != null && LabelAlignment.NONE != control.getLabelAlignment();
	}

	protected boolean hasCaption(T control) {
		Setting setting = control.getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(setting);
		return hasCaption(control, itemPropertyDescriptor);
	}

	@Override
	protected void applyValidation(T control, Component component, ViewModelContext viewContext) {
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
	protected Component render(T renderable, ViewModelContext viewContext) {
		return renderControl(renderable, viewContext);
	}

	protected abstract Component renderControl(T renderable, ViewModelContext viewContext);
}
