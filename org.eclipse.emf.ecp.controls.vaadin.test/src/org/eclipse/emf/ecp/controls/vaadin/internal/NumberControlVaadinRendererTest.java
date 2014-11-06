package org.eclipse.emf.ecp.controls.vaadin.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

@RunWith(VaadinDatabindingClassRunner.class)
public class NumberControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		setup(new NumberControlVaadinRenderer());
	}

	@Test
	public void renderControlLabelAlignmentNone() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.NONE);
		Component render = renderControl();
		assertControl(render);
	}

	@Test
	public void renderControlLabelAlignmentLeft() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		setMockLabelAlignment(LabelAlignment.LEFT);
		Component render = renderControl();

		assertControl(render);
	}

	@Test
	public void renderLabel() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		renderLabel("Lower Bound");
	}

	private void assertControl(Component render) {
		assertTrue(TextField.class.isInstance(render));
		assertNull(((AbstractTextField) render).getConverter());
	}

	@Override
	protected void mockControl() {
		EStructuralFeature eObject = EcoreFactory.eINSTANCE.createEAttribute();
		EStructuralFeature eStructuralFeature = EcorePackage.eINSTANCE.getETypedElement_LowerBound();
		super.mockControl(eObject, eStructuralFeature);
	}

}
