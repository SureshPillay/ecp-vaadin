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
	 * @param root
	 */
	public HierarchyViewModelHandle(VElement root) {
		this.root = root;
	}

	/**
	 * @return the horizontal
	 */
	public VElement getRoot() {
		return this.root;
	}

	/**
	 * @param renderable
	 */
	public void addFirstChildToRoot(VElement renderable) {
		this.firstChild = renderable;
		final VContainedContainer collection = (VContainedContainer) this.root;
		collection.getChildren().add((VContainedElement) renderable);

	}

	/**
	 * @param renderable
	 */
	public void addSecondChildToRoot(VElement renderable) {
		setSecondChild(renderable);
		final VContainedContainer collection = (VContainedContainer) this.root;
		collection.getChildren().add((VContainedElement) renderable);

	}

	/**
	 * @return the first child
	 */
	public VElement getFirstChild() {
		return this.firstChild;
	}

	/**
	 * @return the secondChild
	 */
	public VElement getSecondChild() {
		return this.secondChild;
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
		return this.firstFirstChild;
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
		return this.firstSecondChild;
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
		return this.secondFirstChild;
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
		return this.secondSecondChild;
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
