package pjplugin.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * An adapter for running an IWorkspaceRunnable as an IRunnableWithProgress
 *
 */
public class RunnableAdapter implements IRunnableWithProgress {
	
	private IWorkspaceRunnable fWorkspaceRunnable;
	private ISchedulingRule fRule;
	
	/**
	 * The Constructor.
	 * Equivalent to RunnableAdapter(workspaceRunnable, ResourcesPlugin.getWorkspace().getRoot())
	 * @param workspaceRunnable the IWorkspaceRunnable to run.
	 */
	public RunnableAdapter(IWorkspaceRunnable workspaceRunnable) {
		this(workspaceRunnable, ResourcesPlugin.getWorkspace().getRoot());
	}
	
	/**
	 * The Constructor.
	 * @param workspaceRunnable the IWorkspaceRunnable to run.
	 * @param rule the scheduling rule to prevent concurrent resource conflicts
	 */
	public RunnableAdapter(IWorkspaceRunnable workspaceRunnable, ISchedulingRule rule) {
		fWorkspaceRunnable = workspaceRunnable;
		fRule = rule;
	}
	
	public ISchedulingRule getSchedulingRule() {
		return fRule;
	}
	/**
	 * Runs the given IWorkspaceRunnable, using the given scheduling rule.
	 * Note that the code is not run in the UI thread.
	 * @param monitor The progress monitor to notify User of progress.
	 */
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			JavaCore.run(fWorkspaceRunnable, fRule, monitor);
		} 
		catch (OperationCanceledException e) {
			throw new InterruptedException(e.getMessage());
		} 
		catch (CoreException e) {
			throw new InvocationTargetException(e);
		}
	}
}
