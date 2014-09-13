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
package makeithappen.vaadin.custom.control;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.custom.vaadin.VaadinCustomControl;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomControl;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;

public class UploadCustomComponent extends HorizontalLayout implements VaadinCustomControl {

	private Label label;

	@Override
	public void renderCustomControl(VCustomControl customControl, ViewModelContext viewModelContext) {
		label = new Label();
		addComponent(label);
		Upload upload = new Upload();
		addComponent(upload);

		EMFDataBindingContext bindingContext = new EMFDataBindingContext();
		Setting setting = customControl.getDomainModelReference().getIterator().next();
		VaadinObservables.activateRealm(UI.getCurrent());
		IObservableValue targetValue = VaadinObservables.observeCaption(label);
		IObservableValue modelValue = EMFProperties.value(setting.getEStructuralFeature())
				.observe(setting.getEObject());
		bindingContext.bindValue(targetValue, modelValue);
	}

	@Override
	public boolean showCaption() {
		return false;
	}

	@Override
	public Component getControlComponent() {
		return label;
	}

	@Override
	public boolean showValidation() {
		return false;
	}

	@Override
	public void applyValidation(VCustomControl control) {
	}

	@Override
	public void applyEnable(VCustomControl renderable) {
	}

	@Override
	public void applyVisible(VCustomControl renderable) {
	}
}
