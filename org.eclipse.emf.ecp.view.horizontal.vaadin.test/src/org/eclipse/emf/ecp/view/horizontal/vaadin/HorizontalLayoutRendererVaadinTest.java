package org.eclipse.emf.ecp.view.horizontal.vaadin;

import org.eclipse.emf.ecp.view.common.vaadin.test.AbstractLayoutRendererVaadinTest;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalFactory;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalLayout;

import com.vaadin.ui.HorizontalLayout;

public class HorizontalLayoutRendererVaadinTest extends AbstractLayoutRendererVaadinTest {

	@Override
	protected VHorizontalLayout createLayout() {
		return VHorizontalFactory.eINSTANCE.createHorizontalLayout();
	}

	@Override
	protected Class<HorizontalLayout> getUILayouClass() {
		return HorizontalLayout.class;
	}

}
