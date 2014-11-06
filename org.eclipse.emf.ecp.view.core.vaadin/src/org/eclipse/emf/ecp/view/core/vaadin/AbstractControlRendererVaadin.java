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
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.common.edit.provider.CustomReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.DomainModelReferenceChangeListener;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.template.style.mandatory.model.VTMandatoryPackage;
import org.eclipse.emf.ecp.view.template.style.mandatory.model.VTMandatoryStyleProperty;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;

public abstract class AbstractControlRendererVaadin<T extends VControl> extends AbstractVaadinRenderer<T> {

	private static final String DEFAULT_MANADORY_MARKER = "*";
	protected DataBindingContext bindingContext;
	protected AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private ComposedAdapterFactory composedAdapterFactory;
	private DomainModelReferenceChangeListener domainModelReferenceChangeListener;

	@Override
	protected void applyCaption() {
		Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(setting);

		if (LabelAlignment.NONE == getVElement().getLabelAlignment()) {
			this.controlComponent.setCaption(null);
		}

		if (!hasCaption(itemPropertyDescriptor)) {
			return;
		}

		String extra = StringUtils.EMPTY;

		extra = getMandatoryText(setting, extra);
		this.controlComponent.setCaption(itemPropertyDescriptor.getDisplayName(setting.getEObject()) + extra);

		String description = itemPropertyDescriptor.getDescription(setting.getEObject());
		if (this.controlComponent instanceof AbstractComponent && !StringUtils.isEmpty(description)) {
			((AbstractComponent) this.controlComponent).setDescription(description);
		}
	}

	@Override
	protected void dispose() {
		if (getVElement().getDomainModelReference() != null) {
			getVElement().getDomainModelReference().getChangeListener().remove(this.domainModelReferenceChangeListener);
		}

		this.domainModelReferenceChangeListener = null;
		if (this.composedAdapterFactory != null) {
			this.composedAdapterFactory.dispose();
			this.composedAdapterFactory = null;
		}
		if (this.bindingContext != null) {
			this.bindingContext.dispose();
			this.bindingContext = null;
		}
		super.dispose();
	}

	@Override
	public void init(T vElement, ViewModelContext viewContext) {
		super.init(vElement, viewContext);
		this.bindingContext = new EMFDataBindingContext();
		this.composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
				new CustomReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		this.adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(this.composedAdapterFactory);
		this.domainModelReferenceChangeListener = new DomainModelReferenceChangeListener() {

			@Override
			public void notifyChange() {
				System.out.println(getVElement());
				applyValidation();
			}
		};
		getVElement().getDomainModelReference().getChangeListener().add(this.domainModelReferenceChangeListener);
	}

	private String getMandatoryText(Setting setting, String extra) {
		if (setting.getEStructuralFeature().getLowerBound() > 0) {
			VTMandatoryStyleProperty styleProperty = VaadinStyleTemplateUtil.getVTStyleProperty(
					VTMandatoryPackage.Literals.MANDATORY_STYLE_PROPERTY, getVElement(), getViewModelContext());
			if (styleProperty == null) {
				extra = DEFAULT_MANADORY_MARKER; //$NON-NLS-1$
			} else {
				extra = styleProperty.getMandatoryMarker();
			}

		}
		return extra;
	}

	protected boolean hasCaption(IItemPropertyDescriptor itemPropertyDescriptor) {
		return itemPropertyDescriptor != null && LabelAlignment.NONE != getVElement().getLabelAlignment();
	}

	protected boolean hasCaption() {
		Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(setting);
		return hasCaption(itemPropertyDescriptor);
	}

	@Override
	protected void applyValidation() {
		// TODO: FIXME Register
		AbstractComponent abstractComponent = (AbstractComponent) this.controlComponent;
		abstractComponent.setComponentError(null);

		if (getVElement().getDiagnostic() == null) {
			return;
		}
		if (Diagnostic.ERROR == getVElement().getDiagnostic().getHighestSeverity()) {
			abstractComponent.setComponentError(new UserError(getVElement().getDiagnostic().getMessage()));
		}
	}

}
