package org.eclipse.emf.ecp.translation.service;

import java.util.Locale;

import org.eclipse.emf.ecp.view.spi.context.ViewModelService;

public interface TranslationService extends ViewModelService {

	String getTranslation(String keyName, Locale viewLocale);
}
