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
package org.eclipse.emf.ecp.controls.vaadin;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;

/**
 * Nullable converter.
 *
 * @author Dennis Melzer
 *
 */
public class ECPTextFieldToTargetUpdateValueStrategy extends EMFUpdateValueStrategy {

	@Override
	public Object convert(Object value) {
		if (value == null || StringUtils.EMPTY.equals(value)) {
			return null;
		}
		return super.convert(value);
	}
}
