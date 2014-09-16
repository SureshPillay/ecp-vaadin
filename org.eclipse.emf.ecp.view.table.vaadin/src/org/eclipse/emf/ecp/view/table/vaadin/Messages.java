package org.eclipse.emf.ecp.view.table.vaadin;

import org.eclipse.osgi.util.NLS;

public class Messages {
	private static final String BUNDLE_NAME = "OSGI-INF.l10n.messages"; //$NON-NLS-1$

	public static String ok;
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}