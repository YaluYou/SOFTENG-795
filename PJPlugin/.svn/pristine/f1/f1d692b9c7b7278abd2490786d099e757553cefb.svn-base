//package pjplugin.builder;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//
//
//
//
//
//
//
//
//
////import nz.ac.auckland.PYJAMA.//PYJAMALog;
////import nz.ac.auckland.PYJAMA.Activator;
////import nz.ac.auckland.PYJAMA.builder.MarkerInfo;
////import nz.ac.auckland.PYJAMA.builder.MarkerManager;
////import nz.ac.auckland.PYJAMA.builder.PYJAMAFileBuilder.StringOutputStream;
////import nz.ac.auckland.PYJAMA.internal.builder.BuildDeltaVisitor;
////import nz.ac.auckland.PYJAMA.internal.builder.BuildVisitor;
////import nz.ac.auckland.PYJAMA.internal.builder.StreamGobbler;
//import pjplugin.Activator;
//import pjplugin.constants.PJConstants;
//import pjplugin.preferences.PreferenceConstants;
//
//import org.eclipse.core.resources.ICommand;
//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.resources.IMarker;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IProjectDescription;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.resources.IResourceDelta;
//import org.eclipse.core.resources.IResourceDeltaVisitor;
//import org.eclipse.core.resources.IResourceVisitor;
//import org.eclipse.core.resources.IncrementalProjectBuilder;
//import org.eclipse.core.runtime.Assert;
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.core.runtime.IAdaptable;
//import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.core.runtime.Status;
//import org.eclipse.core.runtime.jobs.Job;
//import org.eclipse.jdt.core.JavaCore;
//import org.eclipse.jface.preference.IPreferenceStore;
//import org.eclipse.jface.preference.PreferencePage;
//import org.eclipse.jface.util.IPropertyChangeListener;
//import org.eclipse.jface.util.PropertyChangeEvent;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PlatformUI;
//import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;
//import org.xml.sax.helpers.DefaultHandler;
//
///**
// * Two way indirection is tricky. We need to get the error lines from
// * Java, to PTJava, and then to Pyjama.
// * 
// * @author vikassingh
// *
// */
///**
// * @author sriram
// * 
// */
//public class FileBuilder extends IncrementalProjectBuilder {
//
//	private static final String PYJAMA_FILE_EXTENSION = ".pj";
//
//	/**
//	 * If not running in debug mode these files are deleted
//	 */
////	private static final String TODELETE_FILE_EXTENSION = ".ptjava";//not longer required for one phase
//
//	/**
//	 * where the PYJAMA compiler jar file is
//	 */
//	private static String fCompilerPath;
//
//	/**
//	 * whether PYJAMA files should be built or not
//	 */
//	private static boolean fCanBuild;
//
//	/**
//	 * whether PYJAMA files should be compiled using a custom compiler or the
//	 * default one
//	 */
//	private static boolean fUseCustomCompiler;
//	
//	private static boolean hasJavaError=false;//wk
//
//	/**
//	 * specifies the type of project created
//	 */
//	public static ProjectType projectType = ProjectType.DESKTOP;
//
//	public static String JAVA_CMD_OPTION = "java -jar ";
//
//	public static String ANDROID_CMD_OPTION = "A";
//	
//	public static final String CLASSPATH = "classpath";
//	
//	public static final String PATH = "path";
//
//	/**
//	 * Listener for when options are changed in project preferences
//	 */
//	private final IPropertyChangeListener fPropertyChangeListener = new IPropertyChangeListener() {
//		public void propertyChange(PropertyChangeEvent event) {
//			// check if event change is for the compiler path
//			System.out.println("prefernces changed");//wk
//			if (event.getProperty().equals(
//					PreferenceConstants.PYJAMA_COMPILER_PATH)) {
//				// change compiler path
//				IPreferenceStore prefs = Activator.getDefault()
//						.getPreferenceStore();
//				fCompilerPath = prefs
//						.getString(PreferenceConstants.PYJAMA_COMPILER_PATH);
//				// System.out.println("file");
//			}
//			if (event.getProperty().equals(
//					PreferenceConstants.PYJAMA_ASSOCIATE_NATURE)) {
//				// add or remove builder from project
//				IPreferenceStore prefs = Activator.getDefault()
//						.getPreferenceStore();
//				fCanBuild = prefs
//						.getBoolean(PreferenceConstants.PYJAMA_ASSOCIATE_NATURE);
//			}
//			if (event.getProperty().equals(
//					PreferenceConstants.PYJAMA_USE_CUSTOM_COMPILER)) {
//				IPreferenceStore prefs = Activator.getDefault()
//						.getPreferenceStore();
//				fUseCustomCompiler = prefs
//						.getBoolean(PreferenceConstants.PYJAMA_USE_CUSTOM_COMPILER);
//			}
//		}
//	};
//
//	/**
//	 * The Constructor. Initializes builder with preferences. Adds a property
//	 * changed listener for updating when preferences change.
//	 */
//	public FileBuilder() {
//		super();
//        
//		System.out.println("intializing builder with preferences");//wk
//		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
//
//		// set the compiler path
//		fCompilerPath = prefs
//				.getString(PreferenceConstants.PYJAMA_COMPILER_PATH);
//
//		// set whether we should build
//		fCanBuild = prefs
//				.getBoolean(PreferenceConstants.PYJAMA_ASSOCIATE_NATURE);
//
//		// set whether we should use a custom compiler
//		fUseCustomCompiler = prefs
//				.getBoolean(PreferenceConstants.PYJAMA_USE_CUSTOM_COMPILER);
//
//		// add a property change listener when preferences change
//		prefs.addPropertyChangeListener(fPropertyChangeListener);
//	}
//
//	/**
//	 * Destructor equivalent. Current implementation removes the class' property
//	 * change listener from the PYJAMA Preference page.
//	 */
//	public void dispose() {
//		// remove the property change listener before class dies
//		Activator.getDefault().getPreferenceStore()
//				.removePropertyChangeListener(fPropertyChangeListener);
//	}
//
//	/**
//	 * Builds the resources in a project. Invokes a {@link BuildVisitor} for
//	 * full builds, or a ({@link BuildDeltaVisitor} for incremental and auto
//	 * builds.
//	 * 
//	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int,
//	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
//	 */
//	@Override
//	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
//			throws CoreException {
//
//		// check if user wants PYJAMA builds
//		if (!fCanBuild)
//			return null;
//
//		// determine what type of build was requested
//		if (kind == FULL_BUILD) {
//			// rebuild all .PYJAMA files in project
//			System.out.println("called full build");//wk
//			fullBuild(monitor);
//		} else {
//			System.out.println("else situation, not full build");//wk
//			IResourceDelta delta = getDelta(getProject());
//			if (delta == null) {
//				// delta returns null if builder has not been invoked before,
//				// or if builder has not been invoked for a very long time
//				System.out.println("delta is null,call full build");//wk
//				fullBuild(monitor);
//			} else {
//				// changes detected, build only changed .PYJAMA resources
//				System.out.println("delta is not null,call incremental build");//wk
//				incrementalBuild(delta, monitor);
//			}
//		}
//
//		new Job("Project Build") {
//			protected IStatus run(IProgressMonitor monitor) {
//
//				try {
//					System.out.println("inside run method");//wk
//					// refresh project
//					getProject().refreshLocal(IProject.DEPTH_INFINITE, monitor);
////					File javaFile=getProject().getLocation().toFile();
////					BufferedReader bfr=new BufferedReader(new FileReader(javaFile));
////					StringBuffer sb=new StringBuffer(javaFile.toString());
////					sb.delete(sb.length() - 3, sb.length());
////					sb.append(".java");
////					BufferedWriter bfw=new BufferedWriter(new FileWriter(new File(sb.toString())));
////					String buffer=null;
////					while((buffer=bfr.readLine())!=null){
////						bfw.write(buffer);
////						bfw.newLine();
////					}
////					bfr.close();
////					bfw.close();
//					// invoke a full build
//					getProject().build(
//							IncrementalProjectBuilder.INCREMENTAL_BUILD,
//							JavaCore.BUILDER_ID, null, monitor);
//
//					// Getting Markers for test purposes
//					IMarker[] markers = getProject().findMarkers(
//							IMarker.PROBLEM, true, IProject.DEPTH_INFINITE);
////					System.out.println("marker:"+markers.toString());
//					for (IMarker i : markers) {
//						if(i.getType()==IMarker.PROBLEM){
//							hasJavaError=true;
//							System.out.println(i);
//						}
//					//	MarkerManager.getInstance().handleJavaMarker(i);
//					}
////					if(markers!=null){ 
////						hasJavaError=true;
////					}
//					// TODO: recode this part, refactoring is interesting
//					// IMarker[] markerText=
//					// getProject().findMarkers(IMarker.TEXT, true,
//					// IProject.DEPTH_INFINITE);
//					// for (IMarker i : markerText) {
//					// MarkerManager.getInstance().handlePJFactoring(i);
//					// }
//				} catch (CoreException exception) {
//					// log.logError(exception);
//				} 
////				catch (FileNotFoundException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//				return Status.OK_STATUS;
//			}
//		}.schedule();
//		return null;
//	}
//
//	/**
//	 * Current implementation removes all PYJAMA Problem markers from the
//	 * project.
//	 * 
//	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#clean(org.eclipse.core.runtime.IProgressMonitor)
//	 */
//	@Override
//	protected void clean(IProgressMonitor monitor) {
//		System.out.println("clean called");//wk
//		MarkerManager.getInstance().deletePYJAMAMarkers(getProject());
//	}
//
//	private void fullBuild(IProgressMonitor monitor) {
//		if (!MarkerManager.getInstance().deletePYJAMAMarkers(getProject()))
//			return;
//
//		try {
//			System.out.println("inside fullBuild");//wk
//			// create a BuildVisitor to visit resource and resources children.
//			// since this is called on the project, we visit every resource in
//			// the workspace
//			getProject().accept(new BuildVisitor());
//
//			// getProject().refreshLocal(IProject.DEPTH_INFINITE, monitor);
//		} catch (CoreException e) {
//		}
//	}
//
//	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
//		try {
//			// delta is the root of the tree representing changes in the project
//			// we will traverse the tree to evaluate all changes
//			delta.accept(new BuildDeltaVisitor());
//
//			// getProject().refreshLocal(IProject.DEPTH_INFINITE, monitor);
//		} catch (CoreException e) {
//		}
//	}
//
//	// --------------------------------------------------------------------------
//	// COMPILER RELATED METHODS
//	/**
//	 * Stream that reads in bytes and stores them. Used when invoking the
//	 * default compiler
//	 */
//	private static class StringOutputStream extends OutputStream {
//		private StringBuffer fBuffer;
//
//		public StringOutputStream(StringBuffer buf) {
//			super();
//			System.out.println("StringOutputStream is used");//wk
//			Assert.isNotNull(buf);
//			fBuffer = buf;
//		}
//
//		@Override
//		public void write(int b) throws IOException {
//			fBuffer.append((char) b);
//		}
//	}
//
//	public static void invokeCompiler(final String toCompile,
//			final IResource resource) throws Exception {
//		new ErrorDetector().detect(resource);
//		
////		MarkerManager.getInstance().
//		IMarker[] markers = resource.findMarkers(
//				IMarker.MARKER, true, IProject.DEPTH_INFINITE);
//		for (IMarker i : markers) {
//			if(i.getType()==IMarker.MARKER){
//				hasJavaError=true;
//				System.out.println("---"+i);
//			}
//		}
////		System.out.println("marker"+markers);
//		
//		System.out.println("resource"+resource);
////		if(hasJavaError){
////			try {
////				new ErrorDetector().detect(resource);
////				return;
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////		}
////		File javaFile=resource.getLocation().toFile();
////		BufferedReader bfr=new BufferedReader(new FileReader(javaFile));
////		StringBuffer sb1=new StringBuffer(javaFile.toString());
////		sb1.delete(sb1.length() - 3, sb1.length());
////		sb1.append(".java");
////		BufferedWriter bfw=new BufferedWriter(new FileWriter(new File(sb1.toString())));
////		String buffer=null;
////		while((buffer=bfr.readLine())!=null){
////			bfw.write(buffer);
////			bfw.newLine();
////		}
////		bfr.close();
////		bfw.close();
//		System.out.println("inside involeCompiler"+resource);//wk
//		final File projectLocation = resource.getProject().getLocation()
//				.toFile();
//		System.out.println(projectLocation);//wk
//		// check if User wanted to use their own custom compiler
//		if (fUseCustomCompiler) {
//			if (fCompilerPath.isEmpty()) {
//				return;
//			}
//
//			// Dynamically form the run command
//			String cmd = getProjectTypeCommand(toCompile, projectLocation);
//			System.out.println(cmd);
//
//			Runtime rt = Runtime.getRuntime();
//			try {
//				// execute command
//				Process proc = rt.exec(cmd);
//
//				// establish objects to read output of executed program
//				StreamGobbler errorGobbler = new StreamGobbler(
//						proc.getErrorStream(), "ERROR");
//				StreamGobbler outputGobbler = new StreamGobbler(
//						proc.getInputStream(), "OUTPUT");
//
//				errorGobbler.start();
//				outputGobbler.start();
//
//				// block until compiler finished
//				proc.waitFor();
//
//				errorGobbler.join();
//				outputGobbler.join();
//
//				// System.out.println("errorGobbler.getMsg(): " +
//				// errorGobbler.getMsg());
//
//				if (!errorGobbler.getMsg().isEmpty()) {
//					// error stream was not empty .: some error occured
//					// the error message is stored in errorGobbler.getMsg()
//					// NOTE: the error message is truncated from what the
//					// compiler
//					// puts out, because there's a lot of junk that is
//					// irrelevant
//					System.out.println("error stream is not empty");//wk
//					String s = errorGobbler.getMsg();
//					MarkerInfo m = createMarkerFromErrorString(s);
//					MarkerManager.getInstance().reportProblem(resource, m);
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} else {
//			// User chose to use the default compiler
//
//			// for invoking from the same JVM using a different thread
//			// This way requires PTCompiler.jar to be specified on the class
//			// path in
//			// the manifest file, and means the user does not ever specify the
//			// jar file
//
//			Runnable r = new Runnable() {
//				@Override
//				public void run() {
//					// step 1
//
//					// Dynamically form the run command
//					String cmd = getProjectTypeCommand(toCompile,
//							projectLocation);
//					System.out.println(cmd);
//
//					Runtime rt = Runtime.getRuntime();
//					try {
//						// execute command
//						Process proc = rt.exec(cmd);
//
//						// establish objects to read output of executed program
//						StreamGobbler errorGobbler = new StreamGobbler(
//								proc.getErrorStream(), "ERROR");
//						StreamGobbler outputGobbler = new StreamGobbler(
//								proc.getInputStream(), "OUTPUT");
//
//						errorGobbler.start();
//						outputGobbler.start();
//
//						// block until compiler finished
//						proc.waitFor();
//
//						errorGobbler.join();
//						outputGobbler.join();
//
//						// System.out.println("errorGobbler.getMsg(): " +
//						// errorGobbler.getMsg());
//
//						if (!errorGobbler.getMsg().isEmpty()) {
//							// error stream was not empty .: some error occured
//							// the error message is stored in
//							// errorGobbler.getMsg()
//							// NOTE: the error message is truncated from what
//							// the compiler
//							// puts out, because there's a lot of junk that is
//							// irrelevant
//							String s = errorGobbler.getMsg();
//							MarkerInfo m = createMarkerFromErrorString(s);
//							MarkerManager.getInstance().reportProblem(resource,
//									m);
//						}
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					} finally {
//						// step 2 =========================================
//
//						// if not running in debug mode, delete the ptjava files
////						IPreferenceStore prefs = Activator.getDefault()
////								.getPreferenceStore();
////						if (false == prefs
////								.getBoolean(PreferenceConstants.PYJAMA_DEBUG_MODE)) {
////							if (true == toCompile
////									.endsWith(PYJAMA_FILE_EXTENSION)) {
////								String toDelete = toCompile.substring(
////										0,
////										toCompile.length()
////												- TODELETE_FILE_EXTENSION
////														.length());
////								toDelete += TODELETE_FILE_EXTENSION;
////								File toDeleteFile = new File(toDelete);
////								if (true == toDeleteFile.exists()) {
////									toDeleteFile.delete();
////								}
////							}
////						}
//					}
//				}
//			};
//
//			Thread t = new Thread(r);
//
//			StringBuffer sb = new StringBuffer(4096);
//			PrintStream printStream = new PrintStream(
//					new StringOutputStream(sb));
//			try {
//				// redirecting error stream while compiler runs
//				System.setErr(printStream);
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			}
//
//			t.run();
//			try {
//				t.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			String errString = sb.toString();
//
//			try {
//				// return error stream
//				System.setErr(System.err);
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			}
//			if (!errString.isEmpty() /*
//									 * && errString.startsWith(
//									 * "********* Failed to parse")
//									 */) {
//				// parse the error string
//
//				MarkerInfo m = createMarkerFromErrorString(errString);
//				MarkerManager.getInstance().reportProblem(resource, m);
//			}
//		}
//	}
//
//	private static MarkerInfo createMarkerFromErrorString(String errorString) {
//		System.out.println("inside createMarkerFromErrorString one");//wk
//		Pattern pattern = Pattern
//				.compile(".*(jump\\.parser\\.(.*) at (line (\\d+)), column (\\d)+\\.).*");
//		Matcher matcher = pattern.matcher(errorString);
//
//		MarkerInfo m = new MarkerInfo();
//		if (matcher.find()) {
//			// m.fMsg= matcher.group();
//
//			m.fMsg = errorString.substring(0,
//					errorString.indexOf("at jump.parser."));
//
//			m.fLocation = matcher.group(1);
//			m.fLineNumber = Integer.parseInt(matcher.group(4));
//		} else {
//			m.fMsg = errorString;
//		}
//		return m;
//	}
//
//	private static MarkerInfo createMarkerFromCompileErrorString(
//			String errorString) {
//		System.out.println("inside createMarkerFromCompileErrorString two");//wk
//		Pattern pattern = Pattern.compile("####\\[(\\d+)\\]####");
//		Matcher matcher = pattern.matcher(errorString);
//
//		MarkerInfo m = new MarkerInfo();
//		if (matcher.find()) {
//			// m.fMsg= matcher.group();
//
//			m.fMsg = errorString.substring(0,
//					errorString.indexOf("at jump.parser."));
//
//			m.fLocation = matcher.group(1);
//			m.fLineNumber = Integer.parseInt(matcher.group(4));
//		} else {
//			m.fMsg = errorString;
//		}
//		return m;
//	}
//
//	// --------------------------------------------------------------------------
//	// UTILITY METHODS
//
//	/**
//	 * The ID for the builder.
//	 */
//	public static final String BUILDER_ID = Activator.PLUGIN_ID
//			+ ".pyjamaFileBuilder";
//
//	/**
//	 * Associates the Pyjama builder with the given project.
//	 * 
//	 * @param project
//	 *            The project to add the builder to
//	 */
//	public static void addBuilderToProject(IProject project) {
//		// cannot modify closed projects
//		if (!project.isOpen())
//			return;
//
//		// get the description
//		System.out.println("inside addBuilderToProject");//wk
//		IProjectDescription description;
//		try {
//			description = project.getDescription();
//		} catch (CoreException exception) {
//			// //PYJAMALog.logError(exception);
//			return;
//		}
//
//		ICommand[] cmds = description.getBuildSpec();
//		for (int j = 0; j < cmds.length; j++) {
//			if (cmds[j].getBuilderName().equals(BUILDER_ID))
//				return;
//		}
//
//		// Associate builder with project.
//		ICommand newCmd = description.newCommand();
//		newCmd.setBuilderName(BUILDER_ID);
//		List<ICommand> newCmds = new ArrayList<ICommand>();
//		newCmds.addAll(Arrays.asList(cmds));
//		newCmds.add(newCmd);
//		description.setBuildSpec((ICommand[]) newCmds
//				.toArray(new ICommand[newCmds.size()]));
//
//		// set the project description
//		try {
//			project.setDescription(description, null);
//		} catch (CoreException e) {
//			// PYJAMALog.logError(e);
//		}
//	}
//
//	/**
//	 * Removes the builder from the given project. Does nothing if the project
//	 * is not open or does not have the builder associated with it.
//	 * 
//	 * @param project
//	 *            The project to remove the builder from
//	 */
//	public static void removeBuilderFromProject(IProject project) {
//
//		// Cannot modify closed projects.
//		if (!project.isOpen())
//			return;
//		System.out.println("inside removeBuilderFromProject");//wk
//		// Get the description.
//		IProjectDescription description;
//		try {
//			description = project.getDescription();
//		} catch (CoreException e) {
//			// PYJAMALog.logError(e);
//			return;
//		}
//
//		// Look for builder.
//		int index = -1;
//		ICommand[] cmds = description.getBuildSpec();
//		for (int j = 0; j < cmds.length; j++) {
//			if (cmds[j].getBuilderName().equals(BUILDER_ID)) {
//				index = j;
//				break;
//			}
//		}
//		if (index == -1)
//			return;
//
//		// Remove builder from project.
//		List<ICommand> newCmds = new ArrayList<ICommand>();
//		newCmds.addAll(Arrays.asList(cmds));
//		newCmds.remove(index);
//		description.setBuildSpec((ICommand[]) newCmds
//				.toArray(new ICommand[newCmds.size()]));
//		try {
//			project.setDescription(description, null);
//		} catch (CoreException e) {
//			// PYJAMALog.logError(e);
//		}
//	}
//
//	private static String getProjectTypeCommand(String _toCompile,
//			File _projectLocation) {
//		System.out.println("inside getProjectTypeCommand");//wk
//		String command = null;
//
//		String jarPath = pjplugin.preferences.PreferencePage
//				.getDefaultRuntimeJarPath().toString();
//
//		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
//				.newInstance();
//		DocumentBuilder documentBuilder;
//		try {
//			documentBuilder = documentBuilderFactory.newDocumentBuilder();
//
//			FileInputStream classpathStream = new FileInputStream(
//					_projectLocation.toString()
//							.concat(PJConstants.DEFAULT_PATH + ".")
//							.concat(CLASSPATH));
//			Document classpathDoc = documentBuilder.parse(classpathStream);
//			NodeList list = classpathDoc.getElementsByTagName(CLASSPATH);
//			int len = list.getLength();
//			l1: for (int i = 0; i < len; i++) {
//				NodeList list1 = list.item(i).getChildNodes();
//				int len1 = list1.getLength();
//				if (len1 > 0) {
//					for (int j = 0; j < len1; j++) {
//						NamedNodeMap nodeMap = list1.item(j).getAttributes();
//						if (nodeMap != null) {
//							int len2 = nodeMap.getLength();
//							for (int k = 0; k < len2; k++) {
//								String nodeName = nodeMap.item(k).getNodeName();
//								String nodeValue = nodeMap.item(k)
//										.getNodeValue();
//
//								if (nodeName != null
//										&& PATH.equalsIgnoreCase(nodeName)
//										&& nodeValue != null) {
//									if (nodeValue.matches("(.*)pyjama(.*)jar")) {
//										jarPath = nodeValue;
//										break l1;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (jarPath == null || PJConstants.DEFAULT_PATH.equals(jarPath)) {
//			if (_projectLocation != null) {
//				String libFolderPath = _projectLocation.getAbsolutePath()
//						+ File.separator + PJConstants.LIBS_DIRECTORY
//						+ File.separator + PJConstants.PJ_JARNAME;
//				File pjJarFile = new File(libFolderPath);
//				if (pjJarFile.exists()) {
//					jarPath = libFolderPath;
//					projectType = ProjectType.ANDROID;
//				} else {
//					libFolderPath = _projectLocation.getAbsolutePath()
//							+ File.separator + PJConstants.LIB_DIRECTORY
//							+ File.separator + PJConstants.PJ_JARNAME;
//					pjJarFile = new File(libFolderPath);
//					if (pjJarFile.exists()) {
//						jarPath = libFolderPath;
//						projectType = ProjectType.DESKTOP;
//					}
//				}
//			}
//		}
//
//		if (jarPath != null) {
//			command = (JAVA_CMD_OPTION).concat(jarPath).concat(" ")
//					.concat(_toCompile);
//			if (projectType == ProjectType.ANDROID)
//				command = command.concat(" ").concat(ANDROID_CMD_OPTION);
//		}
//
//		return command;
//	}
//}

