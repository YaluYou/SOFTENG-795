package pjplugin.decorator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.internal.resources.File;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author sriram (sazh998) This class will add the prefix & suffix decoration
 *         for pyjama, ptjava & auto-generated files
 * 
 */
public class FileDecorator extends LabelProvider implements
		ILightweightLabelDecorator {
	public static final String PJ_FILEEXTN = "pj";
	public static final String JAVA_FILEEXTN = "java";
	public static final String PTJAVA_FILEEXTN = "ptjava";
	public static final String OMP_FILEPREFIX = "_omp";
	public static final String PREFIX_DECORATION = "[Donot Edit] ";
	public static final String SUFFIX_DECORATION = " [Auto-generated from ";
	private Map<String, Boolean> pjFileNames = new HashMap<String, Boolean>();

	@Override
	public void decorate(Object arg0, IDecoration arg1) {
		if (arg0 instanceof File) {
			File file = (File) arg0;
			String fileName = file.getName().split("\\.")[0];
			String fileExtn = file.getFileExtension();
			if (fileExtn != null) {
				if (fileExtn.equalsIgnoreCase(PJ_FILEEXTN)) {
					pjFileNames.put(fileName, Boolean.TRUE);
				} else if (fileExtn.equalsIgnoreCase(JAVA_FILEEXTN)) {
					if (pjFileNames.get(fileName) == Boolean.TRUE) {
						// arg1.addPrefix(prefixDeco);
						arg1.addSuffix(SUFFIX_DECORATION + fileName + "."
								+ PJ_FILEEXTN + "]");
					} else if (fileName.startsWith(OMP_FILEPREFIX)) {
						for (String fn : pjFileNames.keySet()) {
							if (fileName.contains(fn)) {
								// arg1.addPrefix(prefixDeco);
								arg1.addSuffix(SUFFIX_DECORATION + fn + "."
										+ PJ_FILEEXTN + "]");
							}
						}
					}
				} else if (fileExtn.equalsIgnoreCase(PTJAVA_FILEEXTN)) {
					if (pjFileNames.get(fileName) == Boolean.TRUE) {
						// arg1.addPrefix(prefixDeco);
						arg1.addSuffix(SUFFIX_DECORATION + fileName + "."
								+ PJ_FILEEXTN + "]");
					}
				}
			}
		}
	}
}
