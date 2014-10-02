package pjplugin.editors;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

/**
 * TBD. This is important.
 * 
 * @author vikassingh
 *
 */
public class DirectiveEditStrategy implements IAutoEditStrategy{

	@Override
	public void customizeDocumentCommand(IDocument document,
			DocumentCommand command) {

	}
	
    private void configureCommand(DocumentCommand command){
        //puts the caret between both the quotes

        command.caretOffset = command.offset + 1;
        command.shiftsCaret = false;
    }

}