//----------------------------------------MyFileBuilder-----------------------------------------
package pjplugin.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





















import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;














//import nz.ac.auckland.PYJAMA.//PYJAMALog;
//import nz.ac.auckland.PYJAMA.Activator;
//import nz.ac.auckland.PYJAMA.builder.MarkerInfo;
//import nz.ac.auckland.PYJAMA.builder.MarkerManager;
//import nz.ac.auckland.PYJAMA.builder.PYJAMAFileBuilder.StringOutputStream;
//import nz.ac.auckland.PYJAMA.internal.builder.BuildDeltaVisitor;
//import nz.ac.auckland.PYJAMA.internal.builder.BuildVisitor;
//import nz.ac.auckland.PYJAMA.internal.builder.StreamGobbler;
import pjplugin.Activator;
import pjplugin.constants.PJConstants;
import pjplugin.preferences.PreferenceConstants;
import pjplugin.resourcehandler.FileResourceManager;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Two way indirection is tricky. We need to get the error lines from
 * Java, to PTJava, and then to Pyjama.
 * 
 * @author vikassingh
 *
 */
/**
 * @author sriram
 * 
 */
public class FileBuilder extends IncrementalProjectBuilder {

	private static final String PYJAMA_FILE_EXTENSION = ".pj";

	/**
	 * If not running in debug mode these files are deleted
	 */
//	private static final String TODELETE_FILE_EXTENSION = ".ptjava";//not longer required for one phase

