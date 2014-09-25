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
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public abstract class AbstractVaadinRenderer<T extends VElement> {

	private ViewLocaleService viewLocaleService;
	private TranslationService translationService;

	public Component renderComponent(final T renderable, final ViewModelContext viewContext) {
		initServices(viewContext);
		final Component component = render(renderable, viewContext);
		if (component == null) {
			new RuntimeException("No Renderer for: " + renderable.getName());
		}
		final Component controlComponent = getCaptionControlComponent(component);
		ModelChangeListener listener = new ModelChangeListener() {

			@Override
			public void notifyChange(final ModelChangeNotification notification) {
				if (notification.getRawNotification().isTouch()) {
					return;
				}
				if (notification.getNotifier() != renderable) {
					return;
				}
				// TODO: ok?
				UI ui = controlComponent.getUI();
				if (ui == null) {
					updateUI(renderable, controlComponent, notification);
					return;
				}
				controlComponent.getUI().access(new Runnable() {

					@Override
					public void run() {
						updateUI(renderable, controlComponent, notification);
					}
				});
			}

			private void updateUI(final T renderable, final Component controlComponent,
					ModelChangeNotification notification) {
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Visible()) {
					applyVisible(renderable, controlComponent);
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Enabled()
						&& !renderable.isReadonly()) {
					applyEnable(renderable, controlComponent);
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
					applyValidation(renderable, controlComponent);
				}
			}

		};
		viewContext.registerViewChangeListener(listener);
		applyVisible(renderable, controlComponent);
		applyEnable(renderable, controlComponent);
		applyValidation(renderable, controlComponent);
		applyReadonly(renderable, controlComponent);
		applyCaption(renderable, controlComponent);
		return component;
	}

	protected void applyCaption(T renderable, Component controlComponent) {
		String caption = getTranslation(renderable);
		if (StringUtils.isEmpty(caption)) {
			return;
		}
		controlComponent.setCaption(caption);
	}

	protected void applyValidation(T renderable, Component component) {

	}

	protected void applyEnable(T renderable, Component component) {
		component.setEnabled(renderable.isEnabled());
	}

	protected void applyReadonly(T renderable, Component component) {
		component.setReadOnly(renderable.isReadonly());
	}

	protected void applyVisible(T renderable, Component component) {
		component.setVisible(renderable.isVisible());
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

	private void initServices(ViewModelContext viewContext) {
		if (this.viewLocaleService == null) {
			this.viewLocaleService = viewContext.getService(ViewLocaleService.class);
		}

		if (this.translationService == null) {
			this.translationService = viewContext.getService(TranslationService.class);
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

	protected abstract Component render(T renderable, final ViewModelContext viewContext);

}
