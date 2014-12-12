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
package org.eclipse.emf.ecp.view.table.vaadin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.common.UniqueSetting;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.core.vaadin.test.VaadinTestHelper;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelUtil;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@RunWith(VaadinDatabindingClassRunner.class)
public class TableRendererVaadinTest {
	private final VaadinRendererFactory rendererFactory = VaadinTestHelper.getVaadinRendererFactory();
	private EObject domainElement;

	@Before
	public void init() {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.getESuperTypes().add(EcorePackage.eINSTANCE.getEClass());
		domainElement = eClass;
	}

	@Test
	public void testUninitializedTableWithoutColumns() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final TableControlHandle handle = createUninitializedTableWithoutColumns();
		final Component render = renderTableLayout(handle);

		assertNull(render);
	}

	private Component renderTableLayout(final TableControlHandle handle) {
		final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE.createViewModelContext(
			handle.getTableControl(), domainElement);
		return rendererFactory.render(handle.getTableControl(), viewContext);
	}

	private Component renderTableLayoutWithoutServices(final TableControlHandle handle) {
		return rendererFactory.render(handle.getTableControl(),
			new ViewModelContextWithoutServices(handle.getTableControl()));
	}

	private int getItemCountByTable(Table table) {
		return table.getContainerDataSource().getItemIds().size();
	}

	@Test
	public void testInitializedTableWithoutColumnsAndEmptyReference() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final EClass createEClass = EcoreFactory.eINSTANCE.createEClass();
		createEClass.eUnset(EcorePackage.eINSTANCE.getEClass_ESuperTypes());
		domainElement = createEClass;
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();
		final Component component = renderTableLayout(handle);

		try {
			assertNotNull(component);
		} catch (final NullPointerException e) {
			fail("Fails without a reference in domain object");
		}

	}

	@Ignore
	@Test
	public void testInitializedTableWithoutColumnsSingleReference() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final VView view = VViewFactory.eINSTANCE.createView();
		view.setRootEClass(VViewPackage.eINSTANCE.getView());
		domainElement = view;
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();
		final VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(VViewPackage.eINSTANCE.getView_RootEClass());
		handle.getTableControl().setDomainModelReference(domainModelReference);

		renderTableLayout(handle);

	}

	@Ignore
	@Test
	public void testInitializedTableWithoutColumnsEmptySingleReference() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final VView view = VViewFactory.eINSTANCE.createView();
		domainElement = view;
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();
		final VFeaturePathDomainModelReference domainModelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(VViewPackage.eINSTANCE.getView_RootEClass());
		handle.getTableControl().setDomainModelReference(domainModelReference);

		renderTableLayout(handle);

	}

	private Table assertTableWithoutService(TableControlHandle handle, int attributLength) {
		final Component render = renderTableLayoutWithoutServices(handle);
		return assertTable(render, handle, attributLength);
	}

	private Table assertTableWithService(TableControlHandle handle, int attributLength) {
		final Component render = renderTableLayout(handle);
		return assertTable(render, handle, attributLength);
	}

	private Table assertTable(Component render, TableControlHandle handle, int attributLength) {
		assertTrue(render instanceof VerticalLayout);

		assertEquals(attributLength,
			VTableDomainModelReference.class.cast(handle.getTableControl().getDomainModelReference())
				.getColumnDomainModelReferences().size());

		final Component control = getTable((VerticalLayout) render);
		assertTrue(control instanceof Table);
		return (Table) control;

	}

	@Test
	public void testTableWithoutColumns() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		// setup model
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();
		final Table table = assertTableWithService(handle, domainElement.eClass().getEAttributes().size());
		assertEquals(2, table.getColumnHeaders().length);
	}

	@Test
	public void testTableWithoutColumnsWithoutViewServices() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();
		final Table table = assertTableWithoutService(handle, 0);
		assertEquals(0, table.getColumnHeaders().length);
	}

	@Test
	public void testTableWithTwoColumns() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		// setup model
		final TableControlHandle handle = createTableWithTwoTableColumns();
		final Component render = renderTableLayout(handle);
		final Table table = assertTable(render, handle, 2);
		assertEquals(2, table.getColumnHeaders().length);
	}

	@Test
	public void testTableWithTwoColumnsWithoutViewServices() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final TableControlHandle handle = createTableWithTwoTableColumns();
		final Table table = assertTableWithoutService(handle, 2);
		assertEquals(2, table.getColumnHeaders().length);

	}

	@Test
	public void testTableWithTwoColumnsAdd() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final TableControlHandle handle = createTableWithTwoTableColumns();

		final Table table = assertTableWithoutService(handle, 2);

		int itemsCount = getItemCountByTable(table);
		final EList<EClass> eSuperTypes = ((EClass) domainElement).getESuperTypes();
		assertEquals(eSuperTypes.size(), itemsCount);
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eSuperTypes.add(eClass);
		itemsCount = getItemCountByTable(table);
		assertEquals(eSuperTypes.size(), itemsCount);
	}

	@Test
	public void testTableWithTwoColumnsRemove() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final TableControlHandle handle = createTableWithTwoTableColumns();

		final Table table = assertTableWithoutService(handle, 2);
		final EList<EClass> eSuperTypes = ((EClass) domainElement).getESuperTypes();
		assertEquals(eSuperTypes.size(), getItemCountByTable(table));
		final EClass eClass = eSuperTypes.get(0);
		eSuperTypes.remove(eClass);
		assertEquals(eSuperTypes.size(), getItemCountByTable(table));
	}

	@Test
	public void testTableAddRemoveButton() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final TableControlHandle handle = createTableWithTwoTableColumns();

		final VerticalLayout layout = (VerticalLayout) renderTableLayoutWithoutServices(handle);
		final HorizontalLayout horizontalLayout = (HorizontalLayout) layout.getComponent(0);
		final Button addButton = (Button) horizontalLayout.getComponent(0);
		final Button removeButton = (Button) horizontalLayout.getComponent(1);
		final Table table = getTable(layout);
		final EList<EClass> eSuperTypes = ((EClass) domainElement).getESuperTypes();
		assertEquals(eSuperTypes.size(), getItemCountByTable(table));
		addButton.click();
		assertEquals(2, getItemCountByTable(table));
		final EClass eClass = eSuperTypes.get(1);
		table.select(eClass);
		removeButton.click();
		assertEquals(1, getItemCountByTable(table));
		addButton.click();
		assertEquals(2, getItemCountByTable(table));
	}

	@Test
	public void testTableWithTwoColumnsClear() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final EList<EClass> eSuperTypes = ((EClass) domainElement).getESuperTypes();
		final TableControlHandle handle = createTableWithTwoTableColumns();
		final Table table = assertTableWithoutService(handle, 2);
		assertEquals(1, getItemCountByTable(table));
		eSuperTypes.clear();
		assertEquals(0, getItemCountByTable(table));
	}

	@Test
	public void testPanelTableWithTwoColumns() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		((EClass) domainElement).getESuperTypes().add(eClass);
		final TableControlHandle handle = createTableWithTwoTableColumns();
		handle.getTableControl().setDetailEditing(DetailEditing.WITH_PANEL);
		handle.getTableControl().setDetailView(createDetailView());
		try {
			assertTableWithoutService(handle, 2);
			fail();
		} catch (final RuntimeException e) {
		}
		// final Composite controlComposite = (Composite) ((Composite) control).getChildren()[1];
		// final Composite tableComposite = (Composite) controlComposite.getChildren()[0];
		// final Table table = (Table) tableComposite.getChildren()[0];
		// final ScrolledComposite scrolledComposite = (ScrolledComposite) controlComposite.getChildren()[1];
		// final Composite parentForECPView = (Composite) scrolledComposite.getChildren()[0];
		// assertEquals(2, table.getItemCount());
		// final TableViewer tableViewer = getTableViewerFromRenderer(tableRenderer);
		//
		// // no initial selection
		// assertEquals(0, parentForECPView.getChildren().length);
		//
		// // single selection
		// tableViewer.setSelection(new StructuredSelection(table.getItem(0).getData()));
		// assertEquals(1, parentForECPView.getChildren().length);
		// final Composite viewComposite = (Composite) parentForECPView.getChildren()[0];
		// assertEquals(6, viewComposite.getChildren().length);
		//
		// // multi selection
		// tableViewer.setSelection(new StructuredSelection(new Object[] { table.getItem(0).getData(),
		// table.getItem(1).getData() }));
		// assertEquals(0, parentForECPView.getChildren().length);
		//
		// // select again
		// tableViewer.setSelection(new StructuredSelection(table.getItem(0).getData()));
		// assertEquals(1, parentForECPView.getChildren().length);
		//
		// // no selection
		// tableViewer.setSelection(new StructuredSelection());
		// assertEquals(0, parentForECPView.getChildren().length);
	}

	private VView createDetailView() {
		final VView detailView = VViewFactory.eINSTANCE.createView();
		final VControl name = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference nameRef = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		nameRef.setDomainModelEFeature(EcorePackage.eINSTANCE.getENamedElement_Name());
		name.setDomainModelReference(nameRef);
		detailView.getChildren().add(name);
		final VControl abstr = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference abstractRef = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		abstractRef.setDomainModelEFeature(EcorePackage.eINSTANCE.getEClass_Abstract());
		abstr.setDomainModelReference(abstractRef);
		detailView.getChildren().add(abstr);
		return detailView;

	}

	private Table getTable(VerticalLayout render) {
		return (Table) render.getComponent(1);
	}

	private TableControlHandle createTableWithTwoTableColumns() {
		final TableControlHandle tableControlHandle = createInitializedTableWithoutTableColumns();
		final VDomainModelReference tableColumn1 = createTableColumn(EcorePackage.eINSTANCE.getEClass_Abstract());
		// TODO: Checken ob 2 gleiche Columns gehen
		tableControlHandle.addFirstTableColumn(tableColumn1);
		final VDomainModelReference tableColumn2 = createTableColumn(EcorePackage.eINSTANCE.getEClass_Interface());
		tableControlHandle.addSecondTableColumn(tableColumn2);
		return tableControlHandle;
	}

	public VDomainModelReference createTableColumn(EStructuralFeature feature) {
		final VFeaturePathDomainModelReference reference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		reference.setDomainModelEFeature(feature);
		return reference;
	}

	public TableControlHandle createInitializedTableWithoutTableColumns() {
		final TableControlHandle tableControlHandle = createUninitializedTableWithoutColumns();
		final VFeaturePathDomainModelReference domainModelReference = VTableFactory.eINSTANCE
			.createTableDomainModelReference();
		domainModelReference.setDomainModelEFeature(EcorePackage.eINSTANCE.getEClass_ESuperTypes());
		tableControlHandle.getTableControl().setDomainModelReference(domainModelReference);

		return tableControlHandle;
	}

	public TableControlHandle createUninitializedTableWithoutColumns() {
		final VTableControl tableControl = createTableControl();
		return new TableControlHandle(tableControl);
	}

	private VTableControl createTableControl() {
		final VTableControl tc = VTableFactory.eINSTANCE.createTableControl();
		tc.setDomainModelReference(VTableFactory.eINSTANCE.createTableDomainModelReference());
		return tc;
	}

	/**
	 * Stub implementation without getting services from ex. point.
	 *
	 * @author jfaltermeier
	 *
	 */
	private class ViewModelContextWithoutServices implements ViewModelContext {

		private final VElement view;

		public ViewModelContextWithoutServices(VElement view) {
			this.view = view;
			ViewModelUtil.resolveDomainReferences(getViewModel(), getDomainModel());
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getViewModel()
		 */
		@Override
		public VElement getViewModel() {
			return view;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getDomainModel()
		 */
		@Override
		public EObject getDomainModel() {
			return domainElement;
		}

		/**
		 *
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#registerViewChangeListener(org.eclipse.emf.ecp.view.spi.model.ModelChangeListener)
		 */
		@Override
		public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
			// not needed
		}

		/**
		 *
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#unregisterViewChangeListener(org.eclipse.emf.ecp.view.spi.model.ModelChangeListener)
		 */
		@Override
		public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
			// not needed
		}

		/**
		 *
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#registerDomainChangeListener(org.eclipse.emf.ecp.view.spi.model.ModelChangeListener)
		 */
		@Override
		public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
			// not needed
		}

		/**
		 *
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#unregisterDomainChangeListener(org.eclipse.emf.ecp.view.spi.model.ModelChangeListener)
		 */
		@Override
		public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
			// not needed
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#dispose()
		 */
		@Override
		public void dispose() {
			// not needed
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#hasService(java.lang.Class)
		 */
		@Override
		public <T> boolean hasService(Class<T> serviceType) {
			return false;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getService(java.lang.Class)
		 */
		@Override
		public <T> T getService(Class<T> serviceType) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getControlsFor(org.eclipse.emf.ecore.EStructuralFeature.Setting)
		 */
		@Override
		public Set<VControl> getControlsFor(Setting setting) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getControlsFor(org.eclipse.emf.ecp.common.UniqueSetting)
		 */
		@Override
		public Set<VControl> getControlsFor(UniqueSetting setting) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getContextValue(java.lang.String)
		 */
		@Override
		public Object getContextValue(String key) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#putContextValue(java.lang.String,
		 *      java.lang.Object)
		 */
		@Override
		public void putContextValue(String key, Object value) {

		}
	}

}
