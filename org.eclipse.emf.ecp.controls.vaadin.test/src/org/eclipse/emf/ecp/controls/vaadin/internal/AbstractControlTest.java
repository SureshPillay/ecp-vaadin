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
	private AbstractControlRendererVaadin<VControl> renderer;

	/**
	 * Gets the renderer
	 *
	 * @return the renderer
	 */
	public AbstractControlRendererVaadin<VControl> getRenderer() {
		return renderer;
	}

	private Resource createResource() {
		final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> extToFactoryMap = registry.getExtensionToFactoryMap();
		extToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl());
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE), new BasicCommandStack(), resourceSet);
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		final Resource resource = resourceSet.createResource(URI.createURI("VIRTUAL"));
		return resource;

	}

	protected void mockControl(EObject eObject, final EStructuralFeature eStructuralFeature) {
		final VDomainModelReference domainModelReference = Mockito.mock(VDomainModelReference.class);
		final Setting setting = mock(Setting.class);
		final Resource resource = createResource();
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
		final BasicEList<DomainModelReferenceChangeListener> changeListener = new BasicEList<DomainModelReferenceChangeListener>();
		when(domainModelReference.getChangeListener()).thenReturn(changeListener);
		Mockito.when(control.getDomainModelReference()).thenReturn(domainModelReference);
	}

	protected void setMockLabelAlignment(LabelAlignment labelAlignment) {
		Mockito.when(control.getLabelAlignment()).thenReturn(labelAlignment);
	}

	private ViewModelContext context;
	private VControl control;

	protected void setup(AbstractControlRendererVaadin<VControl> renderer) {
		this.renderer = renderer;
		control = Mockito.mock(VControl.class);
		context = Mockito.mock(ViewModelContext.class);
	}

	@Test
	public void testGridDescriptionLabelAlignmentNone() {
		setMockLabelAlignment(LabelAlignment.NONE);
		mockControl();
		renderer.init(control, context);
		final Component component = renderer.renderComponent();
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
		renderer.init(control, context);
		final Component render = renderer.renderComponent();
		assertEquals(text, render.getCaption());
	}

	protected Component renderControl() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		mockControl();
		renderer.init(control, context);
		final Component render = renderer.renderComponent();
		return render;
	}

	protected abstract void mockControl();
}
