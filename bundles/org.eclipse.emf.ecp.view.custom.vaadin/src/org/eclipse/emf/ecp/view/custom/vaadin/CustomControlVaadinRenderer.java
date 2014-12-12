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
package org.eclipse.emf.ecp.view.custom.vaadin;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomControl;
import org.osgi.framework.Bundle;

import com.vaadin.ui.Component;

/**
 * A Vaadin Renderer for {@link VCustomControl}
 *
 * @author Dennis Melzer
 *
 */
public class CustomControlVaadinRenderer extends AbstractVaadinRenderer<VCustomControl> {

	private static AbstractVaadinRenderer<VCustomControl> loadObject(String bundleName, String clazz) {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			new ClassNotFoundException(clazz + " from " + bundleName + " could not be loaded"); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		try {
			final Class<?> loadClass = bundle.loadClass(clazz);
			if (!AbstractVaadinRenderer.class.isAssignableFrom(loadClass)) {
				return null;
			}
			return AbstractVaadinRenderer.class.cast(loadClass.newInstance());
		} catch (final ClassNotFoundException ex) {
			return null;
		} catch (final InstantiationException ex) {
			return null;
		} catch (final IllegalAccessException ex) {
			return null;
		}

	}

	@Override
	protected Component render() {
		final VCustomControl renderable = getVElement();
		final ViewModelContext viewContext = getViewModelContext();
		String bundleName = renderable.getBundleName();
		String className = renderable.getClassName();
		if (bundleName == null) {
			bundleName = ""; //$NON-NLS-1$
		}
		if (className == null) {
			className = ""; //$NON-NLS-1$
		}
		final AbstractVaadinRenderer<VCustomControl> component = loadObject(bundleName, className);
		if (component == null) {
			// TODO
			throw new IllegalStateException(String.format("The  %1$s/%2$s cannot be loaded!", //$NON-NLS-1$
				renderable.getBundleName(), renderable.getClassName()));
		}
		component.init(renderable, viewContext);

		return component.renderComponent();
	}

}
