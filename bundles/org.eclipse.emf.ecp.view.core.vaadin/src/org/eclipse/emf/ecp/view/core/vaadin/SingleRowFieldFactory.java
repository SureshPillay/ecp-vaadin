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
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractComponent;
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
			final AbstractTextField textField = (AbstractTextField) field;
			textField.setNullRepresentation(StringUtils.EMPTY);
			textField.addFocusListener(new FocusListener() {

				@Override
				public void focus(FocusEvent event) {
					((AbstractTextField) event.getComponent()).selectAll();
				}
			});

			final Object firstColumnId = table.getVisibleColumns()[0];
			if (propertyId != null && propertyId.equals(firstColumnId)) {
				textField.focus();
			}

		}

		if (field instanceof AbstractComponent) {
			((AbstractComponent) field).setImmediate(true);
			((AbstractComponent) field).addShortcutListener(new ShortcutListener("Enter", KeyCode.ENTER, null) {

				@Override
				public void handleAction(Object sender, Object target) {
					table.setEditable(false);
					table.setValue(null);
				}
			});

		}

		return field;

	}
}
