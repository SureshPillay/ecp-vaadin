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
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * Abstract Vaadin Renderer for {@link VControl}.
 *
 * @author Dennis Melzer
 *
 * @param <T> VControl Element
 */
public abstract class AbstractControlRendererVaadin<T extends VControl> extends AbstractVaadinRenderer<T> {

	private DataBindingContext bindingContext;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private ComposedAdapterFactory composedAdapterFactory;
	private DomainModelReferenceChangeListener domainModelReferenceChangeListener;

	@SuppressWarnings("rawtypes")
	@Override
	protected void applyCaption() {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(setting);
		final Component controlComponent = getControlComponent();

		if (!hasCaption(itemPropertyDescriptor)) {
			controlComponent.setCaption(null);
			return;
		}

		controlComponent.setCaption(itemPropertyDescriptor.getDisplayName(setting.getEObject()));

		if (controlComponent instanceof AbstractField) {
			if (setting.getEStructuralFeature().getLowerBound() > 0) {
				((AbstractField) controlComponent).setRequired(true);
			}
		}
		final String description = itemPropertyDescriptor.getDescription(setting.getEObject());
		if (controlComponent instanceof AbstractComponent && !StringUtils.isEmpty(description)) {
			((AbstractComponent) controlComponent).setDescription(description);
		}
	}

	@Override
	protected boolean wrapInFormLayout() {
		return hasCaption();
	}

	@Override
	public void dispose() {
		super.dispose();
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

		final VDomainModelReference domainModelReference = getVElement().getDomainModelReference();
		if (domainModelReference == null) {
			return;
		}
		this.domainModelReferenceChangeListener = new DomainModelReferenceChangeListener() {

			@Override
			public void notifyChange() {
				final UI ui = AbstractControlRendererVaadin.this.getControlComponent().getUI();
				if (ui == null) {
					return;
				}
				ui.access(new Runnable() {

					@Override
					public void run() {
						applyValidation();
					}
				});

			}
		};
		domainModelReference.getChangeListener().add(this.domainModelReferenceChangeListener);
	}

	/**
	 * Has the component caption.
	 *
	 * @param itemPropertyDescriptor the descriptor
	 * @return true = has a caption
	 */
	protected boolean hasCaption(IItemPropertyDescriptor itemPropertyDescriptor) {
		return itemPropertyDescriptor != null && LabelAlignment.NONE != getVElement().getLabelAlignment();
	}

	/**
	 * Has the component caption.
	 *
	 * @return true = has a caption
	 */
	protected boolean hasCaption() {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final IItemPropertyDescriptor itemPropertyDescriptor = VaadinRendererUtil.getItemPropertyDescriptor(setting);
		return hasCaption(itemPropertyDescriptor);
	}

	@Override
	protected void applyValidation() {
		// TODO: FIXME Register
		final AbstractComponent abstractComponent = (AbstractComponent) getControlComponent();
		abstractComponent.setComponentError(null);

		if (getVElement().getDiagnostic() == null) {
			return;
		}
		if (Diagnostic.ERROR == getVElement().getDiagnostic().getHighestSeverity()) {
			abstractComponent.setComponentError(new UserError(getVElement().getDiagnostic().getMessage()));
		}
	}

	/**
	 * Returns the bindingcontext.
	 *
	 * @return the bindingContext
	 */
	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	/**
	 * Returns the factory.
	 *
	 * @return the adapterFactoryItemDelegator
	 */
	public AdapterFactoryItemDelegator getAdapterFactoryItemDelegator() {
		return adapterFactoryItemDelegator;
	}

}
