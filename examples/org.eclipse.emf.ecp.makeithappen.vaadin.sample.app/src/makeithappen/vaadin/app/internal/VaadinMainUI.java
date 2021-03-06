package makeithappen.vaadin.app.internal;

import org.eclipse.emf.ecp.makeithappen.model.task.TaskFactory;
import org.eclipse.emf.ecp.makeithappen.model.task.User;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinView;
import org.eclipse.emf.ecp.view.core.vaadin.ECPVaadinViewRenderer;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Render the eObject.
 *
 * @author Dennis Melzer
 *
 */
// @PreserveOnRefresh
@Theme(ValoTheme.THEME_NAME)
// @Theme(Reindeer.THEME_NAME)
@Push
public class VaadinMainUI extends UI {

	private static final long serialVersionUID = 1L;
	/** User Modell. */
	private static final User USER = TaskFactory.eINSTANCE.createUser();

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Test Vaadin Valo");
		VaadinObservables.activateRealm(this);
		final ECPVaadinView ecpVaadinView = ECPVaadinViewRenderer.INSTANCE.render(USER);
		setContent(ecpVaadinView.getComponent());
	}

	@Override
	protected void refresh(VaadinRequest request) {
	}

}