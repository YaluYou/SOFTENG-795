package pjplugin.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pjplugin.Activator;
import pjplugin.editors.ContextInfoTexts;
import pjplugin.preferences.PreferenceConstants;
import pjplugin.resourcehandler.*;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Singleton for handling the pyjama markers and the translation of Java markers
 * to pyjama markers in the .pj source
 * 
 */
public class MarkerManager {
	public static final String MARKER_ID = Activator.PLUGIN_ID
			+ ".pyjamamarker";
	public static final String RF_MARKER_ID = Activator.PLUGIN_ID + ".rfMarker";
	public static final String RF_MARKER_GUI_ID = Activator.PLUGIN_ID
			+ ".rfMarkerGui";
	private static final int LIBRARY_INIT_ERROR = -1000;
	private static final int GENERATED_CODE_ERROR = -1001;
	private static final int OTHER_ERROR = -1101;
	
	//private List<Integer> parallelRegionNumbers = new ArrayList<Integer>();
	private Map<String,Integer> parallelRegionErrors = new HashMap<String,Integer>();

	public static MarkerManager getInstance() {
		if (fgManager == null)
			fgManager = new MarkerManager();
		return fgManager;
	}

	private static MarkerManager fgManager; // the singleton instance of the
											// MarkerManager

////	private Pattern mPTErrorPattern; // regex pattern for determining line
										// numbers for Java-to-PTJava marker
										// translations
	private Pattern mPyjamaErrorPattern; // regex pattern for determining line
											// numbers for PTJava-to-Pyjama
											// marker translations
	private Pattern mPyjamaRefactorPattern;

	/**
	 * Private constructor
	 */
	private MarkerManager() {

		mPyjamaErrorPattern = Pattern.compile("####\\[(\\d+)\\]####");
		// NOTE: the parentheses are needed to identify matcher groups
	}
	
