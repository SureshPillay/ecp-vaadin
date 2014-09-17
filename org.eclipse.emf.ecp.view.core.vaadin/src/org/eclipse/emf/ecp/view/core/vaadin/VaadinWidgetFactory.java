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

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.dialog.EditDialog;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public final class VaadinWidgetFactory {

	private VaadinWidgetFactory() {

	}

	public static Button createTableAddButton(final Setting setting, final AbstractSelect abstractSelect) {
		return createTableAddButton(setting, abstractSelect, null);
	}

	public static Button createTableAddButton(final Setting setting, final AbstractSelect abstractSelect,
			final IItemPropertyDescriptor itemPropertyDescriptor) {
		Button add = new Button();
		add.addStyleName("table-add");
		add.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				EObject addItem = createItem(setting);
				getItems(setting).add(addItem);
				abstractSelect.select(addItem);
				if (ItemCaptionMode.EXPLICIT == abstractSelect.getItemCaptionMode() && itemPropertyDescriptor != null) {
					abstractSelect.setItemCaption(addItem, itemPropertyDescriptor.getDisplayName(setting.getEObject()));
				}
			}
		});
		return add;
	}

	public static Button createTableRemoveButton(final Setting setting, final AbstractSelect abstractSelect) {
		Button remove = new Button();
		remove.addStyleName("table-remove");
		remove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				removeItems(abstractSelect, setting);
			}

		});
		return remove;
	}

	public static Button createTableEditButton(final EObject selection, final VView view) {
		Button edit = new Button();
		edit.addStyleName("table-edit");
		edit.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				EditDialog editDialog = new EditDialog(selection, view);
				UI.getCurrent().addWindow(editDialog);
			}
		});
		return edit;
	}

	public static Button createListAddButton(final Setting setting, final TextField textField) {
		Button add = new Button();
		add.addStyleName("list-add");
		add.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getItems(setting).add(textField.getValue());
				textField.setValue("");
				textField.focus();
			}
		});
		return add;
	}

	public static Button createListRemoveButton(final Setting setting, final AbstractSelect abstractSelect,
			final TextField textField) {
		Button remove = new Button();
		remove.addStyleName("list-remove");

		remove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				removeItems(abstractSelect, setting);
				abstractSelect.select(0);
				textField.focus();
			}
		});

		return remove;
	}

	private static void removeItems(final AbstractSelect abstractSelect, final Setting setting) {
		final List<Object> items = getItems(setting);
		Object selectedValue = abstractSelect.getValue();
		if (selectedValue instanceof Collection) {
			items.removeAll((Collection<?>) selectedValue);
			return;
		}
		items.remove(selectedValue);
	}

	private static EObject createItem(Setting setting) {
		final EClass clazz = ((EReference) setting.getEStructuralFeature()).getEReferenceType();
		final EObject instance = clazz.getEPackage().getEFactoryInstance().create(clazz);
		return instance;
	}

	private static List<Object> getItems(final Setting setting) {
		return (List<Object>) setting.getEObject().eGet(setting.getEStructuralFeature());
	}

}
