package pjplugin.editors;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;

import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.IDocument;

import pjplugin.*;

/**
 * Sets up the document to be a pyjama document. Basically separates
 * the document into partitions, which happen to be the same as the
 * Java partitions.
 */
public class DocumentSetupParticipant implements
		IDocumentSetupParticipant {

	/**
	 * The Constructor. Currently does nothing.
	 */
	public DocumentSetupParticipant() {
		
	}
	
	/**
	 * Sets up the document to be ready for use by a text file buffer. 
	 * Current implementation sets up Java partitions.
	 * 
	 * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
	 */
	@Override
	public void setup(IDocument document) {
		TextTools tools = Activator.getDefault().getTextTools();
		tools.setupPTJavaDocumentPartitioner(document, IJavaPartitions.JAVA_PARTITIONING);
	}
}
