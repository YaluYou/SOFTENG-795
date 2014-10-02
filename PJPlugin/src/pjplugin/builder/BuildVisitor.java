package pjplugin.builder;

import pjplugin.builder.MarkerManager;
import pjplugin.builder.FileBuilder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;


public class BuildVisitor implements IResourceVisitor {
	private static final String PYJAMA_FILE_EXTENSION = ".pj";

	public BuildVisitor() {
		// nothing to do
	}

	@Override
	public boolean visit(IResource resource) throws CoreException {
		System.out.println("inside visit buildVistor");//wk
		String relPath= resource.getFullPath().toString();
		int index= relPath.lastIndexOf('/');
		if (index == -1) {
			return false;
		}
		relPath= relPath.substring(0, index);
		if (relPath.lastIndexOf('/') > 0) {
			//  latter case
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
		if (fullPath != null &&
				fullPath.endsWith(PYJAMA_FILE_EXTENSION)) {
			MarkerManager.getInstance().deletePYJAMAMarkers(resource);
			try {
				FileBuilder.invokeCompiler(fullPath, resource);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

}
