package pjplugin.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class will check for "//#omp"
 * Hopefully, the Pyjama directive syntax is frozen forever,
 * else, this class would need modfications.
 *
 * @author vikassingh
 */
public class PyjamaDirectiveRule implements IRule {

	private static final String pyjamaDirectiveString = "//#omp";

	private IToken fSuccessToken;

	/**
	 * The constructor.
	 * @param successToken the token to return upon successful match.
	 */
	public PyjamaDirectiveRule(IToken successToken) {
		fSuccessToken = successToken;
	}
	/**
	 * Evaluates the rule by examining characters from the given CharacterScanner.
	 * Uses regular expressions to evaluate the character string.
	 * 
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		//  TASK(*) or TASK(n) where n is a Natural number
		int count = 0;
		StringBuffer s = new StringBuffer(5);
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
			if (count == 5)
				break;
		}

		if (s.toString().equals(pyjamaDirectiveString)) {
			int r = scanner.read();
			count++;
			if(Character.isSpaceChar((char)r)){
				return fSuccessToken;
			}
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
