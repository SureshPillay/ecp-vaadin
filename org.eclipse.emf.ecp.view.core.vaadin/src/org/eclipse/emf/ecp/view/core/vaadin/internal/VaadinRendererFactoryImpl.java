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
package org.eclipse.emf.ecp.view.core.vaadin.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.osgi.framework.Bundle;

import com.vaadin.ui.Component;

public final class VaadinRendererFactoryImpl implements VaadinRendererFactory {
	private static final String RENDER_EXTENSION = "org.eclipse.emf.ecp.view.model.vaadin.renderer"; //$NON-NLS-1$
	private Map<Class<VElement>, AbstractVaadinRenderer<VElement>> rendererMapping = new LinkedHashMap<>();

	public VaadinRendererFactoryImpl() {
		loadRenderer();
	}

	private void loadRenderer() {
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(RENDER_EXTENSION);
		for (final IExtension extension : extensionPoint.getExtensions()) {

			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				try {
					@SuppressWarnings("unchecked")
					final AbstractVaadinRenderer<VElement> renderer = (AbstractVaadinRenderer<VElement>) configurationElement
							.createExecutableExtension("renderer");
					String clazz = configurationElement.getAttribute("renderable");

					final Class<VElement> renderable = loadClass(extension.getContributor().getName(), clazz);

					this.rendererMapping.put(renderable, renderer);
				} catch (final CoreException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InvalidRegistryObjectException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + bundleName);
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	@Override
	public <T extends VElement> Component render(T renderable, ViewModelContext viewContext) {
		return getVaadinComponentRenderer(renderable).renderComponent(renderable, viewContext);
	}

	@Override
	public <T extends VElement> AbstractVaadinRenderer<VElement> getVaadinComponentRenderer(T renderable) {
		return this.rendererMapping.get(renderable.getClass().getInterfaces()[0]);
	}

}
