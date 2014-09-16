package makeithappen.vaadin.translation.example.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "OSGI-INF.l10n.messages"; //$NON-NLS-1$
	private static final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
}
