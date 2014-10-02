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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import pjplugin.Activator;
import pjplugin.builder.FileBuilderNature;
import pjplugin.preferences.PreferenceConstants;

/**
 * Creating sample project is an idea to quick start
 * Pyjama dev.
 * 
 * The code implementation does not generate new code files,
 * rather it just copies them from the "sources" folder.
 * 
 * @author vikassingh
 *
 */
public class CreateNewSampleProject extends Wizard implements INewWizard{

	private WizardPage pageSample;
	private NewJavaProjectWizardPageOne pageOne;
	private NewJavaProjectWizardPageTwo pageTwo;
	private IStructuredSelection selection;
	
	private static final String PROJECT_LOGO_LARGE = "icons/parait.png";
	
	/**
	 * Folder for code 
	 */
	private static final String mSrcFolder = "src";
	private static final String mPluginSrcFolder = "sources";
	
	/**
	 * Folder for libraries 
	 */
	private static final String mLibFolder = "lib";
	
	/**
	 * The String for the allowed runtime extension 
	 */
	private static final String mPyjamaJarName = "pyjama.jar";	

	/**
	 * Folder for documents 
	 */
	private static final String mDocFolder = "doc";
	
	private static final String mDocsLicense = "license.txt";
	
	private static final String mDocsHelp = "help.pdf";
	
	private final static String SETTINGS_PAGE_TITLE = "Create a new Sample Pyjama Project"; 
	private final static String PAGE_ONE_DESC = "Select new project Settings.";
	private final static String PAGE_TWO_DESC = "Select build settings for the project.";

	
	public CreateNewSampleProject() {
		this(null, null);
		Activator.finishProjectCreation();
	}

	public CreateNewSampleProject(NewJavaProjectWizardPageOne firstPage, NewJavaProjectWizardPageTwo secondPage) {
		pageOne = firstPage;
		pageTwo = secondPage;
	}


	public void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException{
		//pageOne.setProjectName(Activator.getCurrentProjectCreationName());
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
		
		FileBuilderNature.addNature(project);
		
		Activator.finishProjectCreation();
		
		//  accept finish request
		return true;
	}

	public static void createNewProjectOperations(IProject currentProject) throws IOException, CoreException {

		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "create proj"));
		IFolder libFolder = currentProject.getFolder(mLibFolder);
		libFolder.create(true, true, null);
		IFolder docFolder = currentProject.getFolder(mDocFolder);
		docFolder.create(true, true, null);
		IFolder srcFolder = currentProject.getFolder(mSrcFolder);
		try{
			srcFolder.create(false, true, null);
		}catch (CoreException e) {
			// mostly already there
		}
		
		Path jarSrcPath = new Path("/" + mLibFolder + "/" + mPyjamaJarName);
		
		Path licenseSrcPath = new Path("/" + mDocFolder + "/" + mDocsLicense);
		Path helpSrcPath = new Path("/" + mDocFolder + "/" + mDocsHelp);
		Path srcPath = new Path("/" + mPluginSrcFolder + "/" + Activator.getCurrentProjectCreationName() + ".pj");
		
		URL installJarURL= Platform.getBundle(Activator.PLUGIN_ID).getEntry(jarSrcPath.toString());
		
		URL licenseSrcURL= Platform.getBundle(Activator.PLUGIN_ID).getEntry(licenseSrcPath.toString());
		URL helpSrcURL = Platform.getBundle(Activator.PLUGIN_ID).getEntry(helpSrcPath.toString());
		URL pluginSrcURL = Platform.getBundle(Activator.PLUGIN_ID).getEntry(srcPath.toString());

		if(null == installJarURL || 
				null == licenseSrcURL || 
				null == helpSrcURL ||
				null == pluginSrcURL /*||
				null == paraItJarURL ||
				null == ptCompilerJarURL ||
				null == ptRuntimeJarURL*/){
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "Project creation could not be completed, an internal error occurred.");
			return;
		}
		URL localJarURL = FileLocator.toFileURL(installJarURL);
		
        URL locallicenseURL = FileLocator.toFileURL(licenseSrcURL);
        URL localhelpURL = FileLocator.toFileURL(helpSrcURL);
        URL localsrcURL = FileLocator.toFileURL(pluginSrcURL);
		
		if(true == currentProject.exists() &&
				false == currentProject.isOpen()){
			// TODO: pass a progress monitor
			currentProject.open(null);
		}
		
		IFile pyjamaJarFile = libFolder.getFile(mPyjamaJarName);
		FileInputStream fileStream = new FileInputStream(localJarURL.getFile());
		pyjamaJarFile.create(fileStream, false, null);
		pjplugin.preferences.PreferencePage.setFgDefaultRuntimeJarPath(pyjamaJarFile.getLocation());
		
		IFile licenseFile = docFolder.getFile(mDocsLicense);
		FileInputStream fileStream1 = new FileInputStream(locallicenseURL.getFile());
		licenseFile.create(fileStream1, false, null);
		IFile helpFile = docFolder.getFile(mDocsHelp);
		FileInputStream fileStream2 = new FileInputStream(localhelpURL.getFile());
		helpFile.create(fileStream2, false, null);
		
		IFile srcFile = srcFolder.getFile(Activator.getCurrentProjectCreationName() + ".pj");
		FileInputStream srcFileStream = new FileInputStream(localsrcURL.getFile());
		srcFile.create(srcFileStream, false, null);
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
		Object[] pages = new Object[3];
		pageSample = new NewSampleProjectcreationPageOne(null);
		pageSample.setTitle("Create a new Sample Pyjama Project");
		pageSample.setTitle("Select the type of Sample Project.");
		addPage(pageSample);
		pages[0] = (Object)pageSample;
		
		
		if (pageOne == null)
			pageOne = new NewSampleProjectCreationPageTwo();
		pageOne.setPreviousPage(pageSample);
		pageOne.setProjectName(Activator.getCurrentProjectCreationName());
		
		pageOne.setTitle(SETTINGS_PAGE_TITLE);
		pageOne.setDescription(PAGE_ONE_DESC);
		addPage(pageOne);
		pages[1] = (Object)pageOne;

		if (pageTwo == null)
			pageTwo = new NewJavaProjectWizardPageTwo(pageOne);
		pageTwo.setTitle(SETTINGS_PAGE_TITLE);
		pageTwo.setDescription(PAGE_TWO_DESC);		
		addPage(pageTwo);
		pages[2] = (Object)pageTwo;

        NewProjectCreationState.pages = pages;
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
