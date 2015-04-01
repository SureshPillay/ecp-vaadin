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
package org.eclipse.emf.ecp.view.core.vaadin;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;

/**
 * Edit Support for a Row
 *
 * @author Dennis Melzer
 *
 */
public class SingleRowFieldFactory extends DefaultFieldFactory {
	@Override
	public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
		if (!(uiContext instanceof Table)) {
			return null;
		}

		final Table table = (Table) uiContext;

		if (!table.isEditable()) {
			return null;
		}

		final Object item = table.getValue();

		if (item != itemId) {
			return null;
		}
		final Field<?> field = super.createField(container, itemId, propertyId, uiContext);
		if (field instanceof AbstractTextField) {
			((AbstractTextField) field).setNullRepresentation(StringUtils.EMPTY);
		}

		return field;

	}
}
