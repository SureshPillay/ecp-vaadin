package org.eclipse.emf.ecp.view.model.internal.vaadin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewComponent;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@RunWith(VaadinDatabindingClassRunner.class)
public class VaadinViewRendererTest {

	private AbstractVaadinRenderer<VView> viewRenderer;
	private VView view;
	private ViewModelContext context;
	private VaadinRendererFactory factory;

	@Before
	public void setUp() {
		this.factory = Mockito.mock(VaadinRendererFactory.class);
		this.view = Mockito.mock(VView.class);

		this.context = Mockito.mock(ViewModelContext.class);
		this.viewRenderer = new ViewRendererVaadin(this.factory);

	}

	private AbstractOrderedLayout assertVaadinView(Component render, int childCount) {
		assertTrue(render instanceof ECPVaadinViewComponent);
		AbstractOrderedLayout layoutContent = (AbstractOrderedLayout) ((ECPVaadinViewComponent) render).getContent();
		assertEquals(childCount, layoutContent.getComponentCount());
		return layoutContent;
	}

	@Test
	public void testEmptyView() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		Mockito.when(this.view.getChildren()).thenReturn(new BasicEList<VContainedElement>());
		Component render = this.viewRenderer.renderComponent(this.view, this.context);
		assertVaadinView(render, 0);
	}

	@Test
	public void testMultipleSimpleCompositeView() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		BasicEList<VContainedElement> basicEList = new BasicEList<VContainedElement>();
		VContainedElement control1 = Mockito.mock(VContainedElement.class);
		VContainedElement control2 = Mockito.mock(VContainedElement.class);
		basicEList.add(control1);
		basicEList.add(control2);
		Mockito.when(this.view.getChildren()).thenReturn(basicEList);

		Mockito.when(this.factory.render(control1, this.context)).thenReturn(new VerticalLayout());
		Mockito.when(this.factory.render(control2, this.context)).thenReturn(new VerticalLayout());

		// Mockito.when(this.factory.getVaadinComponentRenderer(control1, this.context)).thenReturn(mockRenderer1);
		// Mockito.when(this.factory.getVaadinComponentRenderer(control2, this.context)).thenReturn(mockRenderer2);

		Component render = this.viewRenderer.renderComponent(this.view, this.context);
		AbstractOrderedLayout assertVaadinView = assertVaadinView(render, 2);
		Component component1 = assertVaadinView.getComponent(0);
		Component component2 = assertVaadinView.getComponent(1);

		assertTrue(VerticalLayout.class.isInstance(component1));
		assertTrue(VerticalLayout.class.isInstance(component2));
	}

	// @Test
	// public void testMultipleComplexGridDescriptionView() throws NoRendererFoundException,
	// NoPropertyDescriptorFoundExeption {
	// BasicEList<VContainedElement> basicEList = new BasicEList<VContainedElement>();
	// VContainedElement control1 = Mockito.mock(VContainedElement.class);
	// VContainedElement control2 = Mockito.mock(VContainedElement.class);
	// VContainedElement control3 = Mockito.mock(VContainedElement.class);
	// basicEList.add(control1);
	// basicEList.add(control2);
	// basicEList.add(control3);
	// Mockito.when(this.view.getChildren()).thenReturn(basicEList);
	//
	// AbstractSWTRenderer<VElement> mockRenderer1 = createCompositeMockRenderer(control1, 1);
	// AbstractSWTRenderer<VElement> mockRenderer2 = createCompositeMockRenderer(control2, 3);
	// AbstractSWTRenderer<VElement> mockRenderer3 = createCompositeMockRenderer(control3, 2);
	//
	// when(this.factory.getRenderer(control1, this.context)).thenReturn(mockRenderer1);
	// when(this.factory.getRenderer(control2, this.context)).thenReturn(mockRenderer2);
	// when(this.factory.getRenderer(control3, this.context)).thenReturn(mockRenderer3);
	//
	// Control render = this.viewRenderer.render(new SWTGridCell(0, 0, this.viewRenderer), this.shell);
	// assertTrue(Composite.class.isInstance(render));
	// assertEquals(6, Composite.class.cast(render).getChildren().length);
	// for (int i = 0; i < 6; i++) {
	// assertTrue(GridData.class.isInstance(Composite.class.cast(render).getChildren()[i].getLayoutData()));
	// }
	//
	// assertEquals(3,
	// GridData.class.cast(Composite.class.cast(render).getChildren()[0].getLayoutData()).horizontalSpan);
	// assertEquals(2,
	// GridData.class.cast(Composite.class.cast(render).getChildren()[5].getLayoutData()).horizontalSpan);
	// }

}
