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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

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
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

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

		when(setting.isSet()).then(new Answer<Boolean>() {
			int count = 0;

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				count++;
				return count % 3 == 0;
			}
		});

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
		Mockito.when(context.getDomainModel()).thenReturn(eObject);
	}

	protected void setMockLabelAlignment(LabelAlignment labelAlignment) {
		Mockito.when(control.getLabelAlignment()).thenReturn(labelAlignment);
	}

	private ViewModelContext context;
	private VControl control;

	protected void setup(AbstractControlRendererVaadin<VControl> renderer) {
		this.renderer = renderer;
		control = Mockito.mock(VControl.class);
		Mockito.when(control.isEnabled()).thenReturn(true);
		context = Mockito.mock(ViewModelContext.class);
	}

	@Test
	public void testGridDescriptionLabelAlignmentNone() {
		setMockLabelAlignment(LabelAlignment.NONE);
		renderControl();
		assertEquals(null, renderer.getControlComponent().getCaption());
	}

	@Test
	public void tetsRenderControlLabelAlignmentNone() {
		setMockLabelAlignment(LabelAlignment.NONE);
		final Component render = renderControl();
		assertControl(render);
	}

	@Test
	public void testRenderControlLabelAlignmentLeft() {
		setMockLabelAlignment(LabelAlignment.LEFT);
		final Component render = renderControl();

		assertControl(render);
	}

	@Test
	public void testRenderControlSettable() {
		setMockLabelAlignment(LabelAlignment.LEFT);
		final Component render = renderControlSettable();

		assertControlSettable(render);
	}

	protected void renderLabel(String text) {
		setMockLabelAlignment(LabelAlignment.LEFT);
		// Mockito.when(control.getName()).thenReturn(text);
		renderControl();
		assertEquals(text, renderer.getControlComponent().getCaption());
	}

	protected Component assertControl(Component render) {
		Component renderComponent = render;
		if (render instanceof AbstractComponent) {
			final Object o = ((AbstractComponent) render).getData();
			renderComponent = (Component) (o == null ? render : o);
		}
		if (renderComponent instanceof AbstractField) {
			final Object value = context.getDomainModel().eGet(
				control.getDomainModelReference().getEStructuralFeatureIterator().next());
			if (value instanceof XMLGregorianCalendar) {
				final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				assertEquals(sdf.format(((AbstractField<?>) renderComponent).getValue()),
					sdf.format(((XMLGregorianCalendar) value).toGregorianCalendar().getTime()));

			} else {
				assertComponentValue(renderComponent, value);
			}

		}

		assertTrue(getComponentClass().isInstance(renderComponent));
		return renderComponent;

	}

	protected void assertComponentValue(Component renderComponent, final Object value) {
		assertEquals(((AbstractField<?>) renderComponent).getValue(), value);
	}

	protected void assertControlSettable(Component render) {
		assertTrue(render instanceof HorizontalLayout);
		final HorizontalLayout layout = (HorizontalLayout) render;
		assertTrue(getComponentClass().isInstance(layout.getComponent(0)));
		assertTrue(layout.getComponent(1) instanceof Button);
		final Button button = (Button) layout.getComponent(1);
		button.click();
		assertTrue(layout.getComponent(0) instanceof Label);
		assertTrue(layout.getComponent(1) instanceof Button);

	}

	protected Component renderControlSettable() {
		mockControl();
		control.getDomainModelReference().getEStructuralFeatureIterator().next().setUnsettable(true);
		renderer.init(control, context);
		return renderer.renderComponent();
	}

	protected Component renderControl() {
		mockControl();
		renderer.init(control, context);
		return renderer.renderComponent();
	}

	protected abstract void mockControl();

	protected abstract Class<?> getComponentClass();
}
