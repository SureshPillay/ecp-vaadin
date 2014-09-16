package makeithappen.vaadin.translation.example.internal;

import java.util.Locale;

import org.eclipse.emf.ecp.translation.service.TranslationService;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;

public class DefaultTranslationService implements TranslationService {

	@Override
	public String getTranslation(String keyName, Locale viewLocale) {
		return Messages.getString(keyName);
	}

	@Override
	public void instantiate(ViewModelContext context) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public int getPriority() {
		return 200;
	}

}
