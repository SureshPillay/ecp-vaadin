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
package org.eclipse.emf.ecp.view.group.vaadin;

import org.eclipse.emf.ecp.view.core.vaadin.AbstractContainerRendererVaadin;
import org.eclipse.emf.ecp.view.spi.group.model.GroupType;
import org.eclipse.emf.ecp.view.spi.group.model.VGroup;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;

public class GroupLayoutRendererVaadin extends AbstractContainerRendererVaadin<VGroup> {

	public static final String GROUP_STYLE_NAME = "group";

	@Override
	protected boolean isMargin() {
		return GroupType.NORMAL.equals(getVElement().getGroupType());
	}

	@Override
	protected boolean isSpacing() {
		return !GroupType.COLLAPSIBLE.equals(getVElement().getGroupType());
	}

	@Override
	protected AbstractOrderedLayout getAbstractOrderedLayout() {
		VerticalLayout formLayout = new VerticalLayout();
		if (GroupType.NORMAL.equals(getVElement().getGroupType())) {
			formLayout.addStyleName(GROUP_STYLE_NAME);
		}
		return formLayout;
	}

	@Override
	protected boolean shouldShowCaption() {
		return true;
	}

	@Override
	protected Component getRenderComponent(final AbstractOrderedLayout orderedLayout) {
		if (GroupType.COLLAPSIBLE.equals(getVElement().getGroupType())) {
			VerticalLayout mainLayout = new VerticalLayout();
			NativeButton collapseButton = new NativeButton("", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					boolean switchVisible = !orderedLayout.isVisible();
					setCollapseStyle(event.getButton(), switchVisible);
					orderedLayout.setVisible(switchVisible);
				}

			});
			collapseButton.setWidth(100, Unit.PERCENTAGE);
			mainLayout.addComponent(collapseButton);
			mainLayout.addComponent(orderedLayout);
			orderedLayout.setMargin(true);
			orderedLayout.setSpacing(true);
			orderedLayout.setVisible(getVElement().isCollapsed());
			setCollapseStyle(collapseButton, getVElement().isCollapsed());
			mainLayout.addStyleName(GROUP_STYLE_NAME);
			return mainLayout;
		}
		return super.getRenderComponent(orderedLayout);
	}

	private void setCollapseStyle(Button collapseButton, boolean collapse) {
		String styleExpand = "collapsible-panel-expand";
		String styleCollapsed = "collapsible-panel-collapsed";
		if (collapse) {
			collapseButton.addStyleName(styleExpand);
			collapseButton.removeStyleName(styleCollapsed);
		} else {
			collapseButton.addStyleName(styleCollapsed);
			collapseButton.removeStyleName(styleExpand);
		}
	}

	@Override
	protected void applyCaption(Component controlComponent) {
		if (GroupType.COLLAPSIBLE.equals(getVElement().getGroupType())) {
			super.applyCaption(((AbstractOrderedLayout) controlComponent).getComponent(0));
			return;

		}
		super.applyCaption(controlComponent);
	}
}