	public void handleJavaMarker(IMarker marker){
//		System.out.println("---handle Java marker is called");
		IResource resource = marker.getResource();
		String resourceExtension = resource.getFileExtension();
		if (resourceExtension == null || !resourceExtension.equals("java")) {
			return;
		}
        
		System.out.println(resourceExtension);
		int charStart = marker.getAttribute(IMarker.CHAR_START, -1);
		if (charStart < 0) {
			return;
		}
		int charEnd = marker.getAttribute(IMarker.CHAR_END, -1);
		if (charEnd < 0) {
			return;
		}


		// get the pyjama resource that generated this resource
		IResource pyjamaResource = findPyjamaFile(resource);
		if (pyjamaResource == null) {
			return;
		}

		// get the source		
		String source = FileResourceManager.getInstance().loadSource(resource);
//		System.out.println(source);//wk
		if (source.isEmpty()) {
			return;
		}

		// get the pt source
		String pyjamaSource = FileResourceManager.getInstance().loadSource(
				pyjamaResource);
		if (pyjamaSource.isEmpty()) {
			return;
		}
		
		
		int lineNum=0;
		try {
			lineNum = (Integer)marker.getAttribute(IMarker.LINE_NUMBER);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	
		
		int offsetStart= findOffsetOfLine(pyjamaSource,lineNum);
//		System.out.println("offsetStart"+offsetStart);
		if (offsetStart < 0)
				return;
		
		int offsetEnd = findOffsetOfLine(pyjamaSource, lineNum + 1);
//		System.out.println("offsetEnd"+offsetEnd);
		if (offsetEnd < 0)
			offsetEnd = pyjamaSource.length();
		
		String javaerr=source.substring(charStart, charEnd);
		String err=' '+pyjamaSource.substring(offsetStart, offsetEnd)+'\n';
		System.out.println("offsetStart from java"+offsetStart);
		System.out.println("offsetEnd from java"+offsetEnd);
		Pattern errorStatementPattern= Pattern.compile("[^\\w]+(\\Q"+javaerr+"\\E)[^\\w]+");
		Matcher matcher= errorStatementPattern.matcher(err);
		
		while(matcher.find()){
		MarkerInfo m= new MarkerInfo();
		try {
			m.fLineNumber=(Integer) marker.getAttribute(IMarker.LINE_NUMBER);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		
//		System.out.println("charStart from java"+charStart);
//		System.out.println("charEnd from java"+charEnd);
//		System.out.println("mather start(1) = "+matcher.start(1));
//		System.out.println("java error lien "+javaerr);
//		System.out.println("error lien "+err);
//		System.out.println(m.fLineNumber);
		m.fMsg= marker.getAttribute(IMarker.MESSAGE, "Unknown Error");
		m.fCharStart= new Integer(offsetStart + matcher.start(1) - 1);
		//  NOTE: the -1 is for the extra character we added into the sample line
		m.fCharEnd= new Integer(m.fCharStart + javaerr.length());
		m.fLocation= new String("line "+m.fLineNumber);

//		System.out.println(m.fCharStart);
//		System.out.println(m.fCharEnd);
//		System.out.println(m.fLocation);
		
		reportProblem(pyjamaResource, m);
		}
	}
	
	public void handlePjMarker(IMarker marker){
//		System.out.println("handle pyjama marker is called");
		IResource resource = marker.getResource();
		String resourceExtension = resource.getFileExtension();
		if (resourceExtension == null || !resourceExtension.equals("java")) {
			return;
		}
		int charStart = marker.getAttribute(IMarker.CHAR_START, -1);
		if (charStart < 0) {
			return;
		}
		int charEnd = marker.getAttribute(IMarker.CHAR_END, -1);
		if (charEnd < 0) {
			return;
		}

		// get the pyjama resource that generated this resource
		IResource pyjamaResource = findPyjamaFile(resource);
		if (pyjamaResource == null) {
			return;
		}

		// get the source		
		String source = FileResourceManager.getInstance().loadSource(resource);
		if (source.isEmpty()) {
			return;
		}

		// get the pt source
		String pyjamaSource = FileResourceManager.getInstance().loadSource(
				pyjamaResource);
		if (pyjamaSource.isEmpty()) {
			return;
		}
		
		int offsetStart=0;
		try {
			offsetStart= findOffsetOfLine(source,(Integer)marker.getAttribute(IMarker.LINE_NUMBER));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		int offsetEnd=0;
		try {
			offsetEnd= findOffsetOfLine(source,(Integer)marker.getAttribute(IMarker.LINE_NUMBER)+1);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		String javaerr=source.substring(charStart, charEnd);
		String errLine=source.substring(offsetStart, offsetEnd-1);
//		System.out.println(javaerr);
//		System.out.println("error Line = "+errLine);
		
		
		
		Pattern errorStatementPattern= Pattern.compile("[^\\w]+(\\Q"+javaerr+"\\E)[^\\w]+");
		Matcher matcher= errorStatementPattern.matcher(pyjamaSource);
		MarkerInfo m= new MarkerInfo();
		
		Pattern parallelRegionPattern = Pattern.compile("_OMP_ParallelRegion_"+"[0-9]*");
		Matcher regionMatcher = parallelRegionPattern.matcher(source);
		
		if(regionMatcher.find()){
			int temp=0;
			while(regionMatcher.find()){
				if(regionMatcher.start()-charStart<0){
					System.out.println(source.substring(regionMatcher.start(),regionMatcher.start()+21));
//					System.out.println(source.substring(regionMatcher.start()+20,regionMatcher.end()));
					temp=Integer.parseInt(source.substring(regionMatcher.start()+20,regionMatcher.end()));
				}
			}
			parallelRegionErrors.put(javaerr, temp);  
		}
		
		
		while(matcher.find()){
			Pattern pyjamaDirectivePattern= Pattern.compile("//#omp parallel");
			Matcher pymatcher= pyjamaDirectivePattern.matcher(pyjamaSource);
			int parallelRegionNumber = -1;
			while(pymatcher.find()){
//				System.out.println("matcher.start= "+matcher.start()+" pymatcher.start= "+pymatcher.start());
				if((matcher.start()-pymatcher.start())>0){
					m.fMsg= marker.getAttribute(IMarker.MESSAGE, "Unknown Error");
					Pattern errorMsg=Pattern.compile("cannot be resolved to a variable");
					Matcher msgMatcher=errorMsg.matcher(m.fMsg);
					if(msgMatcher.find())
						m.fMsg=m.fMsg+",make it as shared or declare inside parallel region";
					m.fCharStart= new Integer(pymatcher.start());
					//  NOTE: the -1 is for the extra character we added into the sample line
					m.fCharEnd= new Integer(m.fCharStart + javaerr.length());
					m.fLocation= new String("line "+m.fLineNumber);
					parallelRegionNumber++;
				}
			}
			if(parallelRegionErrors.get(javaerr)!=null){
				if(parallelRegionNumber==parallelRegionErrors.get(javaerr)){
					reportProblem(pyjamaResource, m);
//					System.out.println("---pyjama error find!!!"+matcher.start());
				}
			}
		}
	}

//	public void handleJavaMarker(IMarker marker) {
//		System.out.println("inside handle marker");//wk
//		IResource resource = marker.getResource();
//		String resourceExtension = resource.getFileExtension();
//		if (resourceExtension == null || !resourceExtension.equals("java")) {
//			return;
//		}
//
//		int charStart = marker.getAttribute(IMarker.CHAR_START, -1);
//		if (charStart < 0) {
//			return;
//		}
//		int charEnd = marker.getAttribute(IMarker.CHAR_END, -1);
//		if (charEnd < 0) {
//			return;
//		}
//
//		// get the ptjava and pyjama resource and sources below
//
//		// get the ptjava resource that generated this resource
//		IResource pyjamaResource = findPyjamaFile(resource);
//		if (pyjamaResource == null) {
//			return;
//		}
//
//		// get the source		
//		String source = FileResourceManager.getInstance().loadSource(resource);
//		System.out.println(source);//wk
//		if (source.isEmpty()) {
//			return;
//		}
//
//		// get the pt source
//		String pyjamaSource = FileResourceManager.getInstance().loadSource(
//				pyjamaResource);
//		if (pyjamaSource.isEmpty()) {
//			return;
//		}
//
//		// find the line number
//		Matcher lineMatcher = mPyjamaErrorPattern.matcher(source);
//
//		try {
//			if (lineMatcher.find(charStart)) {
//				int lineNum = Integer.parseInt(lineMatcher.group(1));
//				int offsetStart = findOffsetOfLine(pyjamaSource, lineNum);
//				if (offsetStart < 0)
//					return;
//				int offsetEnd = findOffsetOfLine(pyjamaSource, lineNum + 1);
//				if (offsetEnd < 0)
//					offsetEnd = pyjamaSource.length();
//				String line = ' ' + pyjamaSource.substring(offsetStart, offsetEnd) + '\n';
//				String err = source.substring(charStart, charEnd);
//
//				int prevOccurances = 0;
//				try {
//					String srcLine = source.substring(
//							findOffsetOfLine(source, (Integer) marker
//									.getAttribute(IMarker.LINE_NUMBER)),
//							charStart);
//					Pattern srcErrPattern = Pattern.compile("(\\Q" + err
//							+ "\\E)[^\\w]+");
//					Matcher srcErrMatcher = srcErrPattern.matcher(srcLine);
//					while (srcErrMatcher.find()) {
//						prevOccurances++;
//					}
//				} catch (CoreException e) {
//					e.printStackTrace();
//					return;
//				}
//
////				if (err.startsWith("__pt__"))
////					err = err.substring("__pt__".length(), err.length());
//
//				Pattern errorStatementPattern = Pattern.compile("[^\\w]+(\\Q"
//						+ err + "\\E)[^\\w]+");
//				Matcher matcher = errorStatementPattern.matcher(line);
//				int findCount = 0;
//				while (matcher.find()) {
//					if (findCount == prevOccurances) {
//						// fill in data
//						MarkerInfo m = new MarkerInfo();
//						m.fMsg = marker.getAttribute(IMarker.MESSAGE,
//								"Unknown Error");
//						m.fCharStart = new Integer(offsetStart
//								+ matcher.start(1) - 1);
//						// NOTE: the -1 is for the extra character we added into
//						// the sample line
//						m.fCharEnd = new Integer(m.fCharStart + err.length());
//						m.fLocation = new String("line " + lineNum);
//						m.fSeverity = marker.getAttribute(IMarker.SEVERITY,
//								IMarker.SEVERITY_INFO);
////						if (!resourcePTMarkerExists(pyjamaResource, m)
////								&& lineNum > 0) {
//
//							// now find the line number in Pyjama
//
//							// got a line number from java, need to find
////							String[] ptCodeAsListing = ptSource.split("\n");
////							String errLine = ptCodeAsListing[lineNum - 1];
//////
////							if (!errLine.isEmpty()) {
////								 get Pyjama resource that generated this
////								 resource
//
//								if (pyjamaResource == null) {
//									return;
//								}
//
//								// get the pyjama source
////								String pyjamaSource = FileResourceManager
////										.getInstance().loadSource(
////												pyjamaResource);
//								if (pyjamaSource.isEmpty()) {
//									return;
//								}
//
////								mPyjamaErrorPattern = Pattern
////										.compile("####\\[(\\d+)\\]####");
////								Matcher ptLineMatcher = mPyjamaErrorPattern
////										.matcher(errLine);
//								if (lineMatcher.find()) {
//
//									MarkerInfo pyjamaMarker = new MarkerInfo();
//
//									int pyjamlineNum = Integer
//											.parseInt(lineMatcher.group(1));
//
//									/*
//									 * these are the special errors
//									 */
//									if (pyjamlineNum == LIBRARY_INIT_ERROR) {
//										pyjamaMarker.fMsg = "Pyjama Internal Error: Runtime initialisation error.";
//									} else if (pyjamlineNum == GENERATED_CODE_ERROR) {
//										pyjamaMarker.fMsg = "Pyjama Internal Error: Error found in runtime generated code.";
//									} else if (pyjamlineNum == OTHER_ERROR) {
//										pyjamaMarker.fMsg = "Pyjama Internal Error: Unknown error.";
//									} else {
//										pyjamaMarker.fLineNumber = pyjamlineNum;
//										int pyjamoffsetStart = findOffsetOfLine(
//												pyjamaSource, pyjamlineNum);
//										if (pyjamoffsetStart < 0) {
//											return;
//										}
//
//										int pyjamoffsetEnd = findOffsetOfLine(
//												pyjamaSource, pyjamlineNum + 1);
//										if (pyjamoffsetEnd < 0) {
//											pyjamoffsetEnd = pyjamaSource
//													.length();
//										}
//
//										String pyjamaSourceErr = source
//												.substring(charStart, charEnd);
//
//										pyjamaMarker.fMsg = marker
//												.getAttribute(IMarker.MESSAGE,
//														"Unknown Error");
//
//									}
//									pyjamaMarker.fLocation = new String("line "
//											+ pyjamlineNum);
//									pyjamaMarker.fSeverity = marker
//											.getAttribute(IMarker.SEVERITY,
//													IMarker.SEVERITY_INFO);
//
//                                        System.out.println("before call reportProblem");//wk)
//										MarkerManager.getInstance()
//												.reportProblem(pyjamaResource,
//														pyjamaMarker);
//									
//								}
//							   findCount++;
//							}
//							IPreferenceStore prefs = Activator.getDefault()
//									.getPreferenceStore();
//							if (!prefs
//									.getBoolean(PreferenceConstants.PYJAMA_SHOW_HIDDEN_ERRORS)) {
//								try {
//									marker.delete();
//								} catch (CoreException e) {
//									// logError(e);
//								}
//							}
//							break;
//						}
//						
//						
//					}
//					
////				}
////			}
//		} catch (IndexOutOfBoundsException e) {
//			// logError(e);
//		}
//	}

////	public void handlePJFactoring(IMarker marker) {
////
////		IResource resource = marker.getResource();
////		String resourceExtension = resource.getFileExtension();
////		if (resourceExtension.equals("pj")) {
////			// get the source
////			String source = FileResourceManager.getInstance().loadSource(
////					resource);
////			if (source.isEmpty()) {
////				return;
////			}
////
////			String[] sourceAsListing = source.split("\n");
////
////			IPath path = resource.getProjectRelativePath();
////			if (path == null)
////				return;
////
////			IProject project = resource.getProject();
////			IResource pjResource = project.findMember(path);
////			if (pjResource == null)
////				return;
////
////			// find the line number
////			mPyjamaRefactorPattern = Pattern
////					.compile("for\\s*\\p{Punct}\\s*\\w+\\s*");
////			Matcher lineMatcher = mPyjamaRefactorPattern.matcher(source);
////
////			while (lineMatcher.find()) {
////				MarkerInfo pyjamaMarker = new MarkerInfo();
////				int pyjamlineNum = -1;
////				int line = 0;
////				String toSrch = source.substring(lineMatcher.start(),
////						lineMatcher.end());
////				for (String itr : sourceAsListing) {
////					if (-1 != itr.indexOf(toSrch)) {					
////						if (false == isWithinDirective("parallel",
////								sourceAsListing, line)) {
////							pyjamlineNum = line + 1;
////							if (-1 != pyjamlineNum) {
////								pyjamaMarker.fMsg = ContextInfoTexts.getInstance()
////										.getRFForMessage();
////								
////								int offsetStart = findOffsetOfLine(source, pyjamlineNum);
////								if (offsetStart < 0)
////									return;
////
////								int offsetEnd = findOffsetOfLine(source, pyjamlineNum + 1);
////								if (offsetEnd < 0)
////									offsetEnd = source.length();
////
////								pyjamaMarker.fCharStart = offsetStart;
////								pyjamaMarker.fCharEnd = offsetEnd;
////								pyjamaMarker.fLocation = new String("line " + pyjamlineNum);
////								pyjamaMarker.fLineNumber = pyjamlineNum;
////								pyjamaMarker.fSeverity = marker.getAttribute(
////										IMarker.SEVERITY, IMarker.SEVERITY_INFO);
////
////								MarkerManager.getInstance().reportRefactoring(pjResource,
////										pyjamaMarker);
////							}
////						}
////					}
////					++line;
////				}
////			}
////		}
////	}
	
	
	// TODO: 
////	public void handlePJGuiFactoring(IMarker marker) {
////
////		IResource resource = marker.getResource();
////		String resourceExtension = resource.getFileExtension();
////		if (resourceExtension.equals("pj")) {
////			// get the source
////			String source = FileResourceManager.getInstance().loadSource(
////					resource);
////			if (source.isEmpty()) {
////				return;
////			}
////
////			String[] sourceAsListing = source.split("\n");
////
////			IPath path = resource.getProjectRelativePath();
////			if (path == null)
////				return;
////
////			IProject project = resource.getProject();
////			IResource pjResource = project.findMember(path);
////			if (pjResource == null)
////				return;
////
////			// find the line number
////			mPyjamaRefactorPattern = Pattern
////					.compile("actionPerformed\\s*\\p{Punct}");
////			Matcher lineMatcher = mPyjamaRefactorPattern.matcher(source);
////
////			while (lineMatcher.find()) {
////				MarkerInfo pyjamaMarker = new MarkerInfo();
////				int pyjamlineNum = -1;
////				int line = 0;
////				String toSrch = source.substring(lineMatcher.start(),
////						lineMatcher.end());
////				for (String itr : sourceAsListing) {
////					if (-1 != itr.indexOf(toSrch)) {					
////						if (false == isWithinDirective("parallel",
////								sourceAsListing, line)) {
////							pyjamlineNum = line + 1;
////							if (-1 != pyjamlineNum) {
////								pyjamaMarker.fMsg = ContextInfoTexts.getInstance()
////										.getRFForMessage();
////								
////								int offsetStart = findOffsetOfLine(source, pyjamlineNum);
////								if (offsetStart < 0)
////									return;
////
////								int offsetEnd = findOffsetOfLine(source, pyjamlineNum + 1);
////								if (offsetEnd < 0)
////									offsetEnd = source.length();
////
////								pyjamaMarker.fCharStart = offsetStart;
////								pyjamaMarker.fCharEnd = offsetEnd;
////								pyjamaMarker.fLocation = new String("line " + pyjamlineNum);
////								pyjamaMarker.fLineNumber = pyjamlineNum;
////								pyjamaMarker.fSeverity = marker.getAttribute(
////										IMarker.SEVERITY, IMarker.SEVERITY_INFO);
////
////								MarkerManager.getInstance().reportRefactoring(pjResource,
////										pyjamaMarker);
////							}
////						}
////					}
////					++line;
////				}
////			}
////		}
////	}

////	private boolean isWithinDirective(String directive, String[] code,
////			int lineNum) {
////		String temp = "";
////		for(int index = lineNum - 1; index >=0; index--){
////			temp = code[index];
////			temp = temp.trim();
////			if(temp.startsWith("//#omp") && temp.contains(directive)){
////				return true;
////			}else if(temp.equals("{") || temp.equals("")){
////				continue;
////			}else{
////				return false;
////			}
////		}
////		return false;
////		String temp = "";
////		temp = code[lineNum - 1].trim();
////		if(temp.startsWith("//#omp") && temp.contains(directive)){
////			return true;
////		}else
////			return false;
////	}

	/**
	 * Returns the IResource representing the PTJava file used to generate the
	 * given resource. Returns null if the given resource is null, does not
	 * exist, or does not have the file extension "java"
	 * 
	 * @param resource
	 *            The resource that was generated by the desired PTJava file.
	 *            Should be a file with a ".java" extension.
	 * @return The resource corresponding to the PTJava file or null.
	 */
	private IResource findPTFile(IResource resource) {
		if (resource == null || !resource.exists())
			return null;

		IPath path = resource.getProjectRelativePath();
		if (path == null)
			return null;
		// System.out.println("path:"+path);
		StringBuffer sb = new StringBuffer(path.toString());
		sb.delete(sb.length() - 5, sb.length());
		sb.append(".ptjava");
		String s = sb.toString();
		// System.out.println(s);

		IProject project = resource.getProject();
		IResource ptResource = project.findMember(new Path(s));
		if (ptResource == null)
			return null;

		// System.out.println("ptResource: "+ptResource);

		return ptResource;
	}

	/**
	 * Returns the IResource representing the Pyjama file used to generate the
	 * given resource. Returns null if the given resource is null, does not
	 * exist, or does not have the file extension "java"
	 * 
	 * @param resource
	 *            The resource that was generated by the desired pyjama file.
	 *            Should be a file with a ".java" extension.
	 * @return The resource corresponding to the Pyjama file or null.
	 */
	private IResource findPyjamaFile(IResource resource) {
		if (resource == null || !resource.exists())
			return null;

		IPath path = resource.getProjectRelativePath();
		if (path == null)
			return null;
		// System.out.println("path:"+path);
		StringBuffer sb = new StringBuffer(path.toString());
//		System.out.println(sb);
		sb.delete(sb.length() - 5, sb.length());
		sb.append(".pj");
		String s = sb.toString();
		// System.out.println(s);

		IProject project = resource.getProject();
		IResource ptResource = project.findMember(new Path(s));
		if (ptResource == null)
			return null;

		// System.out.println("ptResource: "+ptResource);

		return ptResource;
	}
	/**
	 * find java file
	 * @param resource
	 * @return
	 */
	private IResource findJavaFile(IResource resource) {
		if (resource == null || !resource.exists())
			return null;

		IPath path = resource.getProjectRelativePath();
		if (path == null)
			return null;
		// System.out.println("path:"+path);
		StringBuffer sb = new StringBuffer(path.toString());
		sb.delete(sb.length() - 3, sb.length());
		sb.append(".java");
		String s = sb.toString();
		// System.out.println(s);

		IProject project = resource.getProject();
		IResource ptResource = project.findMember(new Path(s));
		if (ptResource == null)
			return null;

		// System.out.println("ptResource: "+ptResource);

		return ptResource;
	}

	/**
	 * Finds the char offset of a given line.
	 * 
	 * @param source
	 *            The source file
	 * @param line
	 *            The line number
	 * @return The zero relative char offset of the given line, or -1
	 */
	int findOffsetOfLine(String source, int line) {
		if (line < 1)
			return -1;
		int count = 1;
		int index = 0;
		try {
			while (count < line) {
				if (source.charAt(index) == '\n')
					count++;
				index++;
			}
			return index;
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	/**
	 * find the character offset of a given line with column number
	 */
	int findOffsetOfLine(String source,int line,int column){
		if (line < 1)
			return -1;
		int count = 1;
		int columnCount=1;
		int index = 0;
		try {
			while (count < line) {
				if (source.charAt(index) == '\n')
					count++;
				index++;
			}
			
			while(columnCount<column){
				if(source.charAt(index) == '\t'){
					columnCount = columnCount + 4;
					System.out.println(column);
				}
				else{
					columnCount++;
					System.out.println(column);
				}
				index++;
			}
			return index;
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * Creates a Problem Marker for the given resource
	 * 
	 * @param resource
	 *            The resource to create the marker on
	 * @param data
	 *            The data representing the marker
	 */
	public void reportProblem(IResource resource, MarkerInfo data) {
		try {
			System.out.println("reportProblem is called"+ resource+" "+data);//wk
			IMarker marker = resource.createMarker(MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, data.fMsg);
			if (data.fSeverity != null) {
				marker.setAttribute(IMarker.SEVERITY, data.fSeverity);
			} else {
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			}
			if (data.fCharStart != null)
				marker.setAttribute(IMarker.CHAR_START, data.fCharStart);
			if (data.fCharEnd != null)
				marker.setAttribute(IMarker.CHAR_END, data.fCharEnd);
			if (data.fLineNumber != null)
				marker.setAttribute(IMarker.LINE_NUMBER, data.fLineNumber);
			if (data.fLocation != null)
				marker.setAttribute(IMarker.LOCATION, data.fLocation);
//			System.out.println(data.fLineNumber);
			System.out.println("---pyjama error---"+marker.getAttribute(IMarker.LINE_NUMBER));
			System.out.println("---pyjama error---"+marker.getAttribute(IMarker.LOCATION));
			System.out.println("---pyjama error---"+marker.getAttribute(IMarker.CHAR_START));
			System.out.println("---pyjama error---"+marker.getAttribute(IMarker.CHAR_END));
		} catch (CoreException e) {
			// logError(e);
		}
	}

	public void reportRefactoring(IResource resource, MarkerInfo data) {
		try {

			IMarker marker = resource.createMarker(RF_MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, data.fMsg);
			if (data.fSeverity != null) {
				marker.setAttribute(IMarker.SEVERITY, data.fSeverity);
			} else {
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			}
			if (data.fCharStart != null)
				marker.setAttribute(IMarker.CHAR_START, data.fCharStart);
			if (data.fCharEnd != null)
				marker.setAttribute(IMarker.CHAR_END, data.fCharEnd);
			if (data.fLineNumber != null)
				marker.setAttribute(IMarker.LINE_NUMBER, data.fLineNumber);
			if (data.fLocation != null)
				marker.setAttribute(IMarker.LOCATION, data.fLocation);
		} catch (CoreException e) {
			// logError(e);
		}
	}

	/**
	 * Determines whether a PTJava Problem Marker with the same info already
	 * exists on the resource.
	 * 
	 * @param resource
	 *            The resource to search for markers on.
	 * @param data
	 *            The marker data to find a similar math to.
	 * @return <code>true</code> if a similar marker exists on the resource,
	 *         <code>false</code> otherwise.
	 */
	private boolean resourcePTMarkerExists(IResource resource, MarkerInfo data) {
		try {
			IMarker[] markers = resource.findMarkers(MARKER_ID, true,
					IResource.DEPTH_ZERO);
			for (IMarker marker : markers) {
				if ((data.fMsg == null || data.fMsg.equals(marker
						.getAttribute(IMarker.MESSAGE)))
						&& (data.fCharStart == null || data.fCharStart
								.equals(marker.getAttribute(IMarker.CHAR_START)))
						&& (data.fCharEnd == null || data.fCharEnd
								.equals(marker.getAttribute(IMarker.CHAR_END)))
						&& (data.fLineNumber == null || data.fLineNumber
								.equals(marker
										.getAttribute(IMarker.LINE_NUMBER)))
						&& (data.fLocation == null || data.fLocation
								.equals(marker.getAttribute(IMarker.LOCATION)))
						&& (data.fSeverity == null || data.fSeverity
								.equals(marker.getAttribute(IMarker.SEVERITY)))) {
					return true;
				}
			}
			return false;
		} catch (CoreException e) {
			// logError(e);
		}
		return true;
	}

	// --------------------------------------------------------------------------
	// MARKER METHODS

	/**
	 * Removes markers from the given resource.
	 * 
	 * @param resource
	 *            The resource to remove the markers from.
	 * @return <code>true</code> if markers removed successfully,
	 *         <code>false</code> otherwise.
	 */
	public boolean deletePYJAMAMarkers(IResource resource) {
		try {
			System.out.println("inside deletePyjamaMarkers");//wk
			resource.deleteMarkers(MARKER_ID, false, IResource.DEPTH_INFINITE);
			resource.deleteMarkers(RF_MARKER_ID, false, IResource.DEPTH_INFINITE);
			return true;
		} catch (CoreException e) {
			// logError(e);
			return false;
		}
	}
	

}



