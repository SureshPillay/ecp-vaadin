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

import org.eclipse.emf.ecp.view.spi.model.VContainedContainer;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;

public class HierarchyViewModelHandle {

	private final VElement root;
	private VElement firstChild;
	private VElement secondChild;

	private VElement firstFirstChild;
	private VElement firstSecondChild;
	private VElement secondFirstChild;
	private VElement secondSecondChild;

	/**
	 * Construct.
	 *
	 * @param root the Element
	 */
	public HierarchyViewModelHandle(VElement root) {
		this.root = root;
	}

	/**
	 * The root
	 *
	 * @return the horizontal the horizontal
	 */
	public VElement getRoot() {
		return root;
	}

	/**
	 *
	 *
	 * @param renderable the renderable
	 */
	public void addFirstChildToRoot(VElement renderable) {
		firstChild = renderable;
		final VContainedContainer collection = (VContainedContainer) root;
		collection.getChildren().add((VContainedElement) renderable);

	}

	/**
	 * @param renderable
	 */
	public void addSecondChildToRoot(VElement renderable) {
		setSecondChild(renderable);
		final VContainedContainer collection = (VContainedContainer) root;
		collection.getChildren().add((VContainedElement) renderable);

	}

	/**
	 * @return the first child
	 */
	public VElement getFirstChild() {
		return firstChild;
	}

	/**
	 * @return the secondChild
	 */
	public VElement getSecondChild() {
		return secondChild;
	}

	/**
	 * @param secondChild the secondChild to set
	 */
	public void setSecondChild(VElement secondChild) {
		this.secondChild = secondChild;
	}

	/**
	 *
	 */
	public void addFirstChildToFirstChild(VContainedElement composite) {
		final VContainedContainer collection = (VContainedContainer) getFirstChild();
		collection.getChildren().add(composite);
		setFirstFirstChild(composite);
	}

	/**
	 * @return the firstFirstChild
	 */
	public VElement getFirstFirstChild() {
		return firstFirstChild;
	}

	/**
	 * @param firstFirstChild the firstFirstChild to set
	 */
	public void setFirstFirstChild(VElement firstFirstChild) {
		this.firstFirstChild = firstFirstChild;
	}

	/**
	 * @return the firstSecondChild
	 */
	public VElement getFirstSecondChild() {
		return firstSecondChild;
	}

	/**
	 * @param firstSecondChild the firstSecondChild to set
	 */
	public void setFirstSecondChild(VElement firstSecondChild) {
		this.firstSecondChild = firstSecondChild;
	}

	/**
	 * @return the secondFirstChild
	 */
	public VElement getSecondFirstChild() {
		return secondFirstChild;
	}

	/**
	 * @param secondFirstChild the secondFirstChild to set
	 */
	public void setSecondFirstChild(VElement secondFirstChild) {
		this.secondFirstChild = secondFirstChild;
	}

	/**
	 * @return the secondSecondChild
	 */
	public VElement getSecondSecondChild() {
		return secondSecondChild;
	}

	/**
	 * @param secondSecondChild the secondSecondChild to set
	 */
	public void setSecondSecondChild(VElement secondSecondChild) {
		this.secondSecondChild = secondSecondChild;
	}

	/**
	 * @param composite
	 */
	public void addSecondChildToFirstChild(VControl composite) {
		final VContainedContainer collection = (VContainedContainer) getFirstChild();
		collection.getChildren().add(composite);
		setFirstSecondChild(composite);
	}

	/**
	 * @param composite
	 */
	public void addFirstChildToSecondChild(VControl composite) {
		final VContainedContainer collection = (VContainedContainer) getSecondChild();
		collection.getChildren().add(composite);
		setSecondFirstChild(composite);

	}

	/**
	 * @param composite
	 */
	public void addSecondChildToSecondChild(VControl composite) {
		final VContainedContainer collection = (VContainedContainer) getSecondChild();
		collection.getChildren().add(composite);
		setSecondSecondChild(composite);
	}

}