	/**
	 * where the PYJAMA compiler jar file is
	 */
	private static String fCompilerPath;

	/**
	 * whether PYJAMA files should be built or not
	 */
	private static boolean fCanBuild;

	/**
	 * whether PYJAMA files should be compiled using a custom compiler or the
	 * default one
	 */
	private static boolean fUseCustomCompiler;
	
	private static boolean hasJavaError=false;//wk
	
	private static boolean hasJavaMarker=false;//wk
	
	/**
	 * specifies the type of project created
	 */
	public static ProjectType projectType = ProjectType.DESKTOP;

	public static String JAVA_CMD_OPTION = "java -jar ";

	public static String ANDROID_CMD_OPTION = "A";
	
	public static final String CLASSPATH = "classpath";
	
	public static final String PATH = "path";

	/**
	 * Listener for when options are changed in project preferences
	 */
	private final IPropertyChangeListener fPropertyChangeListener = new IPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent event) {
			// check if event change is for the compiler path
			System.out.println("prefernces changed");//wk
			if (event.getProperty().equals(
					PreferenceConstants.PYJAMA_COMPILER_PATH)) {
				// change compiler path
				IPreferenceStore prefs = Activator.getDefault()
						.getPreferenceStore();
				fCompilerPath = prefs
						.getString(PreferenceConstants.PYJAMA_COMPILER_PATH);
				// System.out.println("file");
			}
			if (event.getProperty().equals(
					PreferenceConstants.PYJAMA_ASSOCIATE_NATURE)) {
				// add or remove builder from project
				IPreferenceStore prefs = Activator.getDefault()
						.getPreferenceStore();
				fCanBuild = prefs
						.getBoolean(PreferenceConstants.PYJAMA_ASSOCIATE_NATURE);
			}
			if (event.getProperty().equals(
					PreferenceConstants.PYJAMA_USE_CUSTOM_COMPILER)) {
				IPreferenceStore prefs = Activator.getDefault()
						.getPreferenceStore();
				fUseCustomCompiler = prefs
						.getBoolean(PreferenceConstants.PYJAMA_USE_CUSTOM_COMPILER);
			}
		}
	};

	/**
	 * The Constructor. Initializes builder with preferences. Adds a property
	 * changed listener for updating when preferences change.
	 */
	public FileBuilder() {
		super();
        
		System.out.println("intializing builder with preferences");//wk
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();

		// set the compiler path
		fCompilerPath = prefs
				.getString(PreferenceConstants.PYJAMA_COMPILER_PATH);

		// set whether we should build
		fCanBuild = prefs
				.getBoolean(PreferenceConstants.PYJAMA_ASSOCIATE_NATURE);

		// set whether we should use a custom compiler
		fUseCustomCompiler = prefs
				.getBoolean(PreferenceConstants.PYJAMA_USE_CUSTOM_COMPILER);

		// add a property change listener when preferences change
		prefs.addPropertyChangeListener(fPropertyChangeListener);
	}

	/**
	 * Destructor equivalent. Current implementation removes the class' property
	 * change listener from the PYJAMA Preference page.
	 */
	public void dispose() {
		// remove the property change listener before class dies
		Activator.getDefault().getPreferenceStore()
				.removePropertyChangeListener(fPropertyChangeListener);
	}

	/**
	 * Builds the resources in a project. Invokes a {@link BuildVisitor} for
	 * full builds, or a ({@link BuildDeltaVisitor} for incremental and auto
	 * builds.
	 * 
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {

		// check if user wants PYJAMA builds
		if (!fCanBuild)
			return null;

		// determine what type of build was requested
		if (kind == FULL_BUILD) {
			// rebuild all .PYJAMA files in project
//			System.out.println("called full build");//wk
			fullBuild(monitor);
		} else {
//			System.out.println("else situation, not full build");//wk
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				// delta returns null if builder has not been invoked before,
				// or if builder has not been invoked for a very long time
//				System.out.println("delta is null,call full build");//wk
				fullBuild(monitor);
			} else {
				// changes detected, build only changed .PYJAMA resources
//				System.out.println("delta is not null,call incremental build");//wk
				incrementalBuild(delta, monitor);
			}
		}

		new Job("Project Build") {
			protected IStatus run(IProgressMonitor monitor) {

				try {
//					System.out.println("inside run method");//wk
					// refresh project
					getProject().refreshLocal(IProject.DEPTH_INFINITE, monitor);

					// invoke a full build
					getProject().build(
							IncrementalProjectBuilder.INCREMENTAL_BUILD,
							JavaCore.BUILDER_ID, null, monitor);

					// Getting Markers for test purposes
					IMarker[] markers = getProject().findMarkers(
							IMarker.PROBLEM, true, IProject.DEPTH_INFINITE);
//					System.out.println("marker:"+markers.toString());
					for (IMarker i : markers) {
						if(hasJavaMarker==true){
//							System.out.println("--------"+i.getType()+"  "+i.getAttribute(i.LINE_NUMBER)+" "+i.getAttribute(i.CHAR_START));
//							System.out.println("marker info here "+i+" ");
							if((Integer)i.getAttribute(IMarker.SEVERITY)==2)
							MarkerManager.getInstance().handleJavaMarker(i);
						}
						else{ 
							if((Integer)i.getAttribute(IMarker.SEVERITY)==2){
//								System.out.println("--------"+i.getType()+"  "+i.getAttribute(i.LINE_NUMBER)+" "+i.getAttribute(i.CHAR_START));
//								System.out.println("marker info here "+i+" "+i.getAttribute(i.SEVERITY));
								MarkerManager.getInstance().handlePjMarker(i);
							}
						}
					//	MarkerManager.getInstance().handleJavaMarker(i);
					}

//					}
					// TODO: recode this part, refactoring is interesting
					// IMarker[] markerText=
					// getProject().findMarkers(IMarker.TEXT, true,
					// IProject.DEPTH_INFINITE);
					// for (IMarker i : markerText) {
					// MarkerManager.getInstance().handlePJFactoring(i);
					// }
				} catch (CoreException exception) {
					// log.logError(exception);
				} 
//				catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				return Status.OK_STATUS;
			}
		}.schedule();
		return null;
	}

	/**
	 * Current implementation removes all PYJAMA Problem markers from the
	 * project.
	 * 
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#clean(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void clean(IProgressMonitor monitor) {
		System.out.println("clean called");//wk
		MarkerManager.getInstance().deletePYJAMAMarkers(getProject());
	}

	private void fullBuild(IProgressMonitor monitor) {
//		if (!MarkerManager.getInstance().deletePYJAMAMarkers(getProject()))
//			return;

		try {
			System.out.println("inside fullBuild");//wk
			// create a BuildVisitor to visit resource and resources children.
			// since this is called on the project, we visit every resource in
			// the workspace
			getProject().accept(new BuildVisitor());

			// getProject().refreshLocal(IProject.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
		}
	}

	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
		try {
			// delta is the root of the tree representing changes in the project
			// we will traverse the tree to evaluate all changes
			delta.accept(new BuildDeltaVisitor());

			// getProject().refreshLocal(IProject.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
		}
	}

	// --------------------------------------------------------------------------
	// COMPILER RELATED METHODS
	/**
	 * Stream that reads in bytes and stores them. Used when invoking the
	 * default compiler
	 */
	private static class StringOutputStream extends OutputStream {
		private StringBuffer fBuffer;

		public StringOutputStream(StringBuffer buf) {
			super();
//			System.out.println("StringOutputStream is used");//wk
			Assert.isNotNull(buf);
			fBuffer = buf;
		}

		@Override
		public void write(int b) throws IOException {
			fBuffer.append((char) b);
		}
	}

	public static void invokeCompiler(final String toCompile,
			final IResource resource) throws Exception {
		final String javaFileName=new ErrorDetector().detect(resource);
		
//		System.out.println("resource"+resource);

//		System.out.println("inside involeCompiler"+resource);//wk
		final File projectLocation = resource.getProject().getLocation()
				.toFile();
//		System.out.println(projectLocation);//wk
		// check if User wanted to use their own custom compiler
		if (fUseCustomCompiler) {
			if (fCompilerPath.isEmpty()) {
				return;
			}
			// Dynamically form the run command
			String cmd="javac "+javaFileName;
			System.out.println(cmd);

			Runtime rt = Runtime.getRuntime();
			try {
				// execute command
				Process proc = rt.exec(cmd);

				// establish objects to read output of executed program
				StreamGobbler errorGobbler = new StreamGobbler(
						proc.getErrorStream(), "ERROR");
				StreamGobbler outputGobbler = new StreamGobbler(
						proc.getInputStream(), "OUTPUT");

				errorGobbler.start();
				outputGobbler.start();

				// block until compiler finished
				proc.waitFor();

				errorGobbler.join();
				outputGobbler.join();

				// System.out.println("errorGobbler.getMsg(): " +
				// errorGobbler.getMsg());

				if (!errorGobbler.getMsg().isEmpty()) {
					// error stream was not empty .: some error occured
					// the error message is stored in errorGobbler.getMsg()
					// NOTE: the error message is truncated from what the
					// compiler
					// puts out, because there's a lot of junk that is
					// irrelevant
					System.out.println("--------------error stream is not empty");//wk
					String s = errorGobbler.getMsg();
					MarkerInfo m = createMarkerFromErrorString(s,resource);
					MarkerManager.getInstance().reportProblem(resource, m);
					hasJavaError=true;
					hasJavaMarker=true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// User chose to use the default compiler

			// for invoking from the same JVM using a different thread
			// This way requires PTCompiler.jar to be specified on the class
			// path in
			// the manifest file, and means the user does not ever specify the
			// jar file

			Runnable r = new Runnable() {
				@Override
				public void run() {
					// step 1

					try {
					
					// Dynamically form the run command
						
                    String cmd=getJavaProjectTypeCommand(toCompile, projectLocation,javaFileName);
					System.out.println(cmd);				

					Runtime rt = Runtime.getRuntime();
						// execute command
						Process proc = rt.exec(cmd);

						// establish objects to read output of executed program
						StreamGobbler errorGobbler = new StreamGobbler(
								proc.getErrorStream(), "ERROR");
						StreamGobbler outputGobbler = new StreamGobbler(
								proc.getInputStream(), "OUTPUT");

						errorGobbler.start();
						outputGobbler.start();

						// block until compiler finished
						proc.waitFor();

						errorGobbler.join();
						outputGobbler.join();

						// System.out.println("errorGobbler.getMsg(): " +
						// errorGobbler.getMsg());

						if (!errorGobbler.getMsg().isEmpty()) {
							// error stream was not empty .: some error occured
							// the error message is stored in
							// errorGobbler.getMsg()
							// NOTE: the error message is truncated from what
							// the compiler
							// puts out, because there's a lot of junk that is
							// irrelevant
							System.out.println("--------------error stream is not empty");//wk
							String s = errorGobbler.getMsg();
							System.out.println(s);
							MarkerInfo m = createMarkerFromErrorString(s,resource);
							MarkerManager.getInstance().reportProblem(resource,m);
							hasJavaError=true;
							hasJavaMarker=true;
						}
						invokePjCompiler(toCompile,resource);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
//					catch (Exception e) {
//						e.printStackTrace();
//					} 
					finally {
						// step 2 =========================================

						// if not running in debug mode, delete the ptjava files
//						IPreferenceStore prefs = Activator.getDefault()
//								.getPreferenceStore();
//						if (false == prefs
//								.getBoolean(PreferenceConstants.PYJAMA_DEBUG_MODE)) {
//							if (true == toCompile
//									.endsWith(PYJAMA_FILE_EXTENSION)) {
//								String toDelete = toCompile.substring(
//										0,
//										toCompile.length()
//												- TODELETE_FILE_EXTENSION
//														.length());
//								toDelete += TODELETE_FILE_EXTENSION;
//								File toDeleteFile = new File(toDelete);
//								if (true == toDeleteFile.exists()) {
//									toDeleteFile.delete();
//								}
//							}
//						}
					}
				}
			};

			Thread t = new Thread(r);

			StringBuffer sb = new StringBuffer(4096);
			PrintStream printStream = new PrintStream(
					new StringOutputStream(sb));
			try {
				// redirecting error stream while compiler runs
				System.setErr(printStream);
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			t.run();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String errString = sb.toString();

			try {
				// return error stream
				System.setErr(System.err);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			if (!errString.isEmpty() /*
									 * && errString.startsWith(
									 * "********* Failed to parse")
									 */) {
				// parse the error string

				MarkerInfo m = createMarkerFromErrorString(errString,resource);
				MarkerManager.getInstance().reportProblem(resource, m);
			}
		}
	}
	
	//--------------------------------------------------
	public static void invokePjCompiler(final String toCompile,
			final IResource resource) throws Exception {
		
	
		final File projectLocation = resource.getProject().getLocation()
				.toFile();
//		System.out.println(projectLocation);//wk
		// check if User wanted to use their own custom compiler
		if (fUseCustomCompiler) {
			if (fCompilerPath.isEmpty()) {
				return;
			}
			if(hasJavaError==true){
				hasJavaError=false;
				return;
			}

			// Dynamically form the run command
			String cmd = getProjectTypeCommand(toCompile, projectLocation);
//			System.out.println(cmd);

			Runtime rt = Runtime.getRuntime();
			try {
				// execute command
				Process proc = rt.exec(cmd);
//				System.out.println("second cmd running");
				hasJavaMarker=false;

				// establish objects to read output of executed program
				StreamGobbler errorGobbler = new StreamGobbler(
						proc.getErrorStream(), "ERROR");
				StreamGobbler outputGobbler = new StreamGobbler(
						proc.getInputStream(), "OUTPUT");

				errorGobbler.start();
				outputGobbler.start();

				// block until compiler finished
				proc.waitFor();

				errorGobbler.join();
				outputGobbler.join();

				// System.out.println("errorGobbler.getMsg(): " +
				// errorGobbler.getMsg());

				if (!errorGobbler.getMsg().isEmpty()) {
					// error stream was not empty .: some error occured
					// the error message is stored in errorGobbler.getMsg()
					// NOTE: the error message is truncated from what the
					// compiler
					// puts out, because there's a lot of junk that is
					// irrelevant
					System.out.println("-----------!!!!!!!!!!!!!!!I'm here");//wk
					String s = errorGobbler.getMsg();
					MarkerInfo m = createMarkerFromErrorString(s,resource);
					MarkerManager.getInstance().reportProblem(resource, m);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// User chose to use the default compiler

			// for invoking from the same JVM using a different thread
			// This way requires PTCompiler.jar to be specified on the class
			// path in
			// the manifest file, and means the user does not ever specify the
			// jar file

			Runnable r = new Runnable() {
				@Override
				public void run() {
					// step 1
					if(hasJavaError==true){
						hasJavaError=false;
						return;
					}
//					String javaFileName;
					try {
//						javaFileName = new ErrorDetector().detect(resource);
					
					// Dynamically form the run command					
					
					String cmd = getProjectTypeCommand(toCompile,
							projectLocation);
					
					System.out.println(cmd);				

					Runtime rt = Runtime.getRuntime();
						// execute command
						Process proc = rt.exec(cmd);
						hasJavaMarker=false;
						// establish objects to read output of executed program
						StreamGobbler errorGobbler = new StreamGobbler(
								proc.getErrorStream(), "ERROR");
						StreamGobbler outputGobbler = new StreamGobbler(
								proc.getInputStream(), "OUTPUT");

						errorGobbler.start();
						outputGobbler.start();

						// block until compiler finished
						proc.waitFor();

						errorGobbler.join();
						outputGobbler.join();

						// System.out.println("errorGobbler.getMsg(): " +
						// errorGobbler.getMsg());

						if (!errorGobbler.getMsg().isEmpty()) {
							// error stream was not empty .: some error occured
							// the error message is stored in
							// errorGobbler.getMsg()
							// NOTE: the error message is truncated from what
							// the compiler
							// puts out, because there's a lot of junk that is
							// irrelevant
							String s = errorGobbler.getMsg();
							MarkerInfo m = createMarkerFromErrorString(s,resource);
							MarkerManager.getInstance().reportProblem(resource,
									m);
							System.out.println("----------!!!!"+s+" -------!!!");
//							hasJavaError=true;
//							invokeCompiler(toCompile,resource);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
//					catch (Exception e) {
//						e.printStackTrace();
//					} 
					finally {
						// step 2 =========================================

						// if not running in debug mode, delete the ptjava files
//						IPreferenceStore prefs = Activator.getDefault()
//								.getPreferenceStore();
//						if (false == prefs
//								.getBoolean(PreferenceConstants.PYJAMA_DEBUG_MODE)) {
//							if (true == toCompile
//									.endsWith(PYJAMA_FILE_EXTENSION)) {
//								String toDelete = toCompile.substring(
//										0,
//										toCompile.length()
//												- TODELETE_FILE_EXTENSION
//														.length());
//								toDelete += TODELETE_FILE_EXTENSION;
//								File toDeleteFile = new File(toDelete);
//								if (true == toDeleteFile.exists()) {
//									toDeleteFile.delete();
//								}
//							}
//						}
					}
				}
			};

			Thread t = new Thread(r);

			StringBuffer sb = new StringBuffer(4096);
			PrintStream printStream = new PrintStream(
					new StringOutputStream(sb));
			try {
				// redirecting error stream while compiler runs
				System.setErr(printStream);
			} catch (SecurityException e) {
				e.printStackTrace();
			}

			t.run();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String errString = sb.toString();

			try {
				// return error stream
				System.setErr(System.err);
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			if (!errString.isEmpty() /*
									 * && errString.startsWith(
									 * "********* Failed to parse")
									 */) {
				// parse the error string

				MarkerInfo m = createMarkerFromErrorString(errString,resource);
				MarkerManager.getInstance().reportProblem(resource, m);
			}
		}
	}

	private static MarkerInfo createMarkerFromErrorString(String errorString,IResource resource) {
		System.out.println("inside createMarkerFromErrorString one!!");//wk
//		Pattern pattern = Pattern
//				.compile(".*(jump\\.parser\\.(.*) at (line (\\d+)), column (\\d)+\\.).*");
		
//		Pattern pattern= Pattern.compile("pj\\.parser\\.ParseException: Encountered \".*\" at (line (\\d+)), column (\\d+)+\\.");
		Pattern pattern= Pattern.compile("pj\\.parser\\..* at (line (\\d+)), column (\\d+)+\\.");
		Matcher matcher = pattern.matcher(errorString);
		
//		Pattern jpattern=Pattern.compile("pj\\.parser\\.TokenMgrError: Lexical error at (line (\\d+)), column (\\d+)+\\.");
//		Matcher jmatcher = jpattern.matcher(errorString);
		
		MarkerInfo m = new MarkerInfo();
		if (matcher.find()) {
			System.out.println("-------inside find-------");
			// m.fMsg= matcher.group();
			
			
			m.fMsg = errorString;
			System.out.println(m.fMsg);
//			m.fMsg = errorString.substring(0,
//					errorString.indexOf("at pj.parser."));

			m.fLocation = matcher.group(1);
			System.out.println(m.fLocation);
//			System.out.println(matcher.group(2));
//			System.out.println(matcher.group(3));
//			System.out.println("mather end(1)"+matcher.end(1));
//			System.out.println("mather start(1)"+matcher.start(1));
//			System.out.println("mather end(2)"+matcher.end(2));
//			System.out.println("mather start(2)"+matcher.start(2));
//			System.out.println("mather end(3)"+matcher.end(3));
//			System.out.println("mather start(3)"+matcher.start(3));
			System.out.println("line"+(matcher.end(1)-matcher.start(1)+5));
			int column=Integer.parseInt(matcher.group(3));
			m.fLineNumber=Integer.parseInt(errorString.substring(matcher.start(1)+5,matcher.end(1)));
			String pyjamaSource = FileResourceManager.getInstance().loadSource(
					resource);
			MarkerManager.getInstance().findOffsetOfLine(pyjamaSource,m.fLineNumber);
			m.fCharStart=MarkerManager.getInstance().findOffsetOfLine(pyjamaSource,m.fLineNumber,column);
//			m.fCharStart=MarkerManager.getInstance().findOffsetOfLine(pyjamaSource,m.fLineNumber);
			m.fCharEnd=m.fCharStart+1;
			System.out.println(m.fCharStart);
			System.out.println("fail to parse -----"+m.fLineNumber);
		} else {
			System.out.println("-------inside else-------");
//			m.fCharStart=20;
//			m.fCharEnd=m.fCharStart+1;
			m.fMsg = errorString;
			System.out.println(m.fMsg);
		}
		return m;
	}

	private static MarkerInfo createMarkerFromCompileErrorString(
			String errorString) {
		System.out.println("inside createMarkerFromCompileErrorString two");//wk
		Pattern pattern = Pattern.compile("####\\[(\\d+)\\]####");
		Matcher matcher = pattern.matcher(errorString);

		MarkerInfo m = new MarkerInfo();
		if (matcher.find()) {
			// m.fMsg= matcher.group();

			m.fMsg = errorString.substring(0,
					errorString.indexOf("at jump.parser."));

			m.fLocation = matcher.group(1);
			m.fLineNumber = Integer.parseInt(matcher.group(4));
		} else {
			m.fMsg = errorString;
		}
		return m;
	}

	// --------------------------------------------------------------------------
	// UTILITY METHODS

	/**
	 * The ID for the builder.
	 */
	public static final String BUILDER_ID = Activator.PLUGIN_ID
			+ ".pyjamaFileBuilder";

	/**
	 * Associates the Pyjama builder with the given project.
	 * 
	 * @param project
	 *            The project to add the builder to
	 */
	public static void addBuilderToProject(IProject project) {
		// cannot modify closed projects
		if (!project.isOpen())
			return;

		// get the description
		System.out.println("inside addBuilderToProject");//wk
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException exception) {
			// //PYJAMALog.logError(exception);
			return;
		}

		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID))
				return;
		}

		// Associate builder with project.
		ICommand newCmd = description.newCommand();
		newCmd.setBuilderName(BUILDER_ID);
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.add(newCmd);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));

		// set the project description
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			// PYJAMALog.logError(e);
		}
	}

	/**
	 * Removes the builder from the given project. Does nothing if the project
	 * is not open or does not have the builder associated with it.
	 * 
	 * @param project
	 *            The project to remove the builder from
	 */
	public static void removeBuilderFromProject(IProject project) {

		// Cannot modify closed projects.
		if (!project.isOpen())
			return;
		System.out.println("inside removeBuilderFromProject");//wk
		// Get the description.
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			// PYJAMALog.logError(e);
			return;
		}

		// Look for builder.
		int index = -1;
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (cmds[j].getBuilderName().equals(BUILDER_ID)) {
				index = j;
				break;
			}
		}
		if (index == -1)
			return;

		// Remove builder from project.
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			// PYJAMALog.logError(e);
		}
	}
	
