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
package org.eclipse.emf.ecp.view.table.vaadin;

import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.core.vaadin.converter.SelectionConverter;
import org.eclipse.emf.ecp.view.core.vaadin.dialog.EditDialog;
import org.eclipse.emf.ecp.view.spi.model.VView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

/**
 * Render for a {@link org.eclipse.emf.ecp.view.spi.table.model.VTableControl VTableControl} with a detail editing
 * dialog.
 *
 *
 */
public class TableDetailDialogVaadinRenderer extends TableRendererVaadin {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.table.vaadin.TableRendererVaadin#createButtonBar()
	 */
	@Override
	protected HorizontalLayout createButtonBar() {
		final HorizontalLayout horizontalLayout = super.createButtonBar();
		createEditButton(horizontalLayout);
		return horizontalLayout;
	}

	private void createEditButton(HorizontalLayout horizontalLayout) {
		final EMFUpdateValueStrategy emfUpdateValueStrategy = new EMFUpdateValueStrategy();
		emfUpdateValueStrategy.setConverter(new SelectionConverter());
		final VView detailView = getVElement().getDetailView();
		final Button edit = new Button();
		edit.addStyleName("table-edit"); //$NON-NLS-1$
		edit.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				final EditDialog editDialog = new EditDialog((EObject) table.getValue(), getView());
				UI.getCurrent().addWindow(editDialog);
			}
		});
		edit.setEnabled(detailView != null);
		horizontalLayout.addComponent(edit);
		bindButtonEnable(edit);
	}
}
