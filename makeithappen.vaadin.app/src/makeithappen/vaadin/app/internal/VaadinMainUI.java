package makeithappen.vaadin.app.internal;

import org.eclipse.emf.ecp.view.core.vaadin.ECPFVaadinViewRenderer;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.eclipsesource.makeithappen.model.task.TaskFactory;
import com.eclipsesource.makeithappen.model.task.User;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

//@PreserveOnRefresh
@Theme(ValoTheme.THEME_NAME)
// @Theme(Reindeer.THEME_NAME)
@Push
public class VaadinMainUI extends UI {

	private static final long serialVersionUID = 1L;
	final static User USER = TaskFactory.eINSTANCE.createUser();

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Test Vaadin Valo");
		VaadinObservables.activateRealm(UI.getCurrent());
		ECPVaadinView ecpVaadinView = ECPFVaadinViewRenderer.INSTANCE.render(USER);
		setContent(ecpVaadinView.getComponent());
	}

	@Override
	protected void refresh(VaadinRequest request) {
	}

}