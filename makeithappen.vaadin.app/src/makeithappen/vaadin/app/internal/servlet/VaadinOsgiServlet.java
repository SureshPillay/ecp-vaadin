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

import makeithappen.vaadin.app.internal.VaadinMainUI;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

@VaadinServletConfiguration(ui = VaadinMainUI.class, productionMode = false)
/**
 * Vaadin Servlet for OSGi integration
 * @author Dennis Melzer
 *
 */
public class VaadinOsgiServlet extends VaadinServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
		throws ServiceException {
		final VaadinOsgiServletService osgiServletService = new VaadinOsgiServletService(this, deploymentConfiguration);
		osgiServletService.init();

		return osgiServletService;
	}

}
