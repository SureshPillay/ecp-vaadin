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
package org.eclipse.emf.ecp.view.core.vaadin.internal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.view.core.vaadin.AbstractVaadinRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.VaadinRendererFactory;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.model.common.ECPStaticRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.reporting.AbstractReport;
import org.osgi.framework.Bundle;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

/**
 * The implementation for the {@link VaadinRendererFactory}.
 *
 * @author Dennis Melzer
 *
 */
public class VaadinRendererFactoryImpl implements VaadinRendererFactory {
	private static final String RENDER_EXTENSION = "org.eclipse.emf.ecp.view.model.vaadin.renderer"; //$NON-NLS-1$
	//	private static final String ADDITIONAL_RENDER_EXTENSION = "org.eclipse.emf.ecp.view.model.vaadin.renderer.additionalRenderers"; //$NON-NLS-1$
	private static final String TEST_DYNAMIC = "dynamicTest";//$NON-NLS-1$
	private static final String TEST_STATIC = "staticTest";//$NON-NLS-1$
	private static final String TESTER_PRIORITY = "priority";//$NON-NLS-1$
	private static final String TESTER_VELEMENT = "element"; //$NON-NLS-1$
	private static final String RENDERER_TESTER = "testClass"; //$NON-NLS-1$

	/**
	 * A description of all available renderers.
	 */
	private final Set<ECPRendererDescription> rendererDescriptors = new LinkedHashSet<ECPRendererDescription>();

	// /**
	// * A description of all additionally available renderers.
	// */
	// private final Set<ECPAdditionalRendererDescription> additionalRendererDescriptors = new
	// LinkedHashSet<ECPAdditionalRendererDescription>();

	/**
	 * Default constructor for the renderer factory.
	 */
	public VaadinRendererFactoryImpl() {
		readRenderer();
		// readAdditionalRenderer();
	}

	// /**
	// * Returns a set of descriptions of all additionally available renderers.
	// *
	// * @return a set of descriptions of all additionally available renderers.
	// */
	// protected Set<ECPAdditionalRendererDescription> getAdditionalRendererDescriptors() {
	// return this.additionalRendererDescriptors;
	// }

	/**
	 * Returns a set of descriptions of all available renderers.
	 *
	 * @return a set of descriptions of all available renderers.
	 */
	protected Set<ECPRendererDescription> getRendererDescriptors() {
		return rendererDescriptors;
	}

	private void readRenderer() {
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(RENDER_EXTENSION);
		for (final IExtension extension : extensionPoint.getExtensions()) {

			for (final IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				try {
					final Class<AbstractVaadinRenderer<VElement>> renderer = loadClass(configurationElement
						.getContributor().getName(), configurationElement.getAttribute("renderer")); //$NON-NLS-1$

					final Set<ECPRendererTester> tester = new LinkedHashSet<ECPRendererTester>();
					for (final IConfigurationElement testerExtension : configurationElement.getChildren()) {
						if (TEST_DYNAMIC.equals(testerExtension.getName())) {
							tester.add((ECPRendererTester) testerExtension.createExecutableExtension(RENDERER_TESTER));
						} else if (TEST_STATIC.equals(testerExtension.getName())) {

							final int priority = Integer.parseInt(testerExtension.getAttribute(TESTER_PRIORITY));

							final String vElement = testerExtension.getAttribute(TESTER_VELEMENT);
							final Class<? extends VElement> supportedEObject = loadClass(testerExtension
								.getContributor().getName(), vElement);

							tester.add(new ECPStaticRendererTester(priority, supportedEObject));
						}
					}

					rendererDescriptors.add(new ECPRendererDescription(renderer, tester));
				} catch (final CoreException ex) {
					ex.printStackTrace();
					// report(new ECPRendererDescriptionInitFailedReport(ex));
				} catch (final ClassNotFoundException ex) {
					ex.printStackTrace();
					// report(new ECPRendererDescriptionInitFailedReport(ex));
				} catch (final InvalidRegistryObjectException ex) {
					ex.printStackTrace();
					// report(new ECPRendererDescriptionInitFailedReport(ex));
				}
			}
		}
	}

