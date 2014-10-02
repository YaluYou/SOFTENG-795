package pjplugin.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import pjplugin.Activator;

/**
 * Adding the nature
 *
 */
public class FileBuilderNature implements IProjectNature {

	/**
	 * The ID for this nature.
	 */
	public static final String NATURE_ID = Activator.PLUGIN_ID + ".pyjamaFileBuilderNature";
	//  NOTE: this must be the same as the declared name for the nature in MANIFEST.MF
	
	private IProject fProject;

	/**
	 * Configures the nature for this project.
	 * Current implementation adds the pyjama file builder to the project,
	 * then orders a full build as a background Job.
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	@Override
	public void configure() throws CoreException {
		//  associate this builder with the project
		FileBuilder.addBuilderToProject(fProject);
		
		//  issue order to rebuild project
		new Job("Pyjama File Build") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					//  invoke a full build
					fProject.build(
							FileBuilder.FULL_BUILD,
							FileBuilder.BUILDER_ID,
							null, monitor);
				}
				catch (CoreException exception) {
					//log.logError(exception);
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}

	/**
	 * Deconfigures the nature for this project.
	 * The current implementation removes the PTJava builder from the project.
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	@Override
	public void deconfigure() throws CoreException {
		FileBuilder.removeBuilderFromProject(fProject);
		//  TO DO: delete markers here
	}


	/**
	 * Returns the project to which this nature belongs.
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	@Override
	public IProject getProject() {
		return fProject;
	}

	/**
	 * Sets the project to which this nature belongs.
	 *  
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	@Override
	public void setProject(IProject project) {
		this.fProject = project;
	}

	
	/**
    * Add the nature to the specified project if it does not already have it.
    * 
    * @param project the project to be modified
    */
	public static void addNature(IProject project) {
		// Cannot modify closed projects.
		if (!project.isOpen())
			return;

		// Get the description.
		IProjectDescription description;
		try {
			description = project.getDescription();
		}
		catch (CoreException e) {
			//log.logError(e);
			return;
		}

		// Determine if the project already has the nature.
		List<String> newIds = new ArrayList<String>();
		newIds.addAll(Arrays.asList(description.getNatureIds()));
		int index = newIds.indexOf(NATURE_ID);
		if (index != -1)
			return;
	
		// Add the nature
		newIds.add(NATURE_ID);
		description.setNatureIds(newIds.toArray(new String[newIds.size()]));

		// Save the description.
		try {
			project.setDescription(description, null);
		}
		catch (CoreException e) {
			//log.logError(e);
		}
		
		// add auto build for incremental builds
//		IWorkspace workspace = project.getWorkspace();
//		IWorkspaceDescription workSpaceDescription = workspace.getDescription();
//		if(false == workSpaceDescription.isAutoBuilding()){
//			workSpaceDescription.setAutoBuilding(true);
//			try {
//				workspace.setDescription(workSpaceDescription);
//			} catch (CoreException e) {
//				e.printStackTrace();
//			}
//		}
	}

   /**
    * Determine if the specified project has the receiver's nature associated
    * with it.
    * 
    * @param project the project to be tested
    * @return <code>true</code> if the specified project has the receiver's
    *         nature, else <code>false</code>
    */
	public static boolean hasNature(IProject project) {
		try {
			return project.isOpen() && project.hasNature(NATURE_ID);
		}
		catch (CoreException e) {
			//log.logError(e);
			return false;
		}
	}

	/**
	 * Remove the nature from the specified project if it has the nature
	 * associated.
	 * 
	 * @param project the project to be modified
	 */
	public static void removeNature(IProject project) {

		// Cannot modify closed projects.
		if (!project.isOpen())
			return;

		// Get the description.
		IProjectDescription description;
		try {
			description = project.getDescription();
		}
		catch (CoreException e) {
			//log.logError(e);
			return;
		}

		// Determine if the project has the nature.
		List<String> newIds = new ArrayList<String>();
		newIds.addAll(Arrays.asList(description.getNatureIds()));
		int index = newIds.indexOf(NATURE_ID);
		if (index == -1)
			return;
      
		// Remove the nature
		newIds.remove(index);
		description.setNatureIds(newIds.toArray(new String[newIds.size()]));

		// Save the description.
		try {
			project.setDescription(description, null);
		}
		catch (CoreException e) {
			//log.logError(e);
		}
	}

}
