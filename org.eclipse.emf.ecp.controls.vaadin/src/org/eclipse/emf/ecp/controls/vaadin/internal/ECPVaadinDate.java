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
package org.eclipse.emf.ecp.controls.vaadin.internal;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.spi.model.VControl;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;

public class ECPVaadinDate extends ECPVaadinAbstractField {

	@Override
	public AbstractField<?> createFieldControl(VControl control, Setting setting) {
		DateField dateField = new DateField();
		// TODO Model einstellung
		dateField.setResolution(Resolution.MINUTE);
		return dateField;
	}

}