	// private void readAdditionalRenderer() {
	// final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
	// ADDITIONAL_RENDER_EXTENSION);
	//
	// for (final IExtension extension : extensionPoint.getExtensions()) {
	//
	// for (final IConfigurationElement configurationElement : extension.getConfigurationElements()) {
	// try {
	// final Class<AbstractVaadinRenderer<VElement>> renderer = loadClass(configurationElement
	//							.getContributor().getName(), configurationElement.getAttribute("renderer")); //$NON-NLS-1$
	// final ECPAdditionalRendererTester tester = (ECPAdditionalRendererTester) configurationElement
	//							.createExecutableExtension("tester"); //$NON-NLS-1$
	// // final Set<ECPAdditionalRendererTester> tester = new LinkedHashSet<ECPAdditionalRendererTester>();
	// // final IConfigurationElement testerExtension = configurationElement.getChildren(TEST_DYNAMIC)[0];
	// // only dynamic tester allowed
	// // tester
	// // .add((ECPAdditionalRendererTester) testerExtension.createExecutableExtension(RENDERER_TESTER));
	//
	// this.additionalRendererDescriptors.add(new ECPAdditionalRendererDescription(renderer, tester));
	// } catch (final CoreException ex) {
	// report(new ECPRendererDescriptionInitFailedReport(ex));
	// } catch (final ClassNotFoundException e) {
	// report(new ECPRendererDescriptionInitFailedReport(e));
	// } catch (final InvalidRegistryObjectException e) {
	// report(new ECPRendererDescriptionInitFailedReport(e));
	// }
	// }
	// }
	// }

	private void report(AbstractReport reportEntity) {
		Activator.getDefault().getReportService().report(reportEntity);
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + bundleName);
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	@Override
	public AbstractVaadinRenderer<VElement> getVaadinComponentRenderer(VElement vElement, ViewModelContext viewContext) {

		int highestPriority = -1;
		AbstractVaadinRenderer<VElement> bestCandidate = null;
		// final ReportService reportService = Activator.getDefault().getReportService();

		for (final ECPRendererDescription description : rendererDescriptors) {

			int currentPriority = -1;

			for (final ECPRendererTester tester : description.getTester()) {
				final int testerPriority = tester.isApplicable(vElement, viewContext);
				if (testerPriority > currentPriority) {
					currentPriority = testerPriority;
				}

			}

			if (currentPriority == highestPriority && highestPriority != -1) {
				System.out.println("TODO AmbiguousRendererPriorityReport");
				// reportService.report(new AmbiguousRendererPriorityReport(currentPriority, description.getRenderer()
				// .getClass().getCanonicalName(), bestCandidate.getClass().getCanonicalName()));
			}

			if (currentPriority > highestPriority) {
				highestPriority = currentPriority;
				try {
					bestCandidate = description.getRenderer().newInstance();
				} catch (final InstantiationException ex) {
					ex.printStackTrace();
					// reportService.report(new RendererInitFailedReport(ex));
				} catch (final IllegalAccessException ex) {
					// reportService.report(new RendererInitFailedReport(ex));
					ex.printStackTrace();
				}
			}
		}

		if (bestCandidate == null) {
			// reportService.report(new NoRendererFoundReport(vElement));
			// TODO:?
			// if (ViewModelUtil.isDebugMode()) {
			// bestCandidate = new UnknownVElementSWTRenderer();
			// } else {
			// bestCandidate = new EmptyVElementSWTRenderer();
			// }
			throw new RuntimeException("No Renderer for: " + vElement.getName()); //$NON-NLS-1$
		}

		bestCandidate.init(vElement, viewContext);

		return bestCandidate;
	}

	// @Override
	// public Collection<AbstractAdditionalSWTRenderer<VElement>> getAdditionalRenderer(VElement vElement,
	// ViewModelContext viewModelContext) {
	//
	// final ReportService reportService = Activator.getDefault().getReportService();
	// final Set<AbstractAdditionalSWTRenderer<VElement>> renderers = new
	// LinkedHashSet<AbstractAdditionalSWTRenderer<VElement>>();
	//
	// for (final ECPAdditionalRendererDescription description : this.additionalRendererDescriptors) {
	// final ECPAdditionalRendererTester tester = description.getTester();
	// if (tester.isApplicable(vElement, viewModelContext)) {
	// try {
	// final AbstractAdditionalSWTRenderer<VElement> renderer = description.getRenderer().newInstance();
	// renderer.init(vElement, viewModelContext);
	// renderers.add(renderer);
	// continue;
	// } catch (final InstantiationException ex) {
	// reportService.report(new RendererInitFailedReport(ex));
	// continue;
	// } catch (final IllegalAccessException ex) {
	// reportService.report(new RendererInitFailedReport(ex));
	// continue;
	// }
	// }
	// }
	// return renderers;
	// }

	@Override
	public Component render(VElement renderable, ViewModelContext viewContext) {

		final AbstractVaadinRenderer<VElement> vaadinComponentRenderer = getVaadinComponentRenderer(renderable,
			viewContext);
		Component renderComponent = vaadinComponentRenderer.renderComponent();
		if (vaadinComponentRenderer.wrapInFormLayout()) {
			final FormLayout formLayout = new FormLayout(renderComponent);
			formLayout.addStyleName("caption-left"); //$NON-NLS-1$
			formLayout.setMargin(false);
			renderComponent = formLayout;
		}
		return renderComponent;
	}

}
