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
package org.eclipse.emf.ecp.translation.service;

import java.util.Locale;

import org.eclipse.emf.ecp.view.spi.context.ViewModelService;

/**
 * A Translation Server for i18n.
 *
 * @author Dennis Melzer
 *
 */
public interface TranslationService extends ViewModelService {

	/**
	 * Gets the translation.
	 * 
	 * @param keyName the propert kes
	 * @param viewLocale the locale
	 * @return the string
	 */
	String getTranslation(String keyName, Locale viewLocale);
}
