package pjplugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import pjplugin.builder.FileBuilder;
import pjplugin.builder.FileBuilderNature;

/**
 * Apart from the typical file creation implementation,
 * this class also generates the Hello World sample class.
 * Since the Hello World code is small, this class is
 * optimised not to do code copies (like sample project creation),
 * instead flat code writing is implemented.
 * 
 * @author vikassingh
 *
 */
public class NewFileCreationPage extends WizardNewFileCreationPage {

	private IWorkbench workBench;

	//	private Text containerText;
	//	private Text title;

	// TODO: put them in localized resource
	private final String CODE_GENERATION_OPTIONS_TEXT = "Code Generation Options";
	private final String CODE_GENERATION_GEN_HW_TEXT = "Create \"Hello world\" sample code"; 
	private final String TITLE_TEXT = "Create New Pyjama Class"; 
	private final String DESCRIPTION_TEXT = "This wizard creates new file with .pj extension";
	private final String PACKAGE_TEXT = "&Package name:";

	// TODO: add implementations for super class
	private Text superClassText = null;

	private ISelection selection;

	private Text packageText = null;

	//private Group groupAdvanced;

	//private Button buttonHelloWorld;
	
	public NewFileCreationPage(String pageName, IWorkbench workbench, IStructuredSelection selection) {
		super(pageName, selection);
		this.selection = selection;
		this.workBench = workbench;

		setTitle(TITLE_TEXT);
		setDescription(DESCRIPTION_TEXT);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		//		Composite container = new Composite(parent, SWT.NULL);
		//		GridLayout layout = new GridLayout();
		//		container.setLayout(layout);
		//		layout.numColumns = 3;
		//		layout.verticalSpacing = 9;
		//		Label label = new Label(container, SWT.NULL);
		//		label.setText("&Folder:");
		//
		//		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		//		containerText.setLayoutData(gd);
		//		containerText.addModifyListener(new ModifyListener() {
		//			public void modifyText(ModifyEvent e) {
		//				dialogChanged();
		//			}
		//		});
		//
		//		Button button = new Button(container, SWT.PUSH);
		//		button.setText("Browse...");
		//		button.addSelectionListener(new SelectionAdapter() {
		//			public void widgetSelected(SelectionEvent e) {
		//				handleBrowse();
		//			}
		//		});
		//		label = new Label(container, SWT.NULL);
		//		label.setText("&File name:");
		//
		//		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		//		gd = new GridData(GridData.FILL_HORIZONTAL);
		//		fileText.setLayoutData(gd);
		//		fileText.addModifyListener(new ModifyListener() {
		//			public void modifyText(ModifyEvent e) {
		//				dialogChanged();
		//			}
		//		});
		//		initialize();
		//		dialogChanged();
		//		setControl(container);

		super.createControl(parent);

		// create the file list
		Composite composite = (Composite)getControl();

		// create the advanced group
		//groupAdvanced = new Group(composite, SWT.NONE);
		//GridLayout gridLayout = new GridLayout(); 
		//groupAdvanced.setLayout(gridLayout);
		//groupAdvanced.setText(CODE_GENERATION_OPTIONS_TEXT);
		//groupAdvanced.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));


		Label superClassLabel = new Label(composite, SWT.NULL);
		superClassLabel.setText("Super &class:");

		superClassText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gdClass = new GridData(GridData.FILL_HORIZONTAL);
		superClassText.setLayoutData(gdClass);
		superClassText.setText("java.lang.Object");
		superClassText.setEditable(false);
		superClassText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				//dialogChanged();
			}
		});

		Label packageLabel = new Label(composite, SWT.NULL);
		packageLabel.setText(PACKAGE_TEXT);

		packageText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gdPackage = new GridData(GridData.FILL_HORIZONTAL);
		packageText.setLayoutData(gdPackage);
		packageText.setText(getPackageName());
		packageText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				//dialogChanged();
			}
		});

		//  create a check box for generating class name
		/*buttonHelloWorld = new Button(groupAdvanced, SWT.CHECK);
		buttonHelloWorld.setText(CODE_GENERATION_GEN_HW_TEXT);
		buttonHelloWorld.setSelection(true);*/
	}

	/**
	 * Creates the new pj file and opens it in the workbench.
	 * @return <code>true</code> if the file is successfully opened, <code>false</code> otherwise
	 */
	public boolean finish() {
		// create the file
		String fileName = getFileName();
		if(false == fileName.endsWith(".pj")){
			fileName+=".pj";
		}
		setFileName(fileName);
		

		IFile file = createNewFile();

		// compile the file
//		FileBuilderNature.
//		FileBuilder.invokeCompiler(toCompile, resource)
		//  display the file
		IWorkbenchWindow window = workBench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		try {
			IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Creates the initial contents to be placed inside the newly created pj class file.
	 * Current version will generate package declaration and basic class declaration if
	 * those options are checked on the wizard page.
	 */
	@Override
	protected InputStream getInitialContents() {

		StringBuffer sb = new StringBuffer();

		// need to create package ?
		//if (buttonHelloWorld.getSelection())
		{
			IPath containerFullPath= getContainerFullPath();

			//  the original string to the container path
			String pathString= containerFullPath.toString();
			//System.out.println(pathString);

			int slashIndex= pathString.indexOf('/', 1);
			String truncatedPath= null;

			if (slashIndex > 0)
				truncatedPath= pathString.substring(0, slashIndex);
			else
				truncatedPath= pathString;

			//  find the project src folder path
			IProject project= JavaPlugin.getWorkspace().getRoot().getProject(truncatedPath);
			IJavaProject javaProject= JavaCore.create(project);
			if (javaProject != null) {
				try {
					String srcPath= null; 
					IClasspathEntry[] cp= javaProject.getRawClasspath();
					for (IClasspathEntry i : cp) {
						//System.out.println("i:"+i);
						if (i.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
							//  src entry found!
							srcPath= i.getPath().toString();
							//System.out.println("srcPath: " + srcPath);
							break;
						}
					}

					String packageString = "";
					if(null != packageText){
						packageString = packageText.getText();

						if(0 != packageString.length()){
							sb.append("package ");
							if(false == packageString.equals("")){								
								packageString = packageString.replace(File.separator.charAt(0), '.');								
								sb.append(packageString);
							}

							sb.append(";");
							sb.append("\n\n");
						}
					}

				}
				catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}

		//if(true == buttonHelloWorld.getSelection()){
		//	printHelloWorldClass(sb);
		//}
		
		generateDummyPjClass(sb);

		if (sb.length() == 0)
			return null;
		return new ByteArrayInputStream(sb.toString().getBytes());
	}
	
	private void generateDummyPjClass(StringBuffer sb){
		sb.append("import pj.*;");
		sb.append("\n\n");
		sb.append("public class ");
		String filename = getFileName();
		if (filename.contains("."))
			filename = filename.substring(0, filename.indexOf('.'));
		sb.append(filename);
		sb.append("{\n");
		sb.append("\t\n}");	
	}
	
	private void printHelloWorldClass(StringBuffer sb){
		sb.append("public class ");
		String filename = getFileName();
		if (filename.contains("."))
			filename = filename.substring(0, filename.indexOf('.'));
		sb.append(filename);
		sb.append("{\n");

		sb.append("\tpublic static void main(String[] args){\n\n");
		sb.append("\t\tSystem.out.println(\"Hello world from sequential code\");\n");
		sb.append("\t\t//#omp parallel for\n");
		sb.append("\t\tfor(int i = 0; i < 10; i++){\n");
		sb.append("\t\t\tSystem.out.println(\"Hello world from parallel code\");\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\t\n}");			
	}
	
	private String getPackageName(){
		IPath containerFullPath= getContainerFullPath();
		String s = "";		

		//  the original string to the container path
		String pathString= containerFullPath.toString();
		//System.out.println(pathString);

		int slashIndex= pathString.indexOf('/', 1);
		String truncatedPath= null;

		if (slashIndex > 0)
			truncatedPath= pathString.substring(0, slashIndex);
		else
			truncatedPath= pathString;

		//  find the project src folder path
		IProject project= JavaPlugin.getWorkspace().getRoot().getProject(truncatedPath);
		IJavaProject javaProject= JavaCore.create(project);
		if (javaProject != null) {
			try {
				String srcPath= null; 
				IClasspathEntry[] cp= javaProject.getRawClasspath();
				for (IClasspathEntry i : cp) {
					//System.out.println("i:"+i);
					if (i.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
						//  src entry found!
						srcPath= i.getPath().toString();
						//System.out.println("srcPath: " + srcPath);
						break;
					}
				}
				if (srcPath != null) {
					if (pathString.equals(srcPath)) {
						//  do nothing
					}
					else {

						//  omit src path if part of path string
						if (pathString.startsWith(srcPath)){
							s = pathString.substring(srcPath.length()+1);
						}
						//  the +1 is to remove the first "/" after the src path
						else{
							s = pathString;
						}
						s = s.replace('/', '.');
						s = s.replace('\\', '.');
					}
				}
			}
			catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

}