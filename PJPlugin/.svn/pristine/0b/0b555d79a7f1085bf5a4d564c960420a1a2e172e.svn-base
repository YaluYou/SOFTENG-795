package pjplugin.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pjplugin.Activator;
import pjplugin.builder.FileBuilder;
import pjplugin.builder.ProjectType;
import pjplugin.constants.PJConstants;

/**
 * @author sriram (sazh998) This class will dynamically add pyjama dependencies
 *         to any eclipse project, will be enabled or disabled based on the
 *         availability of pyjama lib dependency
 */
public class AddPyjamaNature implements IObjectActionDelegate {
	public static final String PROJECT = "project";
	public static final String CLASSPATH = "classpath";
	public static final String PATH = "path";
	public static final String NATURES = "natures";
	public static final String NATURE = "nature";
	public static final String KIND = "kind";
	public static final String BUILDSPEC = "buildSpec";
	public static final String BUILDCOMMAND = "buildCommand";
	public static final String NAME = "name";
	public static final String ARGUMENTS = "arguments";

	@Override
	public void run(IAction _arg0) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null) {
				IStructuredSelection selection = (IStructuredSelection) window
						.getSelectionService().getSelection();
				Object firstElement = selection.getFirstElement();
				if (firstElement instanceof IAdaptable) {
					IProject project = (IProject) ((IAdaptable) firstElement)
							.getAdapter(IProject.class);
					IPath workspacePath = project.getWorkspace().getRoot()
							.getLocation();
					IPath projectPath = project.getFullPath();
					String projectPathLocation = workspacePath.toString()
							.concat(projectPath.toString());
					FileInputStream fileStream = new FileInputStream(
							projectPathLocation.concat(
									PJConstants.DEFAULT_PATH + ".").concat(
									PROJECT));
					Document projectDoc = documentBuilder.parse(fileStream);
					projectDoc = addPyjamaBuildCommand(projectDoc);
					projectDoc = addNature(projectDoc);

					FileInputStream classpathStream = new FileInputStream(
							projectPathLocation.concat(
									PJConstants.DEFAULT_PATH + ".").concat(
									CLASSPATH));
					Document classpathDoc = documentBuilder
							.parse(classpathStream);
					classpathDoc = updateClasspath(classpathDoc,
							projectPathLocation);

					TransformerFactory transformerFactory = TransformerFactory
							.newInstance();
					Transformer transformer = transformerFactory
							.newTransformer();
					DOMSource sourceProject = new DOMSource(projectDoc);
					StreamResult destinationProject = new StreamResult(
							new FileOutputStream(projectPathLocation.concat(
									PJConstants.DEFAULT_PATH + ".").concat(
									PROJECT)));
					transformer.transform(sourceProject, destinationProject);

					DOMSource sourceClasspath = new DOMSource(classpathDoc);
					StreamResult destinationClasspath = new StreamResult(
							new FileOutputStream(projectPathLocation.concat(
									PJConstants.DEFAULT_PATH + ".").concat(
									CLASSPATH)));
					transformer
							.transform(sourceClasspath, destinationClasspath);

					addPyjamaJar(project);

					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.out
					.println("[AddPyjamaNature][run] Error in parsing the xml file");
		} catch (SAXException e) {
			e.printStackTrace();
			System.out
					.println("[AddPyjamaNature][run] Error in parsing the xml file");
		} catch (IOException e) {
			e.printStackTrace();
			System.out
					.println("[AddPyjamaNature][run] Error in opening the xml file");
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
			System.out
					.println("[AddPyjamaNature][run] Error in configuring the xml file");
		} catch (TransformerException e1) {
			e1.printStackTrace();
			System.out
					.println("[AddPyjamaNature][run] Error in transforming the data to xml file");
		} catch (CoreException e) {
			e.printStackTrace();
			System.out
					.println("[AddPyjamaNature][run] Error in refreshing the project");
		}
	}

	private Document updateClasspath(Document _doc, String _projectPathLocation) {
		NodeList list = _doc.getElementsByTagName(CLASSPATH);
		Node classpathNode = list.item(0);

		int len = list.getLength();
		Boolean found = Boolean.FALSE;
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
							String nodeValue = nodeMap.item(k).getNodeValue();

							if (nodeName != null
									&& PATH.equalsIgnoreCase(nodeName)
									&& nodeValue != null) {
								if (nodeValue.contains(PJConstants.PJ_JARNAME)) {
									found = Boolean.TRUE;
									break l1;
								}
							}
						}
					}
				}
			}
		}

		if (!found) {
			Element classpathentry = _doc.createElement("classpathentry");
			classpathentry.setAttribute(KIND, PJConstants.LIB_DIRECTORY);

			File libFolder = new File(_projectPathLocation + File.separator
					+ PJConstants.LIBS_DIRECTORY);
			if (libFolder != null && libFolder.exists()) {
				classpathentry.setAttribute(
						PATH,
						_projectPathLocation.concat(
								File.separator + PJConstants.LIBS_DIRECTORY
										+ File.separator).concat(
								PJConstants.PJ_JARNAME));
				FileBuilder.projectType = ProjectType.ANDROID;
			} else {
				classpathentry.setAttribute(
						PATH,
						_projectPathLocation.concat(
								File.separator + PJConstants.LIB_DIRECTORY
										+ File.separator).concat(
								PJConstants.PJ_JARNAME));
				FileBuilder.projectType = ProjectType.DESKTOP;
			}
			classpathNode.appendChild(classpathentry);
		}
		return _doc;
	}

	private Document addPyjamaBuildCommand(Document _doc) {
		NodeList list = _doc.getElementsByTagName(NATURES);
		Node naturesNode = list.item(0);
		NodeList naturesNodeChildren = naturesNode.getChildNodes();
		int totalChildren = naturesNodeChildren.getLength();
		Boolean found = Boolean.FALSE;
		for (int i = 0; i < totalChildren; i++) {
			NodeList clist = naturesNodeChildren.item(i).getChildNodes();
			if (clist.getLength() > 0) {
				if (PJConstants.PJ_BUILDER_NATURE.equals(clist.item(0)
						.getNodeValue())) {
					found = Boolean.TRUE;
					break;
				}
			}
		}

		if (!found) {
			Element nature = _doc.createElement(NATURE);
			nature.appendChild(_doc
					.createTextNode(PJConstants.PJ_BUILDER_NATURE));
			naturesNode.appendChild(nature);
		}
		return _doc;
	}

	private Document addNature(Document _doc) {
		NodeList list = _doc.getElementsByTagName(BUILDSPEC);
		Node buildSpecNode = list.item(0);

		int len = list.getLength();
		Boolean found = Boolean.FALSE;
		brl: for (int i = 0; i < len; i++) {
			NodeList clist = list.item(i).getChildNodes();
			int l1 = clist.getLength();
			if (l1 > 0) {
				for (int j = 0; j < l1; j++) {
					NodeList clist1 = clist.item(j).getChildNodes();
					int l2 = clist1.getLength();
					if (l2 > 0) {
						for (int k = 0; k < l2; k++) {
							NodeList clist2 = clist1.item(k).getChildNodes();
							if (clist2.getLength() > 0) {
								if (PJConstants.PJ_BUILDER.equals(clist2
										.item(0).getNodeValue())) {
									found = Boolean.TRUE;
									break brl;
								}
							}
						}

					}
				}
			}
		}

		if (!found) {
			Element buildCommandNode = _doc.createElement(BUILDCOMMAND);

			Element nameNode = _doc.createElement(NAME);
			nameNode.appendChild(_doc.createTextNode(PJConstants.PJ_BUILDER));

			Element argumentsNode = _doc.createElement(ARGUMENTS);

			buildCommandNode.appendChild(nameNode);
			buildCommandNode.appendChild(argumentsNode);

			buildSpecNode.appendChild(buildCommandNode);
		}

		return _doc;
	}

	private void addPyjamaJar(IProject _project) throws IOException,
			CoreException {
		IFolder libFolder = _project.getFolder(PJConstants.LIBS_DIRECTORY);
		IFile pyjamaJarFile = null;
		Path jarSrcPath = new Path(PJConstants.DEFAULT_PATH
				+ PJConstants.LIB_DIRECTORY + PJConstants.DEFAULT_PATH
				+ PJConstants.PJ_JARNAME);
		URL installJarURL = Platform.getBundle(Activator.PLUGIN_ID).getEntry(
				jarSrcPath.toString());
		URL localJarURL = FileLocator.toFileURL(installJarURL);
		FileInputStream fileStream = new FileInputStream(localJarURL.getFile());
		Boolean found = Boolean.FALSE;
		if (libFolder.exists()) {
			pyjamaJarFile = libFolder.getFile(PJConstants.PJ_JARNAME);
			pyjamaJarFile.create(fileStream, false, null);
			found = Boolean.TRUE;
		}

		if (!found) {
			libFolder = _project.getFolder(PJConstants.LIB_DIRECTORY);
			if (!libFolder.exists())
				libFolder.create(true, true, null);

			pyjamaJarFile = libFolder.getFile(PJConstants.PJ_JARNAME);
			pyjamaJarFile.create(fileStream, false, null);
		}

		pjplugin.preferences.PreferencePage
				.setFgDefaultRuntimeJarPath(pyjamaJarFile.getLocation());
	}

	@Override
	public void setActivePart(IAction _arg0, IWorkbenchPart _arg1) {
		Boolean canDisplay = Boolean.TRUE;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IStructuredSelection selection = (IStructuredSelection) window
				.getSelectionService().getSelection();
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IAdaptable) {
			IProject project = (IProject) ((IAdaptable) firstElement)
					.getAdapter(IProject.class);
			IPath workspacePath = project.getWorkspace().getRoot()
					.getLocation();
			IPath projectPath = project.getFullPath();
			String projectPathLocation = workspacePath.toString().concat(
					projectPath.toString());

			File libDirectory = new File(projectPathLocation + File.separator
					+ PJConstants.LIBS_DIRECTORY);
			Boolean found = Boolean.FALSE;
			if (libDirectory.exists() && libDirectory.isDirectory()) {
				String[] files = libDirectory.list();
				for (String file : files) {
					if (file.equals(PJConstants.PJ_JARNAME)) {
						canDisplay = Boolean.FALSE;
						found = Boolean.TRUE;
					}
				}
			}

			if (!found) {
				libDirectory = new File(projectPathLocation + File.separator
						+ PJConstants.LIB_DIRECTORY);
				if (libDirectory.exists() && libDirectory.isDirectory()) {
					String[] files = libDirectory.list();
					for (String file : files) {
						if (file.equals(PJConstants.PJ_JARNAME)) {
							canDisplay = Boolean.FALSE;
						}
					}
				}
			}
		}

		System.setProperty("displayPJNatureMenu", canDisplay.toString());
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}
}
