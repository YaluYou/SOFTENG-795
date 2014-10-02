package pjplugin.actions;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import pjplugin.constants.PJConstants;

/**
 * @author sriram (sazh998) 
 * 	This class will convert a java file extension to a
 *  pj extension and will be enabled only if the project has a pyjama lib
 *  dependency
 * 
 */
public class ConvertJavaToPyjama implements IObjectActionDelegate {
	public static final String PJ_FILEEXTN = "pj";

	@Override
	public void run(IAction _arg0) {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IStructuredSelection selection = (IStructuredSelection) window
				.getSelectionService().getSelection();

		Object selectedUnit = selection.getFirstElement();

		if (selectedUnit instanceof ICompilationUnit) {
			File currentFile = ((ICompilationUnit) selectedUnit).getResource()
					.getLocation().toFile();
			File newFile = new File(currentFile.getParent() + File.separator
					+ currentFile.getName().split("\\.")[0] + "." + PJ_FILEEXTN);
			currentFile.renameTo(newFile);

			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IProject[] projects = workspace.getRoot().getProjects();
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					e.printStackTrace();
					System.out
							.println("[ConvertJavaToPyjama][run] Error in refreshing the projects");
				}
			}
		}

	}

	@Override
	public void selectionChanged(IAction _arg0, ISelection _arg1) {
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		Boolean canDisplay = Boolean.FALSE;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		ISelection ss = window.getSelectionService().getSelection();

		if (ss instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) window
					.getSelectionService().getSelection();

			try {
				String projectPathLocation = ((CompilationUnit) selection
						.getFirstElement()).getJavaProject()
						.getUnderlyingResource().getLocation().toFile()
						.toString();
				File libDirectory = new File(projectPathLocation
						+ File.separator + PJConstants.LIBS_DIRECTORY);
				Boolean found = Boolean.FALSE;
				if (libDirectory.exists() && libDirectory.isDirectory()) {
					String[] files = libDirectory.list();
					for (String file : files) {
						if (file.equals(PJConstants.PJ_JARNAME)) {
							canDisplay = Boolean.TRUE;
							found = Boolean.TRUE;
						}
					}
				}

				if (!found) {
					libDirectory = new File(projectPathLocation
							+ File.separator + PJConstants.LIB_DIRECTORY);
					if (libDirectory.exists() && libDirectory.isDirectory()) {
						String[] files = libDirectory.list();
						for (String file : files) {
							if (file.equals(PJConstants.PJ_JARNAME)) {
								canDisplay = Boolean.TRUE;
							}
						}
					}
				}

			} catch (JavaModelException e) {
				e.printStackTrace();
				System.out
						.println("[ConvertJavaToPyjama][run] Error in getting the underlying selected project");
			}

			System.setProperty("displayConvertToPJMenu", canDisplay.toString());
		}
	}
}
