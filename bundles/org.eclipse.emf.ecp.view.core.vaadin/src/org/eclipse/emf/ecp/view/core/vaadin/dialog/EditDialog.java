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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.internal.Messages;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The Edit Dialog for a View.
 *
 * @author Dennis Melzer
 *
 */
public class EditDialog extends Window {

	private final EObject selection;
	private final Adapter objectChangeAdapter;
	private ComposedAdapterFactory composedAdapterFactory;
	private final AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private VView view;
	private Button okButton;
	private ECPVaadinView ecpVaadinView;

	/**
	 * Constructor.
	 *
	 * @param selection the selection
	 * @param view the view
	 */
	public EditDialog(final EObject selection, VView view) {
		this.selection = selection;
		this.view = view;
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

	/**
	 * Constructor.
	 *
	 * @param selection the selection
	 */
	public EditDialog(final EObject selection) {
		this(selection, null);
	}

	private void initUi() {
		VaadinObservables.activateRealm(UI.getCurrent());
		final VView view = getView();
		if (view == null) {
			setContent(new Label("No View found"));
			return;
		}
		ecpVaadinView = ECPVaadinViewRenderer.INSTANCE.render(selection, getView());
		setContent(getContentLayout(ecpVaadinView));
	}

	private Component getContentLayout(ECPVaadinView ecpVaadinView) {
		final Component component = ecpVaadinView.getComponent();
		return createDefaultLayout(component);
	}

	private VerticalLayout createDefaultLayout(Component component) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(component);
		createOkButton(layout);
		return layout;
	}

	private void createOkButton(AbstractOrderedLayout layout) {
		okButton = new Button(Messages.ok, new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		layout.addComponent(okButton);
		layout.setComponentAlignment(okButton, Alignment.TOP_RIGHT);
	}

	private VView getView() {
		if (view != null) {
			return view;
		}

		view = ViewProviderHelper.getView(selection, null);
		return EcoreUtil.copy(view);
	}

	@Override
	public void close() {
		if (objectChangeAdapter != null) {
			selection.eAdapters().remove(objectChangeAdapter);
		}
		if (composedAdapterFactory != null) {
			composedAdapterFactory.dispose();
		}
		composedAdapterFactory = null;
		ecpVaadinView.dispose();
		super.close();
	}

}
