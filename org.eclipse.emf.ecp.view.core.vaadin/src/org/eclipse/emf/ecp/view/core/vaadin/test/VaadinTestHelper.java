package org.eclipse.emf.ecp.view.core.vaadin.test;

import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.core.vaadin.internal.VaadinRendererFactoryImpl;

public final class VaadinTestHelper {

	public static VaadinRendererFactory getVaadinRendererFactory() {
		return new VaadinRendererFactoryImpl();
	}

}
