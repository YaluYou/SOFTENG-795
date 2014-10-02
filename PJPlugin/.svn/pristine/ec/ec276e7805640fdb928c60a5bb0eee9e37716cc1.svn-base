
package pjplugin.builder;

import pjplugin.builder.MarkerManager;
import pjplugin.builder.FileBuilder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * The visitor for incremental project builds. Instances of this can be used to 
 * visit all files that have been modified since the last save.
 *
 * The conventional builder (and delta builder) constitute this package.
 * The idea is to automate the call to PT compiler and then Java compiler.
 */
public class BuildDeltaVisitor implements IResourceDeltaVisitor {
	private static final String PYJAMA_FILE_EXTENSION = ".pj";
	/**
	 * The Constructor. Currently does nothing.
	 */
	public BuildDeltaVisitor() {
		
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		
		//  get the resource this change represented
		IResource resource= delta.getResource();

		String relPath= resource.getFullPath().toString();
		
		int index= relPath.lastIndexOf('/');
		if (index == -1) {
			return false;
		}
		relPath= relPath.substring(0, index);
		
		if (relPath.lastIndexOf('/') > 0) {
			IJavaProject javaProj= JavaCore.create(resource.getProject());
			if (javaProj == null) {
				return false;
			}
			String outputPath= javaProj.getOutputLocation().toString();
			
			if (relPath.equals(outputPath)) {
				return false;
			}
		}
		
		String fullPath= resource.getLocationURI().getPath();

		if (fullPath.endsWith(PYJAMA_FILE_EXTENSION)) {
			MarkerManager.getInstance().deletePYJAMAMarkers(resource);			
			try {
				FileBuilder.invokeCompiler(fullPath, delta.getResource());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}
