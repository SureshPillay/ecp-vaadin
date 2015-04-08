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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.core.vaadin.dialog.EditDialog;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * A Factory for vaadin controls.
 *
 * @author Dennis Melzer
 *
 */
public final class VaadinWidgetFactory {

	private static final String ACTION_COLUMN = "actions"; //$NON-NLS-1$
	private static final String ACTION_BUTTON = "action-button"; //$NON-NLS-1$
	private static final String ACTION_BUTTONS = "action-buttons"; //$NON-NLS-1$
	private static final String TABLE_ACTION_BUTTONS = "table-action-buttons"; //$NON-NLS-1$

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
	 * Creates a add button for a list.
	 *
	 * @param setting the setting
	 * @param textField the textfield
	 * @return the button
	 */
	public static Button createListAddButton(final Setting setting, final TextField textField, final boolean addEmpty) {
		final Button add = new Button();
		add.addStyleName("list-add"); //$NON-NLS-1$
		add.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (!addEmpty && StringUtils.isEmpty(textField.getValue())) {
					textField.focus();
					return;
				}
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
	 * Creates a remove button for overlays.
	 *
	 * @param setting the setting
	 * @param delete the selection
	 * @return the button
	 */
	public static Button createTableRemoveButtonOverlay(final Setting setting, final Object delete) {
		final Button remove = new NativeButton();
		remove.addStyleName(ACTION_BUTTON);
		remove.addStyleName("table-remove-overlay"); //$NON-NLS-1$
		remove.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				removeItems(setting, delete);
			}

		});
		return remove;
	}

	/**
	 * Creates a move-up button for overlays.
	 *
	 * @param setting the setting
	 * @param move the object to move
	 * @param index the index to which the object is moved
	 * @return the button
	 */
	public static Button createTableMoveUpButtonOverlay(final Setting setting, final Object move, final int index) {
		return createMoveButton(setting, move, index - 1, "table-move-up-overlay"); //$NON-NLS-1$
	}

	/**
	 * Creates a move-down button for overlays.
	 *
	 * @param setting the setting
	 * @param move the object to move
	 * @param index the index to which the object is moved
	 * @return the button
	 */
	public static Button createTableMoveDownButtonOverlay(final Setting setting, final Object move, final int index) {
		return createMoveButton(setting, move, index - 1, "table-move-down-overlay"); //$NON-NLS-1$
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

	public static Button createTableEditButton(HorizontalLayout horizontalLayout) {
		final Button editButton = new NativeButton();
		editButton.addStyleName(ACTION_BUTTON);
		editButton.addStyleName("table-edit-overlay");
		horizontalLayout.addComponent(editButton);
		horizontalLayout.setComponentAlignment(editButton, Alignment.MIDDLE_RIGHT);
		return editButton;
	}

	public static void createTableActionColumn(final Setting setting, final Table table, final boolean enableRemove,
		final boolean enableEdit) {
		table.addStyleName(TABLE_ACTION_BUTTONS);
		table.addGeneratedColumn(ACTION_COLUMN, new ColumnGenerator() {

			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				final Collection<?> items = table.getContainerDataSource().getItemIds();
				final HorizontalLayout buttons = new HorizontalLayout();
				buttons.setStyleName(ACTION_BUTTONS);
				final EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();

				if (enableEdit) {
					final Button editButton = createTableEditButton(buttons);
					editButton.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							table.setValue(itemId);
							if (!table.isEditable()) {
								table.setEditable(true);
							}
						}

					});

					table.addValueChangeListener(new ValueChangeListener() {

						@Override
						public void valueChange(ValueChangeEvent event) {
							if (table.isEditable()) {
								table.setEditable(false);
							}
						}
					});

				} else if (ViewProviderHelper.getView((EObject) itemId, null) != null) {
					final Button editButton = createTableEditButton(buttons);
					editButton.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							final EditDialog editDialog = new EditDialog((EObject) itemId);
							UI.getCurrent().addWindow(editDialog);
						}

					});

				}

				if (items instanceof List && eStructuralFeature.isOrdered()) {
					final int index = ((List<?>) items).indexOf(itemId);

					final Button moveUp = createTableMoveUpButtonOverlay(setting, itemId, index);
					buttons.addComponent(moveUp);
					buttons.setComponentAlignment(moveUp, Alignment.MIDDLE_RIGHT);

					final int indexDown = index + 1;
					if (items.size() > indexDown) {
						final Object itemIdDown = ((List<?>) items).get(indexDown);
						final Button moveDown = createTableMoveDownButtonOverlay(setting, itemIdDown, indexDown);
						buttons.addComponent(moveDown);
						buttons.setComponentAlignment(moveDown, Alignment.MIDDLE_RIGHT);
					}
				}

				if (enableRemove) {
					final Button remove = createTableRemoveButtonOverlay(setting, itemId);
					buttons.addComponent(remove);
					buttons.setComponentAlignment(remove, Alignment.MIDDLE_RIGHT);

				}
				return buttons;
			}
		});
		table.setColumnAlignment(ACTION_COLUMN, Align.RIGHT);
		table.setColumnWidth(ACTION_COLUMN, 0);
	}

	public static void createTableActionColumn(final Setting setting, final Table table, boolean enableRemove) {
		createTableActionColumn(setting, table, enableRemove, true);
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
