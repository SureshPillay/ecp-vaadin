package makeithappen.vaadin.app.internal;

import org.eclipse.emf.ecp.view.core.vaadin.ECPFVaadinViewRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.compart.dbqp.web.ui.model.ModelFactory;
import com.compart.dbqp.web.ui.model.WebUi;
import com.eclipsesource.makeithappen.model.task.TaskFactory;
import com.eclipsesource.makeithappen.model.task.User;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//@PreserveOnRefresh
@Theme(ValoTheme.THEME_NAME)
// @Theme(Reindeer.THEME_NAME)
public class VaadinMainUI extends UI {

	private static final long serialVersionUID = 1L;
	final static WebUi WEB_UI = ModelFactory.eINSTANCE.createWebUi();
	final static User USER = TaskFactory.eINSTANCE.createUser();

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Test Vaadin Valo");
		VaadinObservables.activateRealm(UI.getCurrent());
		ECPVaadinView ecpVaadinView = ECPFVaadinViewRenderer.INSTANCE.render(USER);
		setContent(ecpVaadinView.getComponent());

		setResizeLazy(true);
		setPollInterval(1000);
	}

	//
	// @Override
	// protected void init(VaadinRequest request) {
	// getPage().setTitle("Test Vaadin Valo");
	// final WebUiConfiguration uiConfiguration = ModelFactory.eINSTANCE.createWebUiConfiguration();
	// WEB_UI.setConfiguration(uiConfiguration);
	//
	// VerticalLayout verticalLayout = new VerticalLayout();
	// verticalLayout.setMargin(true);
	// VaadinObservables.activateRealm(UI.getCurrent());
	// ECPVaadinView ecpVaadinView = ECPFVaadinViewRenderer.INSTANCE.render(WEB_UI);
	// verticalLayout.addComponent(ecpVaadinView.getComponent());
	// ECPVaadinView ecpVaadinView1 = ECPFVaadinViewRenderer.INSTANCE.render(uiConfiguration);
	// verticalLayout.addComponent(ecpVaadinView1.getComponent());
	//
	// setContent(verticalLayout);
	//
	// setResizeLazy(true);
	// setPollInterval(1000);
	// }

	@Override
	protected void refresh(VaadinRequest request) {
	}

	protected Component renderControl() {
		VerticalLayout layout = new VerticalLayout();
		// layout.setSizeFull();
		final Table table = new Table();
		table.setSelectable(true);
		table.setSizeFull();
		table.setPageLength(10);
		table.setCaption("Test");
		BeanItemContainer<Object> indexedContainer = new BeanItemContainer(Bean.class);

		setVisibleColumns(table, indexedContainer);

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addStyleName("table-button-toolbar");
		Button add = new Button();
		add.addStyleName("table-add");
		add.addClickListener(new Button.ClickListener() {
			int count = 1;

			@Override
			public void buttonClick(ClickEvent event) {
				Bean bean = new Bean("Mung bean" + this.count, 1452.0);
				bean.setName1("" + this.count);
				bean.setName2("test");
				indexedContainer.addBean(bean);

			}
		});
		horizontalLayout.addComponent(add);
		Button remove = new Button();
		remove.addStyleName("table-remove");
		horizontalLayout.addComponent(remove);

		layout.addComponent(horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.TOP_RIGHT);
		layout.addComponent(table);
		return layout;

	}

	private void setVisibleColumns(final Table table, BeanItemContainer<Object> indexedContainer) {

		table.setContainerDataSource(indexedContainer);
		table.setVisibleColumns("name1", "name2");
	}

}