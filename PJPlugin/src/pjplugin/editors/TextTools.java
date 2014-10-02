package pjplugin.editors;

import org.eclipse.jdt.internal.ui.text.FastJavaPartitionScanner;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * The tools required to configure a PTJava text viewer.
 * Provides a Java partition scanner and methods for setting up document partitions.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be sub-classed.
 * </p>
 */
public class TextTools {
	/**
	 * The Constructor. Currently does nothing.
	 */
	public TextTools() {
		
	}
	/**
	 * Array with legal content types.
	 * @since 3.0
	 */
	private final static String[] LEGAL_CONTENT_TYPES= new String[] {
		IJavaPartitions.JAVA_DOC,
		IJavaPartitions.JAVA_MULTI_LINE_COMMENT,
		IJavaPartitions.JAVA_SINGLE_LINE_COMMENT,
		IJavaPartitions.JAVA_STRING,
		IJavaPartitions.JAVA_CHARACTER
	};	
	
	/**
	 * Disposes all the individual tools of this tools collection.
	 */
	public void dispose() {
	}
	
	/**
	 * Returns a scanner which is configured to scan
	 * PTJava-specific partitions, which are multi-line comments,
	 * Javadoc comments, and regular Java source code.
	 *
	 * @return a Java partition scanner
	 */
	public IPartitionTokenScanner getPartitionScanner() {
		//  replacing with java partitioner
		return new FastJavaPartitionScanner();
		//  doesn't really matter what partitioner is used
		//return new PTJavaPartitionScanner();
	}
	
	/**
	 * Factory method for creating a Java-specific document partitioner
	 * using this object's partitions scanner. This method is a
	 * convenience method.
	 *
	 * @return a newly created Java document partitioner
	 */
	public IDocumentPartitioner createDocumentPartitioner() {
		return new FastPartitioner(getPartitionScanner(), LEGAL_CONTENT_TYPES);
	}
	
	/**
	 * Sets up the Java document partitioner for the given document for the default partitioning.
	 * Equivalent to {@link TextTools#setupPTJavaDocumentPartitioner(IDocument, String)}.
	 *
	 * @param document the document to be set up
	 * @since 3.0
	 */
	public void setupPTJavaDocumentPartitioner(IDocument document) {
		setupPTJavaDocumentPartitioner(document, IDocumentExtension3.DEFAULT_PARTITIONING);
	}
	
	/**
	 * Sets up the PTJava document partitioner for the given document for the given partitioning.
	 * 
	 * @param document the document to be set up
	 * @param partitioning the document partitioning
	 * @since 3.0
	 */
	public void setupPTJavaDocumentPartitioner(IDocument document, String partitioning) {
		IDocumentPartitioner partitioner= createDocumentPartitioner();
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3= (IDocumentExtension3) document;
			extension3.setDocumentPartitioner(partitioning, partitioner);
		} else {
			document.setDocumentPartitioner(partitioner);
		}
		partitioner.connect(document);
	}
}
