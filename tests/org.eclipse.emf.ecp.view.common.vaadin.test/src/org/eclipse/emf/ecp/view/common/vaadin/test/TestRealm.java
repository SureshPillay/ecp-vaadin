package org.eclipse.emf.ecp.view.common.vaadin.test;

import org.eclipse.core.databinding.observable.Realm;

public class TestRealm extends Realm {

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