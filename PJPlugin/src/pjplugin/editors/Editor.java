package pjplugin.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import pjplugin.Activator;


@SuppressWarnings("restriction")
public class Editor extends CompilationUnitEditor{

	public Editor(){
		super();
	}
	
	@SuppressWarnings("restriction")
	protected void setPreferenceStore(IPreferenceStore store) {
		super.setPreferenceStore(store);
		SourceViewerConfiguration sourceViewerConfiguration = getSourceViewerConfiguration();
		if (sourceViewerConfiguration == null || sourceViewerConfiguration instanceof JavaSourceViewerConfiguration) {
			JavaPlugin defaultPlugin = JavaPlugin.getDefault();
			JavaTextTools javaTextTools = defaultPlugin.getJavaTextTools();
			JavaTextTools textTools= javaTextTools;
			setSourceViewerConfiguration(new EditorSourceViewerConfiguration(textTools.getColorManager(), store, this, IJavaPartitions.JAVA_PARTITIONING));
//			setSourceViewerConfiguration(new EditorSourceViewerConfiguration(textTools.getColorManager(), 
//					store, this, Activator.PJ_PARTITIONING));
		}

	}
	
}
