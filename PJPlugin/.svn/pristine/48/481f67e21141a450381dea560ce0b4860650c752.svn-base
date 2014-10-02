package pjplugin.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.ui.text.AbstractJavaScanner;
import org.eclipse.jdt.internal.ui.text.CombinedWordRule;
import org.eclipse.jdt.internal.ui.text.JavaWordDetector;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.activities.ws.WorkbenchTriggerPoints;

import pjplugin.Activator;
import pjplugin.preferences.PreferenceConstants;

/**
 * As said, single line comments are important to us.
 * Here, the keywords in the directives are marked differently,
 * instead of all in single color.
 * 
 * @author vikassingh
 *
 */
public class SingleLineCommentScanner extends AbstractJavaScanner{

	private static Color DIRECTIVE_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
	
	private static Color COMMENT_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	
	private static Color KEYWORD_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_RED);

	private static IToken pyjamaDirectiveToken;
	private static IToken pyjamaKeywordToken;
	private static IToken commentToken = new Token(new TextAttribute(COMMENT_COLOR));
	
	private static final String pyjamaDirectiveString = "//#omp";
	
	/**
	 * Reflective of grammar files from compiler code
	 * Originally, OpenMP 2.5 directive keywords
	 */
	private static ArrayList<String> mPyjamaKeywordsLen3 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen4 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen5 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen6 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen7 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen8 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen9 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen11 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen12 = new ArrayList<String>();
	private static ArrayList<String> mPyjamaKeywordsLen13 = new ArrayList<String>();
	static{
		mPyjamaKeywordsLen3.add("gui");
		mPyjamaKeywordsLen3.add("for");
		
		mPyjamaKeywordsLen4.add("none");
		
		mPyjamaKeywordsLen5.add("async");
		mPyjamaKeywordsLen5.add("flush");
		
		mPyjamaKeywordsLen6.add("shared");
		mPyjamaKeywordsLen6.add("guided");
		mPyjamaKeywordsLen6.add("single");
		mPyjamaKeywordsLen6.add("master");
		mPyjamaKeywordsLen6.add("atomic");
		mPyjamaKeywordsLen6.add("nowait");
		
		mPyjamaKeywordsLen7.add("dynamic");
		mPyjamaKeywordsLen7.add("runtime");
		mPyjamaKeywordsLen7.add("ordered");
		mPyjamaKeywordsLen7.add("section");
		mPyjamaKeywordsLen7.add("barrier");
		
		mPyjamaKeywordsLen8.add("parallel");
		mPyjamaKeywordsLen8.add("schedule");
		mPyjamaKeywordsLen8.add("sections");
		mPyjamaKeywordsLen8.add("critical");
		
		mPyjamaKeywordsLen9.add("reduction");
		
		mPyjamaKeywordsLen11.add("lastprivate");
		
		mPyjamaKeywordsLen12.add("firstprivate");
		
		mPyjamaKeywordsLen13.add("threadprivate");
		mPyjamaKeywordsLen13.add("freeguithread");
	}
	

	private static final class DirectiveClauses extends WordRule{

		public DirectiveClauses(IWordDetector detector) {
			super(detector);

		}
	}
	
	@SuppressWarnings("restriction")
	public SingleLineCommentScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		initialize();
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
        Color color = new Color(Display.getDefault(), PreferenceConverter.getDefaultColor(prefs, PreferenceConstants.PYJAMA_HIGHLIGHT_DIRECTIVES_COLOR));
        formTokens();
	}
	
	private void formTokens(){
        // TODO: use color
        pyjamaDirectiveToken = new Token(new TextAttribute(DIRECTIVE_COLOR));
        pyjamaKeywordToken = new Token(new TextAttribute(KEYWORD_COLOR));
	}

	private static String[] fgTokenProperties= {
		IJavaColorConstants.JAVA_SINGLE_LINE_COMMENT,
	};
	
	private static final class PyjamaDirectiveRule implements IRule{

		@Override
		public IToken evaluate(ICharacterScanner scanner) {
			int count = 0 ;
			String temp ;
			StringBuffer s = new StringBuffer(6) ; 
			while (true) {
				//  read in a char
				int r = scanner.read();
				count++;

				//  test for end/invalid
				if (r == -1)
					break;

				//  append to string buffer
				s.append((char) r);

				//  have we read enough to test?
				if (count == 3)
					break;
			}
			
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen3.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 4)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen4.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}
			
			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 5)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen5.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 6)
					break;
			}

			if (s.toString().equals(pyjamaDirectiveString)) {
				int r = scanner.read();
				count++;
				if(Character.isSpaceChar((char)r)){
					return pyjamaDirectiveToken;
				}
			}
			
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen6.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 7)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen7.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}
			
			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 8)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen8.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 9)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen9.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 11)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen11.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 12)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen12.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			while (true) {
				int r = scanner.read();
				count++;
				if (r == -1)
					break;
				s.append((char) r);
				if (count == 13)
					break;
			}
			temp = s.toString().trim();
			if(mPyjamaKeywordsLen13.contains(temp)){
				count++;
				return pyjamaKeywordToken;
			}

			//  rule does not match
			//  reset scanner
			for (int i = 0; i < count; i++)
				scanner.unread();

			//  return undefined token
			//  so other rules can try
			return Token.UNDEFINED;
		}
	}	

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		
		rules.add(new PyjamaDirectiveRule());
		
//		Token token = getToken(IJavaColorConstants.JAVA_KEYWORD);
//		WordRule wordRule= new WordRule(new JavaWordDetector(), token);
//		for (int i= 0; i < mPyjamaTokens.length; i++)
//		wordRule.addWord(mPyjamaTokens[i], token);
//		rules.add(wordRule);
		
		rules.add(new EndOfLineRule("//", commentToken));
		setDefaultReturnToken(commentToken);
		return rules;
	}

	@Override
	protected String[] getTokenProperties() {
		// TODO Auto-generated method stub
		return fgTokenProperties;
	}

}
