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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.common.spi.UniqueSetting;
import org.eclipse.emf.ecp.view.common.vaadin.test.TestRealm;
import org.eclipse.emf.ecp.view.common.vaadin.test.VaadinDatabindingClassRunner;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewComponent;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.core.vaadin.test.VaadinTestHelper;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextDisposeListener;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.context.ViewModelService;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelUtil;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
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
		final ResourceSet resourceSet = new ResourceSetImpl();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
			new ComposedAdapterFactory(new AdapterFactory[] {
				new ReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) }),
				new BasicCommandStack(), resourceSet);
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		final Resource resource = resourceSet.createResource(URI.createURI("VIRTUAL_URI_TEMP")); //$NON-NLS-1$
		resource.getContents().add(domainElement);
	}

	@Test
	public void testUninitializedTableWithoutColumns() {
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
	public void testInitializedTableWithoutColumnsAndEmptyReference()
	{
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
	public void testInitializedTableWithoutColumnsSingleReference()
	{
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
	public void testInitializedTableWithoutColumnsEmptySingleReference()
	{
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
	public void testTableWithoutColumns() {
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();

		final AbstractVaadinRenderer<VElement> tableRenderer = rendererFactory.getVaadinComponentRenderer(
			handle.getTableControl(),
			ViewModelContextFactory.INSTANCE.createViewModelContext(handle.getTableControl(), domainElement));
		final VerticalLayout verticalLayout = assertTable(tableRenderer);

		assertEquals(0,
			VTableDomainModelReference.class.cast(handle.getTableControl().getDomainModelReference())
				.getColumnDomainModelReferences().size());

		assertTrue(verticalLayout.getComponent(1) instanceof Table);
		final Table table = (Table) verticalLayout.getComponent(1);
		assertEquals(0, table.getColumnHeaders().length - 1);

	}

	@Test
	public void testTableWithoutColumnsWithoutViewServices()
	{
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();
		final AbstractVaadinRenderer<VElement> tableRenderer = rendererFactory.getVaadinComponentRenderer(
			handle.getTableControl(),
			new ViewModelContextWithoutServices(handle.getTableControl()));

		final VerticalLayout verticalLayout = assertTable(tableRenderer);

		assertEquals(0, VTableDomainModelReference.class.cast(handle.getTableControl().getDomainModelReference())
			.getColumnDomainModelReferences().size());

		assertTrue(verticalLayout.getComponent(1) instanceof Table);
		final Table table = (Table) verticalLayout.getComponent(1);
		assertEquals(0, table.getColumnHeaders().length - 1);
	}

	private VerticalLayout assertTable(final AbstractVaadinRenderer<VElement> tableRenderer) {
		if (!TableRendererVaadin.class.isAssignableFrom(tableRenderer.getClass())) {
			fail("No Table Renderer " + tableRenderer.getClass().getName());
		}
		final Component control = tableRenderer.renderComponent();
		if (control == null) {
			fail("No control was rendered");
		}
		assertTrue(control instanceof VerticalLayout);
		final VerticalLayout verticalLayout = (VerticalLayout) control;
		return verticalLayout;
	}

	@Test
	public void testTableWithTwoColumns() {
		// setup model
		final TableControlHandle handle = createTableWithTwoTableColumns();
		final Component render = renderTableLayout(handle);
		final Table table = assertTable(render, handle, 2);
		assertEquals(2, table.getColumnHeaders().length - 1);
	}

	@Test
	public void testTableWithTwoColumnsWithoutViewServices()
	{
		// setup model
		final TableControlHandle handle = createTableWithTwoTableColumns();
		final Table table = assertTableWithoutService(handle, 2);
		assertEquals(2, table.getColumnHeaders().length - 1);

	}

	@Test
	public void testTableWithTwoColumnsAdd() {
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
	public void testTableWithTwoColumnsRemove() {
		final TableControlHandle handle = createTableWithTwoTableColumns();

		final Table table = assertTableWithoutService(handle, 2);
		final EList<EClass> eSuperTypes = ((EClass) domainElement).getESuperTypes();
		assertEquals(eSuperTypes.size(), getItemCountByTable(table));
		final EClass eClass = eSuperTypes.get(0);
		eSuperTypes.remove(eClass);
		assertEquals(eSuperTypes.size(), getItemCountByTable(table));
	}

	@Test
	public void testTableAddRemoveButton() {
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
	public void testTableWithTwoColumnsClear() {
		final EList<EClass> eSuperTypes = ((EClass) domainElement).getESuperTypes();
		final TableControlHandle handle = createTableWithTwoTableColumns();
		final Table table = assertTableWithoutService(handle, 2);
		assertEquals(1, getItemCountByTable(table));
		eSuperTypes.clear();
		assertEquals(0, getItemCountByTable(table));
	}

	@Test
	public void testPanelTableWithTwoColumnsReadonly() {
		testPanelTableWithTwoColumns(true);
	}

	@Test
	public void testPanelTableWithTwoColumns() {
		final Table table = testPanelTableWithTwoColumns(false);
		final VerticalLayout layout = (VerticalLayout) table.getParent();

		final TestRealm realm = new TestRealm();
		Realm.runWithDefault(realm, new Runnable() {
			@Override
			public void run() {
				table.select(table.getItemIds().iterator().next());
			}
		});

		// single selection
		assertNotNull(table.getValue());
		assertTrue(layout.getComponent(2) instanceof ECPVaadinViewComponent);
		final ECPVaadinViewComponent detailView = (ECPVaadinViewComponent) layout.getComponent(2);
		assertTrue(detailView.getContent() instanceof VerticalLayout);
		final VerticalLayout detailViewLayout = (VerticalLayout) detailView.getContent();
		assertEquals(2, detailViewLayout.getComponentCount());

		Realm.runWithDefault(realm, new Runnable() {
			@Override
			public void run() {
				table.select(null);
			}
		});
		assertNull(table.getValue());
		assertEquals(2, layout.getComponentCount());
	}

	private Table testPanelTableWithTwoColumns(boolean readonly) {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		((EClass) domainElement).getESuperTypes().add(eClass);
		final TableControlHandle handle = createTableWithTwoTableColumns();
		handle.getTableControl().setReadonly(readonly);
		handle.getTableControl().setDetailEditing(DetailEditing.WITH_PANEL);
		handle.getTableControl().setDetailView(createDetailView());
		final AbstractVaadinRenderer<VElement> tableRenderer = rendererFactory.getVaadinComponentRenderer(
			handle.getTableControl(),
			new ViewModelContextWithoutServices(handle.getTableControl()));
		final VerticalLayout verticalLayout = assertTable(tableRenderer);
		Table table = null;
		if (readonly) {
			assertTrue(verticalLayout.getComponent(0) instanceof Table);
			table = (Table) verticalLayout.getComponent(0);
		} else {
			assertTrue(verticalLayout.getComponent(0) instanceof HorizontalLayout);
			assertTrue(verticalLayout.getComponent(1) instanceof Table);
			table = (Table) verticalLayout.getComponent(1);
		}
		if (readonly) {
			assertEquals(2, table.getColumnHeaders().length);
		} else {
			assertEquals(2, table.getColumnHeaders().length - 1);
		}

		// no initial selection
		assertNull(table.getValue());

		assertEquals(2, table.getItemIds().size());
		return table;
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

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#addContextUser(java.lang.Object)
		 */
		@Override
		public void addContextUser(Object arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getChildContext(org.eclipse.emf.ecore.EObject,
		 *      org.eclipse.emf.ecp.view.spi.model.VElement, org.eclipse.emf.ecp.view.spi.model.VView,
		 *      org.eclipse.emf.ecp.view.spi.context.ViewModelService[])
		 */
		@Override
		public ViewModelContext getChildContext(EObject arg0, VElement arg1, VView arg2, ViewModelService... arg3) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#registerDisposeListener(org.eclipse.emf.ecp.view.spi.context.ViewModelContextDisposeListener)
		 */
		@Override
		public void registerDisposeListener(ViewModelContextDisposeListener arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#removeContextUser(java.lang.Object)
		 */
		@Override
		public void removeContextUser(Object arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelContext#getControlsFor(org.eclipse.emf.ecp.common.spi.UniqueSetting)
		 */
		@Override
		public Set<VElement> getControlsFor(UniqueSetting arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
