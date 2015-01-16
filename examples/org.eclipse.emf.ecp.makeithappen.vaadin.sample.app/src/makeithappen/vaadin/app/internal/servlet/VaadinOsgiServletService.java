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

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;

/**
 * Servlet Service.
 *
 * @author Dennis Melzer
 *
 */
public class VaadinOsgiServletService extends VaadinServletService implements SessionInitListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 *
	 * @param servlet the servlet
	 * @param deploymentConfiguration the config
	 * @throws ServiceException cannot init
	 */
	public VaadinOsgiServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration)
		throws ServiceException {
		super(servlet, deploymentConfiguration);
	}

	@Override
	protected VaadinSession createVaadinSession(VaadinRequest request) throws ServiceException {
		addSessionInitListener(this);
		return super.createVaadinSession(request);
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		event.getSession().addUIProvider(new VaadinOsgiUIProvider());

	}

	@Override
	public void destroy() {
		removeSessionInitListener(this);
		super.destroy();
	}

}