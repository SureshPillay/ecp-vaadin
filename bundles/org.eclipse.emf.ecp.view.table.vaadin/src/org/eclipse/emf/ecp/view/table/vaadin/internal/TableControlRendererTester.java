/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.table.vaadin.internal;

import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;

/**
 * @author jfaltermeier
 *
 */
public class TableControlRendererTester extends AbstractTableControlRendererTester {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.internal.table.swt.AbstractTableControlRendererTester#getDetailEditing()
	 */
	@Override
	protected DetailEditing getDetailEditing() {
		return DetailEditing.NONE;
	}

}
