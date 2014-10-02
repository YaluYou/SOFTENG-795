package pjplugin.editors;

import java.util.List;

import org.eclipse.jdt.internal.ui.text.AbstractJavaScanner;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.ITextEditor;

import pjplugin.Activator;


/**
 * This is the most important part for the editor. This is where all the
 * configuration is done, such as telling the system that we will 
 * handle the single line comments (directives), using our own damage
 * repairer, and things like that.
 * 
 * The source viewer configuration for the Pyjama text editor. This class controls
 * features such as syntax highlighting, code completion and text hovers for the 
 * editor.
 * 
 * Getting the content assistant for single line comments is hacky (at least to me).
 * This can be cleaner.
 *
 * @author vikassingh
 */
public class EditorSourceViewerConfiguration extends JavaSourceViewerConfiguration {
	
	//  The code scanners (for each document partition)
	private AbstractJavaScanner fCodeScanner;
	
	/**
	 * Creates a new Java source viewer configuration for viewers in the given editor
	 * using the given preference store, the color manager and the specified document partitioning.
	 *
	 * @param colorManager the color manager
	 * @param preferenceStore the preference store, can be read-only
	 * @param editor the editor in which the configured viewer(s) will reside, or <code>null</code> if none
	 * @param partitioning the document partitioning for this configuration, or <code>null</code> for the default partitioning
	 */
	public EditorSourceViewerConfiguration(IColorManager colorManager, IPreferenceStore preferenceStore, ITextEditor editor, String partitioning) {
		super(colorManager, preferenceStore, editor, partitioning);
		initializeScanners();
	}	
	
	@Override
	 public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
	  {
		ContentAssistant assistant = (ContentAssistant)super.getContentAssistant(sourceViewer);

		IContentAssistProcessor cap = new DirectiveCompletionProcessor();
		assistant.setContentAssistProcessor(cap, IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);

	      return assistant;    
	  }
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		if(contentType.equals(IJavaPartitions.JAVA_SINGLE_LINE_COMMENT)){
			
		}
		return super.getAutoEditStrategies(sourceViewer, contentType);
	}
	
	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		if(contentType.equals(IJavaPartitions.JAVA_SINGLE_LINE_COMMENT)){
			return new DirectiveTextHover();
		}
		return super.getTextHover(sourceViewer, contentType);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration#getCodeScanner()
	 */
	@Override
	protected RuleBasedScanner getCodeScanner() {
		return this.fCodeScanner;
		
	}
	
	private void initializeScanners() {
		//  Scanner for DEFAULT partitions (i.e. the ones that don't get recognized)
		fCodeScanner = new CodeScanner(getColorManager(), fPreferenceStore);
		//  NOTE: the other scanners are initialized in JavaSourceViewerConfiguration constructor
	}

	/*
	 * @see SourceViewerConfiguration#getPresentationReconciler(ISourceViewer)
	 */
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {

		PresentationReconciler reconciler= new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(getCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr= new DefaultDamagerRepairer(getJavaDocScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_DOC);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_DOC);

		dr= new DefaultDamagerRepairer(getMultilineCommentScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_MULTI_LINE_COMMENT);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_MULTI_LINE_COMMENT);
		
		dr = new DefaultDamagerRepairer(new SingleLineCommentScanner(getColorManager(), fPreferenceStore));
		reconciler.setDamager(dr, IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_SINGLE_LINE_COMMENT);

		dr= new DefaultDamagerRepairer(getStringScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_STRING);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_STRING);

		dr= new DefaultDamagerRepairer(getStringScanner());
		reconciler.setDamager(dr, IJavaPartitions.JAVA_CHARACTER);
		reconciler.setRepairer(dr, IJavaPartitions.JAVA_CHARACTER);

		return reconciler;
	}
	/*
	 * @see org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration#handlePropertyChangeEvent(PropertyChangeEvent)
	 */
	@Override
	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		//  to handle when someone changes the java editor properties
		super.handlePropertyChangeEvent(event);
		if (fCodeScanner.affectsBehavior(event))
			fCodeScanner.adaptToPreferenceChange(event);
	}
	
//	@Override
//	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
//		return Activator.PJ_PARTITIONING;
//	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		  return new String[] { 		IJavaPartitions.JAVA_DOC,
					IJavaPartitions.JAVA_MULTI_LINE_COMMENT,
					IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
					IJavaPartitions.JAVA_STRING,
					IJavaPartitions.JAVA_CHARACTER
					};
	}
}
