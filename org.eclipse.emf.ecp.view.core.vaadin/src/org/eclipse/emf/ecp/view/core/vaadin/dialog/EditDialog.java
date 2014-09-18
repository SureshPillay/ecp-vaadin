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
package org.eclipse.emf.ecp.view.core.vaadin.dialog;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.core.vaadin.ECPFVaadinViewRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.core.vaadin.internal.Messages;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
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
	private VView view;
	private Button okButton;

	public EditDialog(final EObject selection, VView view) {
		this.selection = selection;
		this.view = view;
		this.composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
				new ReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		this.adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(this.composedAdapterFactory);

		setCaption(this.adapterFactoryItemDelegator.getText(selection));

		this.objectChangeAdapter = new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				setCaption(EditDialog.this.adapterFactoryItemDelegator.getText(selection));
			}

		};
		selection.eAdapters().add(this.objectChangeAdapter);
		initUi();
		setResizable(true);
		setWidth(40, Unit.PERCENTAGE);
		center();
	}

	public EditDialog(final EObject selection) {
		this(selection, null);
	}

	private void initUi() {
		VaadinObservables.activateRealm(UI.getCurrent());
		ECPVaadinView ecpVaadinView = ECPFVaadinViewRenderer.INSTANCE.render(this.selection, getView());
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		Component component = ecpVaadinView.getComponent();
		layout.addComponent(component);
		this.okButton = new Button(Messages.ok, new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		layout.addComponent(this.okButton);
		layout.setComponentAlignment(this.okButton, Alignment.TOP_RIGHT);
		setContent(layout);
	}

	private VView getView() {
		if (this.view != null) {
			return this.view;
		}

		this.view = ViewProviderHelper.getView(this.selection, null);
		return this.view;
	}

	@Override
	public void close() {
		if (this.objectChangeAdapter != null) {
			this.selection.eAdapters().remove(this.objectChangeAdapter);
		}
		if (this.composedAdapterFactory != null) {
			this.composedAdapterFactory.dispose();
		}
		this.composedAdapterFactory = null;
		super.close();
	}

}
