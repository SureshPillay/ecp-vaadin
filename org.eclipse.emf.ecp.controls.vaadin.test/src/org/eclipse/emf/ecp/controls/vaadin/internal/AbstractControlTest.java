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
package org.eclipse.emf.ecp.controls.vaadin.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractControlRendererVaadin;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.DomainModelReferenceChangeListener;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.vaadin.ui.Component;

public abstract class AbstractControlTest {
	protected AbstractControlRendererVaadin<VControl> renderer;

	private Resource createResource() {
		Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> extToFactoryMap = registry.getExtensionToFactoryMap();
		extToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE), new BasicCommandStack(), resourceSet);
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		Resource resource = resourceSet.createResource(URI.createURI("VIRTUAL"));
		return resource;

	}

	protected void mockControl(EObject eObject, final EStructuralFeature eStructuralFeature) {
		VDomainModelReference domainModelReference = Mockito.mock(VDomainModelReference.class);
		final Setting setting = mock(Setting.class);
		Resource resource = createResource();
		resource.getContents().add(eObject);

		when(setting.getEObject()).thenReturn(eObject);
		when(setting.getEStructuralFeature()).thenReturn(eStructuralFeature);
		when(domainModelReference.getIterator()).then(new Answer<Iterator<Setting>>() {
			@Override
			public Iterator<Setting> answer(InvocationOnMock invocation) throws Throwable {
				return Collections.singleton(setting).iterator();
			}
		});
		when(domainModelReference.getEStructuralFeatureIterator()).then(new Answer<Iterator<EStructuralFeature>>() {
			@Override
			public Iterator<EStructuralFeature> answer(InvocationOnMock invocation) throws Throwable {
				return Collections.singleton(eStructuralFeature).iterator();
			}
		});
		BasicEList<DomainModelReferenceChangeListener> changeListener = new BasicEList<DomainModelReferenceChangeListener>();
		when(domainModelReference.getChangeListener()).thenReturn(changeListener);
		Mockito.when(this.control.getDomainModelReference()).thenReturn(domainModelReference);
	}

	protected void setMockLabelAlignment(LabelAlignment labelAlignment) {
		Mockito.when(this.control.getLabelAlignment()).thenReturn(labelAlignment);
	}

	private ViewModelContext context;
	private VControl control;

	protected void setup(AbstractControlRendererVaadin<VControl> renderer) {
		this.renderer = renderer;
		this.control = Mockito.mock(VControl.class);
		this.context = Mockito.mock(ViewModelContext.class);
	}

	@Test
	public void testGridDescriptionLabelAlignmentNone() {
		setMockLabelAlignment(LabelAlignment.NONE);
		mockControl();
		this.renderer.init(this.control, this.context);
		Component component = this.renderer.renderComponent();
		// SWTGridDescription gridDescription =
		// renderer.getGridDescription(GridDescriptionFactory.INSTANCE.createEmptyGridDescription());
		assertEquals(null, component.getCaption());
		// assertEquals(1, gridDescription.getRows());
	}

	// @Test
	// public void testGridDescriptionLabelAlignmentLeft() {
	// setMockLabelAlignment(LabelAlignment.LEFT);
	// mockControl();
	// this.renderer.init(this.control, this.context);
	// Component component = this.renderer.renderComponent();
	// assertEquals("", component.getCaption());
	// }

	protected void renderLabel(String text) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		mockControl();
		this.renderer.init(this.control, this.context);
		Component render = this.renderer.renderComponent();
		assertEquals(text, render.getCaption());
	}

	protected Component renderControl() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		mockControl();
		this.renderer.init(this.control, this.context);
		Component render = this.renderer.renderComponent();
		return render;
	}

	protected abstract void mockControl();
}
