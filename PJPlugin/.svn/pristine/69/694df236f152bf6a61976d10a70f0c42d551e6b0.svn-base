package pjplugin.wizards;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import pjplugin.Activator;
import pjplugin.builder.FileBuilder;
import pjplugin.builder.ProjectType;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (pj).
 */

public class NewSampleProjectcreationPageOne extends WizardPage implements SelectionListener, ModifyListener {
	/**
	 * UI elements
	 */
	private Text containerText;
	private Text fileText;
	private ISelection selection;
	private Table mTable;
	private TableViewer mTableViewer;
	private Text mSampleProjectName;
	
	/**
	 * Data
	 */
	private List<String> samples = new ArrayList<String>();
	private List<String> samplesProjects = new ArrayList<String>();
	
	private Group projectNameGroup;
	
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewSampleProjectcreationPageOne(ISelection selection) {
		super("samplePage");
		setTitle("Create a Sample Pyjama Project.");
		setDescription("Select a Sample Project to create.");
		this.selection = selection;
		Activator.initNewProject(true, NewProjectCreationState.samplesProjects.get(0));
		loadSamples();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

		mTableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		mTable = mTableViewer.getTable();
		GridData tableGridData = new GridData();
		tableGridData.grabExcessVerticalSpace = true;
		tableGridData.grabExcessHorizontalSpace = true;
		tableGridData.horizontalAlignment = GridData.FILL;
		tableGridData.verticalAlignment = GridData.FILL;
		tableGridData.horizontalSpan = 2;
		
		mTable.setLayoutData(tableGridData);
		mTable.addSelectionListener(this);
		
		GridLayout projectNameGridLayout = new GridLayout(2, false);
		projectNameGroup = new Group(container, SWT.NONE);
		projectNameGroup.setLayout(projectNameGridLayout);
		
		GridData projectNameGroupGridData = new GridData();		
		projectNameGroupGridData.grabExcessHorizontalSpace = true;
		projectNameGroupGridData.horizontalAlignment = GridData.FILL;
		projectNameGroupGridData.verticalAlignment = GridData.FILL;
		projectNameGroupGridData.horizontalSpan = 2;
		projectNameGroup.setLayoutData(projectNameGroupGridData);		
		
		Label projectLabel = new Label(projectNameGroup, SWT.NONE);
		projectLabel.setText("Project Name:");
		
		mSampleProjectName = new Text(projectNameGroup, SWT.BORDER);
		
		GridData projectNameTextGridData = new GridData();		
		projectNameTextGridData.grabExcessHorizontalSpace = true;
		projectNameTextGridData.horizontalAlignment = GridData.FILL;
		projectNameTextGridData.verticalAlignment = GridData.FILL;	
		mSampleProjectName.setLayoutData(projectNameTextGridData);
		mSampleProjectName.addModifyListener(this);			
		
		setControl(container);			
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible) {
			updateSamples();
		}
	}

	/**
	 * Important set of strings
	 * The string names and the project names should be similar
	 * 
	 * @note The project names are used as files names too
	 */
	private void loadSamples(){
		samples = (ArrayList<String>) NewProjectCreationState.samples;
		samplesProjects = (ArrayList<String>) NewProjectCreationState.samplesProjects;
	}

	private void updateSamples() {
		IBaseLabelProvider labelProvider = new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				org.eclipse.jface.resource.ImageDescriptor imageDesc = org.eclipse.ui.plugin.AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/prclicon.png");
				Image imageLogo = imageDesc.createImage();
				return imageLogo;
			}

			@Override
			public String getText(Object element) {
				return (String) element;
			}
		};


		mTableViewer.setContentProvider(new ArrayContentProvider());
		mTableViewer.setLabelProvider(labelProvider);
		mTableViewer.setInput(samples);

		mTable.select(0);
		mSampleProjectName.setText(samplesProjects.get(0));
	}
	
	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
			}
		}
		fileText.setText("Mainapp.pj");
//		setPageComplete(true);
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() == mTable) {
			int index = mTable.getSelectionIndex();
			if (index >= 0) {
				String projName = samplesProjects.get(index);
				mSampleProjectName.setText(projName);
				Activator.setCurrentProjectCreationName(projName);
				
				// TODO: fix this design 
				NewSampleProjectCreationPageTwo page = (NewSampleProjectCreationPageTwo) NewProjectCreationState.pages[1];
				page.setProjectName(projName);				
			}
		} else {
			assert false : e.getSource();
		}
	}
}