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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.vaadin.ECPFVaadinViewRenderer;
import org.eclipse.emf.ecp.view.model.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EditDialog extends Window {

	private final EObject selection;
	private Adapter objectChangeAdapter;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private final VTableControl tableControl;
	private Button okButton;

	public EditDialog(final EObject selection, VTableControl tableControl) {
		this.selection = selection;
		this.tableControl = tableControl;
		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
				new ReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);

		setCaption(adapterFactoryItemDelegator.getText(selection));

		objectChangeAdapter = new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				setCaption(adapterFactoryItemDelegator.getText(selection));
			}

		};
		selection.eAdapters().add(objectChangeAdapter);
		initUi();
		setResizable(true);
		setWidth(40, Unit.PERCENTAGE);
		center();
	}

	private void initUi() {
		VaadinObservables.activateRealm(UI.getCurrent());
		ECPVaadinView ecpVaadinView = ECPFVaadinViewRenderer.INSTANCE.render(selection, tableControl.getDetailView());
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		Component component = ecpVaadinView.getComponent();
		layout.addComponent(component);
		okButton = new Button(Messages.ok, new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.TOP_RIGHT);
		setContent(layout);
	}

	@Override
	public void close() {
		if (objectChangeAdapter != null) {
			selection.eAdapters().remove(objectChangeAdapter);
		}
		if (composedAdapterFactory != null) {
			composedAdapterFactory.dispose();
		}
		super.close();
	}

}
