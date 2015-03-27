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

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.dialog.EditDialog;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.BaseTheme;

/**
 * A Factory for vaadin controls.
 *
 * @author Dennis Melzer
 *
 */
public final class VaadinWidgetFactory {

	private static final String ACTION_BUTTON = "action-button"; //$NON-NLS-1$

	private VaadinWidgetFactory() {

	}

	/**
	 * Creates a add button for table.
	 *
	 * @param setting the setting
	 * @param table the table
	 * @return the button
	 */
	public static Button createTableAddButton(final Setting setting, final Table table) {
		final Button add = new Button();
		add.addStyleName("table-add"); //$NON-NLS-1$
		add.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				final EObject addItem = createItem(setting);
				addItem(setting, addItem);
				if (table.isSelectable()) {
					table.select(addItem);
				}
			}

		});
		return add;
	}

	public static EditingDomain getEditingDomain(Setting setting) {
		return AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject());
	}

	private static void addItem(final Setting setting, final Object addItem) {
		final EditingDomain editingDomain = getEditingDomain(setting);
		editingDomain.getCommandStack().execute(
			AddCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), addItem));
	}

	/**
	 * Creates a remove button for a select element.
	 *
	 * @param setting the setting
	 * @param abstractSelect the table
	 * @return the button
	 */
	public static Button createTableRemoveButton(final Setting setting, final AbstractSelect abstractSelect) {
		final Button remove = new Button();
		remove.addStyleName("table-remove"); //$NON-NLS-1$
		remove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				removeItems(setting, abstractSelect.getValue());
			}

		});
		return remove;
	}

	/**
	 * Creates a add button for a list.
	 *
	 * @param setting the setting
	 * @param textField the textfield
	 * @return the button
	 */
	public static Button createListAddButton(final Setting setting, final TextField textField) {
		final Button add = new Button();
		add.addStyleName("list-add"); //$NON-NLS-1$
		add.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					addItem(setting, textField.getConvertedValue());
				} catch (final Converter.ConversionException e) {
					return;
				}
				textField.setValue(""); //$NON-NLS-1$
				textField.focus();
			}
		});
		return add;
	}

	/**
	 * Creates a edit link.
	 *
	 * @param selection the selection
	 * @param caption the caption
	 * @return the button
	 */
	public static Button createEditLink(final EObject selection, String caption) {
		final Button edit = new Button(caption);
		edit.addStyleName(BaseTheme.BUTTON_LINK);
		edit.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				final EditDialog editDialog = new EditDialog(selection);
				UI.getCurrent().addWindow(editDialog);
			}
		});
		return edit;
	}

	/**
	 * Creates a remove button in icon-only look.
	 *
	 * @param setting the setting
	 * @param delete the selection
	 * @return the button
	 */
	public static Button createTableRemoveButtonIconOnly(final Setting setting, final Object delete) {
		final Button remove = new NativeButton();
		remove.addStyleName(ACTION_BUTTON);
		remove.addStyleName("table-remove"); //$NON-NLS-1$
		remove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				removeItems(setting, delete);
			}

		});
		return remove;
	}

	/**
	 * Creates a move-up button in icon-only look.
	 *
	 * @param setting the setting
	 * @param move the object to move
	 * @param index the index to which the object is moved
	 * @return the button
	 */
	public static Button createTableMoveUpButtonIconOnly(final Setting setting, final Object move, final int index) {
		return createMoveButton(setting, move, index, "table-move-up"); //$NON-NLS-1$
	}

	/**
	 * Creates a move-down button in icon-only look.
	 *
	 * @param setting the setting
	 * @param move the object to move
	 * @param index the index to which the object is moved
	 * @return the button
	 */
	public static Button createTableMoveDownButtonIconOnly(final Setting setting, final Object move, final int index) {
		return createMoveButton(setting, move, index, "table-move-down"); //$NON-NLS-1$
	}

	private static Button createMoveButton(final Setting setting, final Object move, final int index, String styleName) {
		final Button moveButton = new NativeButton();
		final Command command = createMoveCommand(setting, move, index);
		if (command.canExecute()) {
			moveButton.addStyleName(ACTION_BUTTON);
			moveButton.addStyleName(styleName);
			moveButton.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					getEditingDomain(setting).getCommandStack().execute(command);
				}

			});
		} else {
			moveButton.setVisible(false);
		}
		return moveButton;
	}

	private static void removeItems(Setting setting, Object deleteObject) {
		if (deleteObject == null) {
			return;
		}

		final EditingDomain editingDomain = getEditingDomain(setting);
		editingDomain.getCommandStack().execute(
			RemoveCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), deleteObject));
	}

	private static Command createMoveCommand(Setting setting, Object moveObject, int index) {

		final EditingDomain editingDomain = getEditingDomain(setting);
		return MoveCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(), moveObject,
			index);
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
