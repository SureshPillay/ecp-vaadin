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

import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportService;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelUtil;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends Plugin {
	// The shared instance
	private static Activator plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		if (ViewModelUtil.isDebugMode()) {
			// TODO:
			// getReportService().addConsumer(new DebugSWTReportConsumer());
			// getReportService().addConsumer(new InvalidGridDescriptionReportConsumer());
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private VTViewTemplateProvider viewTemplate;

	/**
	 * Returns the Template Provider.
	 * 
	 * @return the provider
	 */
	public VTViewTemplateProvider getVTViewTemplateProvider() {
		if (viewTemplate == null) {
			final ServiceReference<VTViewTemplateProvider> viewTemplateReference = plugin.getBundle()
				.getBundleContext().getServiceReference(VTViewTemplateProvider.class);
			if (viewTemplateReference != null) {
				viewTemplate = plugin.getBundle().getBundleContext().getService(viewTemplateReference);
			}
		}
		return viewTemplate;
	}

	/**
	 * Returns the {@link ReportService}.
	 *
	 * @return the {@link ReportService}
	 */
	public ReportService getReportService() {
		final BundleContext bundleContext = getBundle().getBundleContext();
		final ServiceReference<ReportService> serviceReference = bundleContext.getServiceReference(ReportService.class);
		return bundleContext.getService(serviceReference);
	}

}
