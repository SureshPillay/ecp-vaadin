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

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.http.HttpContext;

public class VaadinResourceProvider implements HttpContext, BundleListener {

	private static final String VAADIN_RESOURCES_HEADER = "Vaadin-Resources"; //$NON-NLS-1$

	@SuppressWarnings("nls")
	private static final String[] VAADIN_BUNDLE_NAMES = new String[] { "com.vaadin.server", "com.vaadin.client",
			"com.vaadin.client-compiled", "com.vaadin.push", "com.vaadin.themes" };

	private BundleContext bundleContext;

	private Set<Bundle> resourceBundles;

	/**
	 * Registers as bundle listener and check current bundles.
	 */
	public VaadinResourceProvider() {
		this.resourceBundles = new HashSet<Bundle>();
		this.bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
		this.bundleContext.addBundleListener(this);

		for (Bundle bundle : this.bundleContext.getBundles()) {
			if (isVaadinResourceBundle(bundle)) {
				this.resourceBundles.add(bundle);
			}
		}
	}

	/**
	 * Unregisters as bundle listener and clears the resource bundle list.
	 */
	public void deactivate() {
		this.bundleContext.removeBundleListener(this);
		this.resourceBundles = null;
	}

	@Override
	public boolean handleSecurity(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse) throws IOException {
		return true;
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();
		int type = event.getType();

		if (type == BundleEvent.RESOLVED && isVaadinResourceBundle(bundle)) {
			this.resourceBundles.add(bundle);
		} else if (type == BundleEvent.UNINSTALLED && isVaadinResourceBundle(bundle)) {
			this.resourceBundles.remove(bundle);
		}
	}

	@Override
	public String getMimeType(String name) {
		URL resource = getResource(name);
		if (null == resource) {
			return null;
		}

		try {
			return resource.openConnection().getContentType();
		} catch (IOException e) {
			return null;
		}
	}

	@SuppressWarnings("nls")
	@Override
	public URL getResource(String name) {
		String uri = name.charAt(0) == '/' ? name : "/" + name;

		for (Bundle bundle : this.resourceBundles) {
			String adjustedUri = adjustUri(uri, bundle);
			URL resource = bundle.getResource(adjustedUri);

			if (resource != null) {
				return resource;
			}
		}

		return null;
	}

	/**
	 * Adjusts the URI.
	 * 
	 * @param uri The URI to adjust.
	 * @param bundle The bundle.
	 * @return The adjusted URI.
	 */
	@SuppressWarnings("nls")
	private String adjustUri(String uri, Bundle bundle) {
		if (isVaadinBundle(bundle)) {
			return uri;
		}

		String root = bundle.getHeaders().get(VAADIN_RESOURCES_HEADER);
		if (StringUtils.isEmpty(root) || ".".equals(root)) {
			return uri;
		}

		return "/" + root + uri;
	}

	/**
	 * Returns <code>true</code> if the given bundle is a vaadin resource bundle.
	 * 
	 * @param bundle The bundle.
	 * @return <code>true</code> if the given bundle is a vaadin resource bundle.
	 */
	private boolean isVaadinResourceBundle(Bundle bundle) {
		if (isVaadinBundle(bundle)) {
			return true;
		}

		return bundle.getHeaders().get(VAADIN_RESOURCES_HEADER) != null;
	}

	private boolean isVaadinBundle(Bundle bundle) {
		String symbolicName = bundle.getSymbolicName();
		for (String vaadinBundleName : VAADIN_BUNDLE_NAMES) {
			if (vaadinBundleName.equals(symbolicName)) {
				return true;
			}
		}

		return false;
	}

}
