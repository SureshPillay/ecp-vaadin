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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewRenderer;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * Render for a {@link org.eclipse.emf.ecp.view.spi.table.model.VTableControl VTableControl} with a detail editing
 * panel.
 *
 *
 */
public class TableControlDetailPanelRenderer extends TableRendererVaadin {

	private ECPVaadinView ecpVaadinView;

	@Override
	protected Component render() {
		final Component component = super.render();
		table.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				disposeDetailView();
				if (table.getValue() == null) {
					return;
				}
				if (Realm.getDefault() == null) {
					VaadinObservables.activateRealm(UI.getCurrent());
				}
				ecpVaadinView = ECPVaadinViewRenderer.INSTANCE.render(
					(EObject) table.getValue(), getView());
				layout.addComponent(ecpVaadinView.getComponent());
			}

		});
		return component;
	}

	@Override
	public void dispose() {
		super.dispose();
		disposeDetailView();
	}

	private void disposeDetailView() {
		if (ecpVaadinView != null) {
			ecpVaadinView.dispose();
			layout.removeComponent(ecpVaadinView.getComponent());
		}
	}

}
