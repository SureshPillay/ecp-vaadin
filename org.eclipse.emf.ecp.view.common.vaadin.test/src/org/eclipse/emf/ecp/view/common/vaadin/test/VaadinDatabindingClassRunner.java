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
package org.eclipse.emf.ecp.view.common.vaadin.test;

import org.eclipse.core.databinding.observable.Realm;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class VaadinDatabindingClassRunner extends BlockJUnit4ClassRunner {

	public VaadinDatabindingClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	public void run(final RunNotifier notifier) {
		final TestRealm realm = new TestRealm();
		Realm.runWithDefault(realm, new Runnable() {
			@Override
			public void run() {
				VaadinDatabindingClassRunner.super.run(notifier);
			}
		});
	}

	private static class TestRealm extends Realm {

		public TestRealm() {
			setDefault(this);
		}

		@Override
		public boolean isCurrent() {
			return true;
		}

		@Override
		public void exec(Runnable runnable) {
			runnable.run();
		}

	}
}
