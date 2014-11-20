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
package org.eclipse.emf.ecp.view.context.translation.internal;

import java.util.Locale;

import org.eclipse.emf.ecp.translation.service.TranslationService;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;

/**
 * The default translation service.
 *
 * @author Dennis Melzer
 *
 */
public class DefaultTranslationService implements TranslationService {

	@Override
	public String getTranslation(String keyName, Locale viewLocale) {
		return keyName;
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
