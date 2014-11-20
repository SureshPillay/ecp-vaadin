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

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecp.edit.spi.ViewLocaleService;
import org.eclipse.emf.ecp.translation.service.TranslationService;
import org.eclipse.emf.ecp.view.core.vaadin.internal.VaadinRendererFactoryImpl;
import org.eclipse.emf.ecp.view.model.common.AbstractRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * The abstract vaadin renderer for a {@link VElement}.
 *
 * @author Dennis Melzer
 *
 * @param <T> Element {@link VElement}
 */
public abstract class AbstractVaadinRenderer<T extends VElement> extends AbstractRenderer<T> {

	private VaadinRendererFactory rendererFactory;
	private Component controlComponent;
	private ViewLocaleService viewLocaleService;
	private TranslationService translationService;
	private ModelChangeListener modelChangeListener;

	/**
	 * Default constructor.
	 */
	public AbstractVaadinRenderer() {
		this(new VaadinRendererFactoryImpl());
	}

	/**
	 * Returns the {@link VaadinRendererFactory}.
	 *
	 * @return the rendererFactory
	 */
	public VaadinRendererFactory getRendererFactory() {
		return rendererFactory;
	}

	/**
	 * Returns the control Component.
	 *
	 * @return the controlComponent
	 */
	protected Component getControlComponent() {
		return controlComponent;
	}

	/**
	 * Set the component.
	 *
	 * @param controlComponent the controlComponent to set
	 */
	protected void setControlComponent(Component controlComponent) {
		this.controlComponent = controlComponent;
	}

	/**
	 * Constructor for testing purpose.
	 *
	 * @param factory the factory to use
	 */
	protected AbstractVaadinRenderer(VaadinRendererFactory factory) {
		this.rendererFactory = factory;
	}

	/**
	 * Render the vaadin component.
	 *
	 * @return the vaadin component
	 */
	public final Component renderComponent() {

		final Component component = render();
		if (component == null) {
			return null;
		}
		this.controlComponent = getCaptionControlComponent(component);

		applyVisible();
		applyEnable();
		applyValidation();
		applyReadonly();
		applyCaption();
		return component;
	}

	@Override
	public void init(T vElement, ViewModelContext viewContext) {
		super.init(vElement, viewContext);
		initServices();
		this.modelChangeListener = new ModelChangeListener() {

			@Override
			public void notifyChange(final ModelChangeNotification notification) {
				if (notification.getRawNotification().isTouch()) {
					return;
				}
				if (notification.getNotifier() != getVElement()) {
					return;
				}
				final UI ui = AbstractVaadinRenderer.this.controlComponent.getUI();
				if (ui == null) {
					updateUI(AbstractVaadinRenderer.this.controlComponent, notification);
					return;
				}
				ui.access(new Runnable() {

					@Override
					public void run() {
						updateUI(AbstractVaadinRenderer.this.controlComponent, notification);
					}
				});
			}

		};
		getViewModelContext().registerViewChangeListener(this.modelChangeListener);
	}

	private void updateUI(final Component controlComponent, ModelChangeNotification notification) {
		if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Visible()) {
			applyVisible();
		}
		if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Enabled()
			&& !getVElement().isReadonly()) {
			applyEnable();
		}
		if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
			applyValidation();
		}
	}

	@Override
	protected void dispose() {
		super.dispose();
		getViewModelContext().registerViewChangeListener(this.modelChangeListener);
	}

	/**
	 * Apply the Caption.
	 */
	protected void applyCaption() {
		// controlComponent.setCaption(StringUtils.EMPTY);
		final String caption = getTranslation();
		if (StringUtils.isEmpty(caption)) {
			return;
		}
		this.controlComponent.setCaption(caption);
	}

	/**
	 * Apply the Validation.
	 */
	protected void applyValidation() {
	}

	/**
	 * Apply the Enable.
	 */
	protected void applyEnable() {
		this.controlComponent.setEnabled(getVElement().isEnabled());
	}

	/**
	 * Apply the Readonly.
	 */
	protected void applyReadonly() {
		this.controlComponent.setReadOnly(getVElement().isReadonly());
	}

	/**
	 * Apply the Visible.
	 */
	protected void applyVisible() {
		this.controlComponent.setVisible(getVElement().isVisible());
	}

	/**
	 * Returns the control for the caption.
	 *
	 * @param component the parent component
	 * @return the caption component
	 */
	protected Component getCaptionControlComponent(Component component) {
		if (!(component instanceof AbstractComponent)) {
			return component;
		}
		final AbstractComponent abstractComponent = (AbstractComponent) component;
		if (abstractComponent.getData() instanceof Component) {
			return (Component) abstractComponent.getData();
		}
		return component;
	}

	private void initServices() {
		if (this.viewLocaleService == null) {
			this.viewLocaleService = getViewModelContext().getService(ViewLocaleService.class);
		}

		if (this.translationService == null) {
			this.translationService = getViewModelContext().getService(TranslationService.class);
		}
	}

	/**
	 * Returns the translation.
	 *
	 * @return the translation string
	 */
	protected String getTranslation() {
		final String keyName = getVElement().getName();
		if (StringUtils.isEmpty(keyName)) {
			return keyName;
		}

		if (this.translationService == null) {
			return keyName;
		}
		Locale locale = Locale.getDefault();
		if (this.viewLocaleService != null) {
			locale = this.viewLocaleService.getLocale();
		}

		return this.translationService.getTranslation(keyName, locale);
	}

	/**
	 * Render the component.
	 *
	 * @return the vaadin component
	 */
	protected abstract Component render();

}
