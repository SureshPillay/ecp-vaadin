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
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public abstract class AbstractVaadinRenderer<T extends VElement> extends AbstractRenderer<T> {

	protected VaadinRendererFactory rendererFactory;
	private ViewLocaleService viewLocaleService;
	private TranslationService translationService;

	/**
	 * Default constructor.
	 */
	public AbstractVaadinRenderer() {
		this(new VaadinRendererFactoryImpl());
	}

	/**
	 * Constructor for testing purpose.
	 *
	 * @param factory the factory to use
	 */
	protected AbstractVaadinRenderer(VaadinRendererFactory factory) {
		this.rendererFactory = factory;
	}

	public final Component renderComponent() {
		initServices();
		final Component component = render();
		if (component == null) {
			throw new RuntimeException("Error Render Component: " + getVElement().getName());
		}
		final Component controlComponent = getCaptionControlComponent(component);
		ModelChangeListener listener = new ModelChangeListener() {

			@Override
			public void notifyChange(final ModelChangeNotification notification) {
				if (notification.getRawNotification().isTouch()) {
					return;
				}
				if (notification.getNotifier() != getVElement()) {
					return;
				}
				UI ui = controlComponent.getUI();
				if (ui == null) {
					updateUI(controlComponent, notification);
					return;
				}
				controlComponent.getUI().access(new Runnable() {

					@Override
					public void run() {
						updateUI(controlComponent, notification);
					}
				});
			}

			private void updateUI(final Component controlComponent, ModelChangeNotification notification) {
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Visible()) {
					applyVisible(controlComponent);
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Enabled()
						&& !getVElement().isReadonly()) {
					applyEnable(controlComponent);
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
					applyValidation(controlComponent);
				}
			}

		};
		getViewModelContext().registerViewChangeListener(listener);
		applyVisible(controlComponent);
		applyEnable(controlComponent);
		applyValidation(controlComponent);
		applyReadonly(controlComponent);
		applyCaption(controlComponent);
		return component;
	}

	protected void applyCaption(Component controlComponent) {
		controlComponent.setCaption(StringUtils.EMPTY);
		String caption = getTranslation(getVElement());
		if (StringUtils.isEmpty(caption)) {
			return;
		}
		controlComponent.setCaption(caption);
	}

	protected void applyValidation(Component component) {

	}

	protected void applyEnable(Component component) {
		component.setEnabled(getVElement().isEnabled());
	}

	protected void applyReadonly(Component component) {
		component.setReadOnly(getVElement().isReadonly());
	}

	protected void applyVisible(Component component) {
		component.setVisible(getVElement().isVisible());
	}

	protected Component getCaptionControlComponent(Component component) {
		if (!(component instanceof AbstractComponent)) {
			return component;
		}
		AbstractComponent abstractComponent = (AbstractComponent) component;
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

	protected String getTranslation(T renderable) {
		String keyName = renderable.getName();
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

	protected abstract Component render();

}
