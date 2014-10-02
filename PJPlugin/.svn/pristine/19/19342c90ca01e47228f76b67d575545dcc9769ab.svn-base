package pjplugin.menu.handler;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

/**
 * Same as the actions. 
 * As mentioned earlier, the idea is to keep the UI 
 * additions to a minimum. So, this "clean" gets added
 * to menu (Nasser's idea).
 * 
 * @author vikassingh
 *
 */
public class menuActionClean extends AbstractHandler {

	public menuActionClean() {
		// TODO Auto-generated constructor stub
	}

	public void createContributionItems(IServiceLocator serviceLocator,
			IContributionRoot additions) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	    ISelection sel = HandlerUtil.getActiveMenuSelection(event);
	    IStructuredSelection selection = (IStructuredSelection) sel;
	    IProject currentProject = null;

		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();

			if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element)
						.getJavaProject();
				currentProject = jProject.getProject();
			}
			
			if(null != currentProject){
				if (currentProject.exists()) {
					try {
						IFolder srcFolder = currentProject.getFolder("src");
						IResource[] files = srcFolder.members();
						for (IResource file : files) {
							String fileExt = file.getFileExtension();
							if (null != fileExt && fileExt.equals("ptjava")){
								file.delete(true, null);
							}
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
