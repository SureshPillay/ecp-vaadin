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

	@Override
	protected boolean isMargin(VGroup renderable) {
		return GroupType.NORMAL.equals(renderable.getGroupType());
	}

	@Override
	protected boolean isSpacing(VGroup renderable) {
		return !GroupType.COLLAPSIBLE.equals(renderable.getGroupType());
	}

	@Override
	protected AbstractOrderedLayout getAbstractOrderedLayout(VGroup renderable) {
		VerticalLayout formLayout = new VerticalLayout();
		if (GroupType.NORMAL.equals(renderable.getGroupType())) {
			formLayout.addStyleName("group");
		}
		return formLayout;
	}

	@Override
	protected boolean shouldShowCaption(VGroup renderable) {
		return true;
	}

	@Override
	protected Component getRenderComponent(VGroup renderable, final AbstractOrderedLayout orderedLayout) {
		if (GroupType.COLLAPSIBLE.equals(renderable.getGroupType())) {
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
			orderedLayout.setVisible(renderable.isCollapsed());
			setCollapseStyle(collapseButton, renderable.isCollapsed());
			mainLayout.addStyleName("group");
			return mainLayout;
		}
		return super.getRenderComponent(renderable, orderedLayout);
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
	protected void applyCaption(VGroup renderable, Component controlComponent) {
		if (GroupType.COLLAPSIBLE.equals(renderable.getGroupType())) {
			super.applyCaption(renderable, ((AbstractOrderedLayout) controlComponent).getComponent(0));
			return;

		}
		super.applyCaption(renderable, controlComponent);
	}
}
