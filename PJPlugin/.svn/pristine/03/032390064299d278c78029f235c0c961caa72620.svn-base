package pjplugin.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import pjplugin.Activator;

/**
 * This is st-forward. However, one might find the completion code tricky.
 * Only care to be taken is to include all the Pyjama keywords here.
 * 
 * @author vikassingh
 *
 */
public class DirectiveCompletionProcessor implements IContentAssistProcessor {
	private static final String PROJECT_LOGO_LARGE = "icons/paraicon.png";
	private final char[] ACTIVATION_CHARS = { '#', '(', 'o', 'p', 'f', 'l',
			's', 'n', 'r', 'a', 'd', 'g', 'm', 'c', 'b', 't' };

	private final String[] replacements = { "#omp", "parallel", "for",
			"shared", "private", "firstprivate", "lastprivate", "copyin",
			"copyprivate", "threadprivate", "if", "default", "none",
			"reduction", "schedule", "dynamic", "guided", "runtime",
			"sections", "section", "single", "ordered", "master", "critical",
			"atomic", "flush", "barrier", "nowait", "freeguithread", "gui" };
	
	private final String DEFAULT_BR = "default(";
	private final String REDUCTION_BR = "reduction(";
	private final String SCHEDULE_BR = "schedule(";

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		List<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
		IDocument document = viewer.getDocument();
		String prefix = lastWord(document, offset);
		String indent = lastIndent(document, offset);
		if (prefix.length() == 0) {
			try {
				if (document.getChar(offset - 1) == '#') {
					prefix = "#";
				}else{
					String toChk = DEFAULT_BR;
					String temp = document.get(offset - toChk.length(),
							toChk.length());
					if (temp.endsWith(toChk)) {
						prefix = toChk;
					} else {
						toChk = REDUCTION_BR;
						temp = document.get(offset - toChk.length(),
								toChk.length());
						if (temp.endsWith(toChk)) {
							prefix = toChk;
						} else {
							toChk = SCHEDULE_BR;
							temp = document.get(offset - toChk.length(),
									toChk.length());
							if (temp.endsWith(toChk)) {
								prefix = toChk;
							}
						}
					}
				}					
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		
		}
		ImageDescriptor desc = Activator.getImageDescriptor(PROJECT_LOGO_LARGE);

		// for omp
		if (prefix.endsWith("#")) {
			String[] moreInfo = ContextInfoTexts.getInstance().getOMPInfo();
			String[] completions = ContextInfoTexts.getInstance()
					.getOMPCompletions();
			int index = 0;
			for (String str : completions) {
				org.eclipse.jface.text.contentassist.CompletionProposal one = new org.eclipse.jface.text.contentassist.CompletionProposal(
						str.substring(prefix.length()), offset, 0, str
								.substring(prefix.length()).length(),
						desc.createImage(), str, null, moreInfo[index]);
				++index;
				proposals.add(one);
			}
		} else if(prefix.equals(DEFAULT_BR)){
			String replaceStr = "";
			int cursorPosition = 0;
			String[] infoStr = ContextInfoTexts.getInstance().getDefaultInfo();
			int index = 0;
			for(String temp: ContextInfoTexts.getInstance().getDefaultCompletions()){
				replaceStr = temp;
				cursorPosition = replaceStr.length();
				org.eclipse.jface.text.contentassist.CompletionProposal one = new org.eclipse.jface.text.contentassist.CompletionProposal(
						replaceStr, offset, 0, cursorPosition,
						desc.createImage(), temp, null, infoStr[index]);
				++index;
				proposals.add(one);
			}
		}else if(prefix.equals(REDUCTION_BR)){
			String replaceStr = "";
			int cursorPosition = 0;
			String[] infoStr = ContextInfoTexts.getInstance().getReductionInfo();
			int index = 0;
			for(String temp: ContextInfoTexts.getInstance().getReductionCompletions()){
				replaceStr = temp;
				cursorPosition = replaceStr.length();
				org.eclipse.jface.text.contentassist.CompletionProposal one = new org.eclipse.jface.text.contentassist.CompletionProposal(
						replaceStr, offset, 0, cursorPosition,
						desc.createImage(), temp.substring(0, temp.length()-1), null, infoStr[index]);
				++index;
				proposals.add(one);
			}
		}else if(prefix.equals(SCHEDULE_BR)){
			String replaceStr = "";
			int cursorPosition = 0;
			String[] infoStr = ContextInfoTexts.getInstance().getScheduleInfo();
			int index = 0;
			for(String temp: ContextInfoTexts.getInstance().getScheduleCompletions()){
				replaceStr = temp;
				cursorPosition = replaceStr.length();
				org.eclipse.jface.text.contentassist.CompletionProposal one = new org.eclipse.jface.text.contentassist.CompletionProposal(
						replaceStr, offset, 0, cursorPosition,
						desc.createImage(), temp.substring(0, temp.length()-2), null, infoStr[index]);
				++index;
				proposals.add(one);
			}
		} else {
			for (String str : replacements) {
				if (str.startsWith(prefix)) {
					String replaceStr = str.substring(prefix.length());
					int cursorPosition = replaceStr.length();
					if (str.equals("private") 
							|| str.equals("shared")
							|| str.equals("firstprivate")
							|| str.equals("lastprivate")
							|| str.equals("copyin")
							|| str.equals("copyprivate")
							|| str.equals("threadprivate") 
							|| str.equals("if")
							|| str.equals("reduction")
							|| str.equals("schedule")) {
						replaceStr = replaceStr + "()";
						cursorPosition = replaceStr.length();
						cursorPosition -= 1;

						org.eclipse.jface.text.contentassist.CompletionProposal one = new org.eclipse.jface.text.contentassist.CompletionProposal(
								replaceStr, offset, 0, cursorPosition,
								desc.createImage(), str, null, null);

						proposals.add(one);
					}

					else if (str.equals("default")) {
							replaceStr = replaceStr + "()";
							cursorPosition = replaceStr.length();
							cursorPosition -= 1;

							org.eclipse.jface.text.contentassist.CompletionProposal one = new org.eclipse.jface.text.contentassist.CompletionProposal(
									replaceStr, offset, 0, cursorPosition,
									desc.createImage(), str, null, null);

							proposals.add(one);
					}
				}
			}
		}

		return proposals.toArray(new ICompletionProposal[proposals.size()]);
	}

	private String lastWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (!Character.isJavaIdentifierPart(c))
					return doc.get(n + 1, offset - n - 1);
			}
		} catch (BadLocationException e) {
			// ... log the exception ...
		}
		return "";
	}

	private String lastIndent(IDocument doc, int offset) {
		try {
			int start = offset - 1;
			while (start >= 0 && doc.getChar(start) != '\n')
				start--;
			int end = start;
			while (end < offset && Character.isSpaceChar(doc.getChar(end)))
				end++;
			return doc.get(start + 1, end - start - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return ACTIVATION_CHARS;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return ACTIVATION_CHARS;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

}
