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
import org.eclipse.emf.ecp.view.core.vaadin.AbstractControlRendererVaadin;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomControl;
import org.osgi.framework.Bundle;

import com.vaadin.ui.Component;

public class CustomControlVaadinRenderer extends AbstractControlRendererVaadin<VCustomControl> {

	private static VaadinCustomControl loadObject(String bundleName, String clazz) {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			new ClassNotFoundException(clazz + " from " + bundleName + " could not be loaded");
			return null;
		}
		try {
			final Class<?> loadClass = bundle.loadClass(clazz);
			if (!VaadinCustomControl.class.isAssignableFrom(loadClass)) {
				return null;
			}
			return VaadinCustomControl.class.cast(loadClass.newInstance());
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
		VCustomControl renderable = getVElement();
		ViewModelContext viewContext = getViewModelContext();
		String bundleName = renderable.getBundleName();
		String className = renderable.getClassName();
		if (bundleName == null) {
			bundleName = ""; //$NON-NLS-1$
		}
		if (className == null) {
			className = ""; //$NON-NLS-1$
		}
		final VaadinCustomControl component = loadObject(bundleName, className);
		if (component == null) {
			// TODO
			throw new IllegalStateException(String.format("The  %1$s/%2$s cannot be loaded!", //$NON-NLS-1$
					renderable.getBundleName(), renderable.getClassName()));
		}
		component.renderCustomControl(renderable, viewContext);
		return component;
	}

	@Override
	protected void applyCaption(Component component) {
		VaadinCustomControl vaadinCustomControl = (VaadinCustomControl) component;
		if (vaadinCustomControl.showCaption()) {
			super.applyCaption(vaadinCustomControl.getControlComponent());
		}
	}

	@Override
	protected void applyValidation(Component component) {
		VaadinCustomControl vaadinCustomControl = (VaadinCustomControl) component;
		if (vaadinCustomControl.showValidation()) {
			super.applyValidation(vaadinCustomControl.getControlComponent());
		}
		vaadinCustomControl.applyValidation(getVElement());
	}

	@Override
	protected void applyEnable(Component component) {
		VaadinCustomControl vaadinCustomControl = (VaadinCustomControl) component;
		vaadinCustomControl.applyEnable(getVElement());
	}

	@Override
	protected void applyVisible(Component component) {
		VaadinCustomControl vaadinCustomControl = (VaadinCustomControl) component;
		vaadinCustomControl.applyVisible(getVElement());
	}

}
