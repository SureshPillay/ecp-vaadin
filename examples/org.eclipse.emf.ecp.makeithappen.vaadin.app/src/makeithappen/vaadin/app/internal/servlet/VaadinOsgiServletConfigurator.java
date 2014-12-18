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
package makeithappen.vaadin.app.internal.servlet;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletException;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.vaadin.server.Constants;

/**
 * Servlet Configuration
 *
 * @author Dennis Melzer
 *
 */
public class VaadinOsgiServletConfigurator {

	private static final String ROOT_PATH = "/ECP"; //$NON-NLS-1$
	private static final String VAADIN_RESOURCE_PATH = "/VAADIN"; //$NON-NLS-1$
	private HttpContext resourceProvider;
	private HttpService httpService;

	/**
	 * Sets value of resourceProvider.
	 *
	 * @param resourceProvider the resourceProvider to set.
	 */
	public void setResourceProvider(HttpContext resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	/**
	 * Set {@link HttpService} by ds
	 *
	 * @param httpService the service
	 */
	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	/**
	 * Binds the HTTP service.
	 *
	 */
	public void bindHttpService() {
		try {
			final VaadinOsgiServlet osgiServlet = new VaadinOsgiServlet();

			final Dictionary<String, String> dict = new Hashtable<String, String>();
			dict.put(Constants.SERVLET_PARAMETER_PRODUCTION_MODE, Boolean.TRUE.toString());

			httpService.registerServlet(ROOT_PATH, osgiServlet, dict, null);
			httpService.registerResources(VAADIN_RESOURCE_PATH, VAADIN_RESOURCE_PATH, resourceProvider);

		} catch (final ServletException e) {
			e.printStackTrace();
		} catch (final NamespaceException e) {
			e.printStackTrace();
		}
	}
}
