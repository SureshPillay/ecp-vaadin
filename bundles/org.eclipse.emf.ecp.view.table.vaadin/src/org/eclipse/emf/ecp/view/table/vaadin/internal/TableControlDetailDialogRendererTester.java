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
package org.eclipse.emf.ecp.view.table.vaadin.internal;

import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;

/**
 * @author Dennis Melzer
 *
 */
public class TableControlDetailDialogRendererTester extends AbstractTableControlRendererTester {

	@Override
	protected DetailEditing getDetailEditing() {
		return DetailEditing.WITH_DIALOG;
	}

}
