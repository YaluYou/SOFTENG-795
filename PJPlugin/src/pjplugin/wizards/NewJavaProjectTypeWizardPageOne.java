package pjplugin.wizards;

import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

import pjplugin.builder.FileBuilder;
import pjplugin.builder.ProjectType;

public class NewJavaProjectTypeWizardPageOne extends
		NewJavaProjectWizardPageOne {

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, 0);
		composite.setFont(parent.getFont());
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(256));

		Control nameControl = createNameControl(composite);
		nameControl.setLayoutData(new GridData(768));		

		Control locationControl = createLocationControl(composite);
		locationControl.setLayoutData(new GridData(768));

		Control jreControl = createJRESelectionControl(composite);
		jreControl.setLayoutData(new GridData(768));

		Control layoutControl = createProjectLayoutControl(composite);
		layoutControl.setLayoutData(new GridData(768));

		Control workingSetControl = createWorkingSetControl(composite);
		workingSetControl.setLayoutData(new GridData(768));

		Control infoControl = createInfoControl(composite);
		infoControl.setLayoutData(new GridData(768));

		setControl(composite);
	}

	private GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(4);
		layout.verticalSpacing = convertVerticalDLUsToPixels(4);
		if (margins) {
			layout.marginWidth = convertHorizontalDLUsToPixels(7);
			layout.marginHeight = convertVerticalDLUsToPixels(7);
		} else {
			layout.marginWidth = 0;
			layout.marginHeight = 0;
		}
		return layout;
	}
}
