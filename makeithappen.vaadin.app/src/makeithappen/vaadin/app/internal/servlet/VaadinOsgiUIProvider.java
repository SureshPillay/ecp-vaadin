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

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

/**
 * The Vaadin UI provider
 * 
 * @author Dennis Melzer
 *
 */
public class VaadinOsgiUIProvider extends UIProvider {

	private static final long serialVersionUID = 1L;
	private static final int MAX_INACTIVE_INTERVAL = 10 * 60;

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return VaadinMainUI.class;
	}

	@Override
	public UI createInstance(UICreateEvent event) {
		final UI ui = super.createInstance(event);
		final WrappedSession session = VaadinSession.getCurrent().getSession();
		session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
		return ui;
	}

}
