package makeithappen.vaadin.app.internal;

import org.eclipse.emf.ecp.makeithappen.model.task.TaskFactory;
import org.eclipse.emf.ecp.makeithappen.model.task.User;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewComponent;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewRenderer;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

// @PreserveOnRefresh
@Theme(ValoTheme.THEME_NAME)
// @Theme(Reindeer.THEME_NAME)
@Push
/**
 * Render the eObject.
 * @author Dennis Melzer
 *
 */
public class VaadinMainUI extends UI {

	private static final long serialVersionUID = 1L;
	final static User USER = TaskFactory.eINSTANCE.createUser();

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Test Vaadin Valo");
		VaadinObservables.activateRealm(this);
		final ECPVaadinView ecpVaadinView = ECPVaadinViewRenderer.INSTANCE.render(USER);

		final ECPVaadinViewComponent ecpVaadinView1 = new ECPVaadinViewComponent();
		ecpVaadinView1.setSizeFull();
		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		ecpVaadinView1.setContent(horizontalLayout);
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);

		final Panel leftPanel = new Panel();
		leftPanel.setSizeFull();
		final VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.setSizeUndefined();
		leftLayout.addStyleName("group");
		leftLayout.addComponent(new TextField());
		leftLayout.addComponent(new TextField());
		leftLayout.addComponent(new TextField());
		leftLayout.addComponent(new TextField());
		leftLayout.addComponent(new TextField());
		leftLayout.addComponent(new TextField());
		leftPanel.setContent(leftLayout);

		final Panel rightPanel = new Panel();
		rightPanel.setSizeFull();
		final VerticalLayout rightLayout = new VerticalLayout();
		rightLayout.setSizeUndefined();
		rightLayout.addStyleName("group");
		rightLayout.addComponent(new TextField());
		rightLayout.addComponent(new TextField());
		rightPanel.setContent(rightLayout);

		horizontalLayout.addComponent(leftPanel);
		horizontalLayout.addComponent(rightPanel);

		setContent(ecpVaadinView1);

		setContent(ecpVaadinView.getComponent());
	}

	@Override
	protected void refresh(VaadinRequest request) {
	}

}