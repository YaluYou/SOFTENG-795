package pjplugin.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import pjplugin.Activator;
import pjplugin.builder.FileBuilder;
import pjplugin.builder.FileBuilderNature;
import pjplugin.builder.ProjectType;
import pjplugin.constants.PJConstants;
import pjplugin.preferences.PreferenceConstants;

/**
 * The folder structure is formed here.
 * We also things like help file, Pyjama jar in library folder.
 * But the implementation is straight forward plugin dev implementation.
 * 
 * Branding is important.
 * 
 * @author vikassingh
 *
 */
public class CreateNewProject extends Wizard implements INewWizard{

	private NewJavaProjectWizardPageOne pageOne;
	private NewJavaProjectWizardPageTwo pageTwo;
	private IStructuredSelection selection;
	
	private static final String PROJECT_LOGO_LARGE = "icons/parait.png";
	/**
	 * Folder for libraries 
	 */
	private static final String mLibFolder = "lib";
	/**
	 * Folder for documents 
	 */
	private static final String mDocFolder = "doc";
	
	private static final String mDocsLicense = "license.txt";
	
	private static final String mDocsHelp = "help.pdf";
	
	private final static String PAGE_TITLE = "Create a new Pyjama Project";
	private final static String PAGE_ONE_DESC = "Select new project Settings.";
	private final static String PAGE_TWO_DESC = "Select build settings for the project.";
	
	public CreateNewProject() {
		this(null, null);
	}

	public CreateNewProject(NewJavaProjectWizardPageOne firstPage, NewJavaProjectWizardPageTwo secondPage) {
		pageOne = firstPage;
		pageTwo = secondPage;
	}


	public void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException{
        pageTwo.performFinish(monitor);
	}
	
	/**
	 * Performs actions to create the new project and add the Pyjama nature to the project.
	 */
	@Override
	public boolean performFinish() {
 
		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "perform finish"));
//		System.err.println("Perform finish");
		IWorkspaceRunnable op= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
				try {
					finishPage(monitor);
				} catch (InterruptedException e) {
					throw new OperationCanceledException(e.getMessage());
				}
			}
		};
		
		try {
			//  see Platform Plug-in developers guide > Programmer's Guide > Resources overview > Modifying the workspace
			ISchedulingRule rule= null;
			Job job= Job.getJobManager().currentJob();
			if (job != null)
				rule= job.getRule();
			IRunnableWithProgress runnable= null;
			if (rule != null)
				runnable= new RunnableAdapter(op, rule);
			else
				runnable= new RunnableAdapter(op, getSchedulingRule());
			getContainer().run(true, true, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch  (InterruptedException e) {
			return false;
		}
		
		//  add Pyjama builder nature to project
		IJavaProject javaProject = pageTwo.getJavaProject();
		IProject project = javaProject.getProject();

		try {
			Activator.currentProjectName = project.getName();
			createNewProjectOperations(project);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		try {
			IClasspathEntry entries[] = javaProject.getRawClasspath();
			
			List<IClasspathEntry> list = new ArrayList<IClasspathEntry>(entries.length + 1);
			for (int i = 0; i < entries.length; i++) {
				list.add(entries[i]);
			}
			
			//  add the java runtime library
			IPath path = null;
			path = pjplugin.preferences.PreferencePage.getFgDefaultRuntimeJarPath();

			list.add(JavaCore.newLibraryEntry(path, null, null));
			
			IClasspathEntry updatedEntries[] = new IClasspathEntry[list.size()];
			list.toArray(updatedEntries);
			javaProject.setRawClasspath(updatedEntries, null);
		}
		catch (JavaModelException e) {
			e.printStackTrace();
		}				
		
		FileBuilder.projectType = ProjectType.DESKTOP;
		FileBuilderNature.addNature(project);
		
		//  accept finish request
		return true;
	}

	public static void createNewProjectOperations(IProject currentProject) throws IOException, CoreException {

		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "create proj"));
		IFolder libFolder = currentProject.getFolder(mLibFolder);
		libFolder.create(true, true, null);
		IFolder docFolder = currentProject.getFolder(mDocFolder);
		docFolder.create(true, true, null);
		
		ErrorDialog.openError(null, "Error", "came to me", Status.OK_STATUS);
		Path jarSrcPath = new Path("/" + mLibFolder + "/" + PJConstants.PJ_JARNAME);

		Path licenseSrcPath = new Path("/" + mDocFolder + "/" + mDocsLicense);
		Path helpSrcPath = new Path("/" + mDocFolder + "/" + mDocsHelp);
		
		URL installJarURL= Platform.getBundle(Activator.PLUGIN_ID).getEntry(jarSrcPath.toString());
		
		URL licenseSrcURL= Platform.getBundle(Activator.PLUGIN_ID).getEntry(licenseSrcPath.toString());
		URL helpSrcURL = Platform.getBundle(Activator.PLUGIN_ID).getEntry(helpSrcPath.toString());

		if(null == installJarURL || null == licenseSrcURL || null == helpSrcURL){
			return;
		}
		
		URL localJarURL = FileLocator.toFileURL(installJarURL);
		
        URL locallicenseURL = FileLocator.toFileURL(licenseSrcURL);
        URL localhelpURL = FileLocator.toFileURL(helpSrcURL);
		
		if(true == currentProject.exists() &&
				false == currentProject.isOpen()){
			// TODO: pass a progress monitor
			currentProject.open(null);
		}
		
		IFile pyjamaJarFile = libFolder.getFile(PJConstants.PJ_JARNAME);
		FileInputStream fileStream = new FileInputStream(localJarURL.getFile());
		pyjamaJarFile.create(fileStream, false, null);
		pjplugin.preferences.PreferencePage.setFgDefaultRuntimeJarPath(pyjamaJarFile.getLocation());
		
		IFile licenseFile = docFolder.getFile(mDocsLicense);
		FileInputStream fileStream1 = new FileInputStream(locallicenseURL.getFile());
		licenseFile.create(fileStream1, false, null);
		IFile helpFile = docFolder.getFile(mDocsHelp);
		FileInputStream fileStream2 = new FileInputStream(localhelpURL.getFile());
		helpFile.create(fileStream2, false, null);
	}	
	/**
	 * Initializes the wizard.
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
        ImageDescriptor desc = Activator.getImageDescriptor(PROJECT_LOGO_LARGE);
        setDefaultPageImageDescriptor(desc);	
        
	}
	
	/**
	 * Add pages to the wizard. Current implementation adds an instances of:
	 * {@link NewFileCreationPage}, 
	 * {@link NewProjectcreationPageOne},
	 * {@link PTJavaWizardPage}, to the wizard if they haven't already been added.
	 */
	@Override
	public void addPages() {
		if (pageOne == null)
			pageOne = new NewJavaProjectTypeWizardPageOne();

		pageOne.setTitle(PAGE_TITLE);
		pageOne.setDescription(PAGE_ONE_DESC);
		addPage(pageOne);

		if (pageTwo == null)
			pageTwo = new NewJavaProjectWizardPageTwo(pageOne);
		pageTwo.setTitle(PAGE_TITLE);
		pageTwo.setDescription(PAGE_TWO_DESC);
		addPage(pageTwo);

	}
	
	/**
	 * Returns the scheduling rule for creating the element.
	 * @return returns the scheduling rule
	 */
	protected ISchedulingRule getSchedulingRule() {
		return ResourcesPlugin.getWorkspace().getRoot(); // look all by default
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	public boolean performCancel() {
		pageTwo.performCancel();
		return super.performCancel();
	}

}