//	private static String getJavaProjectTypeCommand(String _toCompile,File _projectLocation){
//		String command=null;
//		DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
//		
//	}

	private static String getProjectTypeCommand(String _toCompile,
			File _projectLocation) {
		System.out.println("inside getProjectTypeCommand");//wk
		String command = null;

		String jarPath = pjplugin.preferences.PreferencePage
				.getDefaultRuntimeJarPath().toString();

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			FileInputStream classpathStream = new FileInputStream(
					_projectLocation.toString()
							.concat(PJConstants.DEFAULT_PATH + ".")
							.concat(CLASSPATH));
			Document classpathDoc = documentBuilder.parse(classpathStream);
			NodeList list = classpathDoc.getElementsByTagName(CLASSPATH);
			int len = list.getLength();
			l1: for (int i = 0; i < len; i++) {
				NodeList list1 = list.item(i).getChildNodes();
				int len1 = list1.getLength();
				if (len1 > 0) {
					for (int j = 0; j < len1; j++) {
						NamedNodeMap nodeMap = list1.item(j).getAttributes();
						if (nodeMap != null) {
							int len2 = nodeMap.getLength();
							for (int k = 0; k < len2; k++) {
								String nodeName = nodeMap.item(k).getNodeName();
								String nodeValue = nodeMap.item(k)
										.getNodeValue();

								if (nodeName != null
										&& PATH.equalsIgnoreCase(nodeName)
										&& nodeValue != null) {
									if (nodeValue.matches("(.*)pyjama(.*)jar")) {
										jarPath = nodeValue;
										break l1;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jarPath == null || PJConstants.DEFAULT_PATH.equals(jarPath)) {
			if (_projectLocation != null) {
				String libFolderPath = _projectLocation.getAbsolutePath()
						+ File.separator + PJConstants.LIBS_DIRECTORY
						+ File.separator + PJConstants.PJ_JARNAME;
				File pjJarFile = new File(libFolderPath);
				if (pjJarFile.exists()) {
					jarPath = libFolderPath;
					projectType = ProjectType.ANDROID;
				} else {
					libFolderPath = _projectLocation.getAbsolutePath()
							+ File.separator + PJConstants.LIB_DIRECTORY
							+ File.separator + PJConstants.PJ_JARNAME;
					pjJarFile = new File(libFolderPath);
					if (pjJarFile.exists()) {
						jarPath = libFolderPath;
						projectType = ProjectType.DESKTOP;
					}
				}
			}
		}

		if (jarPath != null) {
			command = (JAVA_CMD_OPTION).concat(jarPath).concat(" ")
					.concat(_toCompile);
			if (projectType == ProjectType.ANDROID)
				command = command.concat(" ").concat(ANDROID_CMD_OPTION);
		}

		return command;
	}
	
	private static String getJavaProjectTypeCommand(String _toCompile,
			File _projectLocation,String javaFileName) {
		System.out.println("inside getProjectTypeCommand");//wk
		String command = null;

		String jarPath = pjplugin.preferences.PreferencePage
				.getDefaultRuntimeJarPath().toString();

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			FileInputStream classpathStream = new FileInputStream(
					_projectLocation.toString()
							.concat(PJConstants.DEFAULT_PATH + ".")
							.concat(CLASSPATH));
			Document classpathDoc = documentBuilder.parse(classpathStream);
			NodeList list = classpathDoc.getElementsByTagName(CLASSPATH);
			int len = list.getLength();
			l1: for (int i = 0; i < len; i++) {
				NodeList list1 = list.item(i).getChildNodes();
				int len1 = list1.getLength();
				if (len1 > 0) {
					for (int j = 0; j < len1; j++) {
						NamedNodeMap nodeMap = list1.item(j).getAttributes();
						if (nodeMap != null) {
							int len2 = nodeMap.getLength();
							for (int k = 0; k < len2; k++) {
								String nodeName = nodeMap.item(k).getNodeName();
								String nodeValue = nodeMap.item(k)
										.getNodeValue();

								if (nodeName != null
										&& PATH.equalsIgnoreCase(nodeName)
										&& nodeValue != null) {
									if (nodeValue.matches("(.*)pyjama(.*)jar")) {
										jarPath = nodeValue;
										break l1;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jarPath == null || PJConstants.DEFAULT_PATH.equals(jarPath)) {
			if (_projectLocation != null) {
				String libFolderPath = _projectLocation.getAbsolutePath()
						+ File.separator + PJConstants.LIBS_DIRECTORY
						+ File.separator + PJConstants.PJ_JARNAME;
				File pjJarFile = new File(libFolderPath);
				if (pjJarFile.exists()) {
					jarPath = libFolderPath;
					projectType = ProjectType.ANDROID;
				} else {
					libFolderPath = _projectLocation.getAbsolutePath()
							+ File.separator + PJConstants.LIB_DIRECTORY
							+ File.separator + PJConstants.PJ_JARNAME;
					pjJarFile = new File(libFolderPath);
					if (pjJarFile.exists()) {
						jarPath = libFolderPath;
						projectType = ProjectType.DESKTOP;
					}
				}
			}
		}

		if (jarPath != null) {
			command = "javac -cp ".concat(jarPath).concat(" ")
					.concat(javaFileName);
			if (projectType == ProjectType.ANDROID)
				command = command.concat(" ").concat(ANDROID_CMD_OPTION);
		}

		return command;
	}
}
