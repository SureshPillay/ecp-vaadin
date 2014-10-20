package org.eclipse.emf.ecp.view.core.vaadin.internal;

import org.eclipse.core.runtime.Plugin;
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

	public VTViewTemplateProvider getVTViewTemplateProvider() {
		if (this.viewTemplate == null) {
			final ServiceReference<VTViewTemplateProvider> viewTemplateReference = plugin.getBundle()
					.getBundleContext().getServiceReference(VTViewTemplateProvider.class);
			if (viewTemplateReference != null) {
				this.viewTemplate = plugin.getBundle().getBundleContext().getService(viewTemplateReference);
			}
		}
		return this.viewTemplate;
	}

}
