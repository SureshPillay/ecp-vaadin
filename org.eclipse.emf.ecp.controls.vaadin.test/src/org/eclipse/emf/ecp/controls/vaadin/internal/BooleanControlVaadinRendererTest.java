package org.eclipse.emf.ecp.controls.vaadin.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
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

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

@RunWith(VaadinDatabindingClassRunner.class)
public class BooleanControlVaadinRendererTest extends AbstractControlTest {

	@Before
	public void before() {
		// VaadinRendererFactory factory = mock(VaadinRendererFactory.class);
		setup(new BooleanControlVaadinRenderer());
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
		renderLabel("Interface");
	}

	private void assertControl(Component render) {
		assertTrue(CheckBox.class.isInstance(render));
		CheckBox checkBox = (CheckBox) render;
		assertFalse(checkBox.getValue());
	}

	@Override
	protected void mockControl() {
		EClass eObject = EcoreFactory.eINSTANCE.createEClass();
		EStructuralFeature eStructuralFeature = EcorePackage.eINSTANCE.getEClass_Interface();
		super.mockControl(eObject, eStructuralFeature);
	}

}
