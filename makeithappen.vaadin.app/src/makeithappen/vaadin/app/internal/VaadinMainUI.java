package makeithappen.vaadin.app.internal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.view.model.vaadin.ECPFVaadinViewRenderer;
import org.eclipse.emf.ecp.view.model.vaadin.ECPVaadinView;

import com.eclipsesource.makeithappen.model.task.TaskFactory;
import com.eclipsesource.makeithappen.model.task.User;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@PreserveOnRefresh
public class VaadinMainUI extends UI {

	private static final long serialVersionUID = 1L;
	final static User user = TaskFactory.eINSTANCE.createUser();

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Test Vaadin ECP");

		try {
			ECPVaadinView ecpVaadinView = ECPFVaadinViewRenderer.INSTANCE.render(user);
			setContent(ecpVaadinView.getComponent());

		} catch (ECPRendererException e) {
			e.printStackTrace();
		}

		EContentAdapter adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				System.out.println(user);
			}
		};
		user.eAdapters().add(adapter);
		setResizeLazy(true);
		setPollInterval(1000);
	}

	@Override
	protected void refresh(VaadinRequest request) {
	}
}