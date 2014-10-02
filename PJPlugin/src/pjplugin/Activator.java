package pjplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import pjplugin.editors.PyjamaPartitionScanner;
import pjplugin.editors.TextTools;
import pjplugin.preferences.PreferencePage;
import pjplugin.wizards.NewProjectCreationState;


/**
 * The activator class controls the plug-in life cycle
 * This is a straight forward approach of using AbstractUIPlugin
 * 
 * @author vikassingh
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "pjplugin";
	
	public static String currentProjectName;
	
	private static final String PYJAMA_COMPILER_NAME = "pyjama.jar";
	
	private static final String DESTINATION_JAR_FOLDER = "lib";
	
	private static URL fPluginFolderPath;

	private static Activator plugin;

	private TextTools mTextTools;
	
	private static NewProjectCreationState projectCreationState = new NewProjectCreationState();

	public static final String PJ_PARTITIONING = "___pj__partitioning____";

	private PyjamaPartitionScanner fPartitionScanner;

	public PyjamaPartitionScanner getMyPartitionScanner() {
	        if (fPartitionScanner == null)
	            fPartitionScanner= new PyjamaPartitionScanner();
	        return fPartitionScanner;
	}
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		this.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, "From start"));
		
		//PreferencePage.loadRuntimeJarFile();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public synchronized TextTools getTextTools() {
		if (mTextTools == null)
			mTextTools= new TextTools();
		return mTextTools;
	}
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Returns the URL representing the plug-in directory
	 * @return The URL representing the path to the folder where the plug-in is installed
	 */
	public static URL getFolderPath() {
		if (fPluginFolderPath == null) {
			URL url = Platform.getBundle(PLUGIN_ID).getEntry("/");
			
			try {
				fPluginFolderPath = FileLocator.resolve(url);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fPluginFolderPath;
	}

	public static void initNewProject(boolean bIsSample, String name){
		if(false == projectCreationState.bOnGoingCreation){
			projectCreationState.bOnGoingCreation = true;
			projectCreationState.bIsSample = bIsSample;
			projectCreationState.projectName = name;
		}
	}
	
	public static void initNewProject(boolean bIsSample, String name, Object[] pages){
		if(false == projectCreationState.bOnGoingCreation){
			projectCreationState.bOnGoingCreation = true;
			projectCreationState.bIsSample = bIsSample;
			projectCreationState.projectName = name;
			projectCreationState.pages = pages;
			
		}
	}
	
	public static boolean isProjectCreationOn(){
		return projectCreationState.bOnGoingCreation;
	}
	
	public static boolean isSampleProjectCreationOn(){
		return projectCreationState.bOnGoingCreation && projectCreationState.bIsSample;
	}
	
	public static String getCurrentProjectCreationName(){
		if(true == projectCreationState.bOnGoingCreation){
			return projectCreationState.projectName;
		}
		return null;
	}
	
	public static Object[] getCurrentProjectPages(){
		if(true == projectCreationState.bOnGoingCreation){
			return projectCreationState.pages;
		}
		return null;
	}
	
	public static void setCurrentProjectCreationName(String name){
		projectCreationState.projectName = name;
	}
	
	public static void finishProjectCreation(){
		projectCreationState.bOnGoingCreation = false;
		projectCreationState.bIsSample = false;
		projectCreationState.projectName = null;
		projectCreationState.pages = null;
	}
}
