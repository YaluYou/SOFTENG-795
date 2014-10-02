package pjplugin.actions;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

import pjplugin.Activator;
import pjplugin.builder.FileBuilder;

/**
 * "PJPlugin tries to be as close to Eclipse UI as possible." 
 * Please keep that in mind before adding new UI elements, 
 * most of the times we are able to achieve new actions within
 * the existing menus.
 *
 * Clean action takes care of redundant (to user) intermediate files.
 * 
 * @author vikassingh
 */
public class ActionClean implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	private IProject project = null;

	public ActionClean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		IProject currentProject = getCurrentSelectedProject();
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

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		ISelectionService selectionService = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService();
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();

			if (element instanceof IResource) {
				this.project = ((IResource) element).getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element)
						.getJavaProject();
				this.project = jProject.getProject();
			}
		}
	}

	public IProject getCurrentSelectedProject() {
		IProject project = null;
		ISelectionService selectionService = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService();

		ISelection selection = selectionService.getSelection();

		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();

			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element)
						.getJavaProject();
				project = jProject.getProject();
			}
		}
		return project;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		this.window = window;
	}

}
