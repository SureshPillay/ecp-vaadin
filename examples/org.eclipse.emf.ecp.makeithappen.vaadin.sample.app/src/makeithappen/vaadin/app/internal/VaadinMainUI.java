package makeithappen.vaadin.app.internal;

import org.eclipse.emf.ecp.makeithappen.model.aufgabe.Benutzer;
import org.eclipse.emf.ecp.makeithappen.model.aufgabe.TaskFactory;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewComponent;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewRenderer;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
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
	final static Benutzer USER = TaskFactory.eINSTANCE.createBenutzer();

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Test Vaadin Valo");
		VaadinObservables.activateRealm(this);
		final ECPVaadinView ecpVaadinView = ECPVaadinViewRenderer.INSTANCE.render(USER);
		setContent(ecpVaadinView.getComponent());

		// see https://vaadin.com/forum#!/thread/8838904
		// testVaadinForum(ecpVaadinView);

	}

	private void testVaadinForum(final ECPVaadinView ecpVaadinView) {
		final ECPVaadinViewComponent ecpVaadinView1 = new ECPVaadinViewComponent();
		final HorizontalLayout horizontalLayout = new HorizontalLayout();

		ecpVaadinView1.setContent(horizontalLayout);
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);

		final VerticalLayout layout1 = new VerticalLayout();
		layout1.addStyleName("group");
		layout1.addComponent(new TextField());
		layout1.addComponent(new TextField());
		layout1.addComponent(new TextField());
		layout1.addComponent(new TextField());
		layout1.addComponent(new TextField());
		layout1.addComponent(new TextField());

		final VerticalLayout layout2 = new VerticalLayout();

		layout2.addStyleName("group");
		layout2.addComponent(new TextField());
		layout2.addComponent(new TextField());
		layout2.setHeight(100, Unit.PERCENTAGE);

		horizontalLayout.addComponent(layout1);
		horizontalLayout.addComponent(layout2);
		setContent(ecpVaadinView.getComponent());
	}

	@Override
	protected void refresh(VaadinRequest request) {
	}

}