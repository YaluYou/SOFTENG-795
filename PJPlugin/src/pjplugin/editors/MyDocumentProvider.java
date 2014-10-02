package pjplugin.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import pjplugin.Activator;

public class MyDocumentProvider extends FileDocumentProvider
{

    @Override
    protected IDocument createDocument(Object element) throws CoreException
    {
        IDocument document = super.createDocument(element);

    //we will look at document partitions later in this tutorial
        if (document instanceof IDocumentExtension3) 
        {
    IDocumentExtension3 extension3= (IDocumentExtension3) document;
    IDocumentPartitioner partitioner= 
        new FastPartitioner(Activator.getDefault().getMyPartitionScanner(), 
        PyjamaPartitionScanner.PARTITION_TYPES);
    extension3.setDocumentPartitioner(Activator.PJ_PARTITIONING, partitioner);
    partitioner.connect(document);
        }


        return document;
    }


    @Override
    protected IDocument createEmptyDocument() {
        return null;
    }

}