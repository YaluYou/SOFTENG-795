package pjplugin.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import org.eclipse.jface.resource.ImageDescriptor;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import pjplugin.Activator;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "pj". If a sample multi-configPage editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 * 
 * Branding is important.
 */

public class CreateNewClass extends Wizard implements INewWizard {
	private NewFileCreationPage configPage;
	private final static String PAGE_NAME = "Pyjama";
	private IWorkbench fWorkbench;
	private IStructuredSelection fSelection;	
	
	private static final String PROJECT_LOGO_LARGE = "icons/parait.png";

	/**
	 * Adding the pages to the wizard.
	 */

	public void addPages() {
		configPage = new NewFileCreationPage(PAGE_NAME, fWorkbench, fSelection);
		addPage(configPage);
	}
	

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		return configPage.finish();
	}
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "Pyjama", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		fWorkbench = workbench;
		fSelection = selection;
		
        ImageDescriptor desc = Activator.getImageDescriptor(PROJECT_LOGO_LARGE);
        setDefaultPageImageDescriptor(desc);
	}
}