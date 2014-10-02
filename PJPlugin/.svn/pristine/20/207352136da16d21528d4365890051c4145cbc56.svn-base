package pjplugin.wizards;

import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;

import pjplugin.Activator;

public class NewSampleProjectCreationPageTwo extends NewJavaProjectWizardPageOne{

	public NewSampleProjectCreationPageTwo(){
		super();
		setProjectName(Activator.getCurrentProjectCreationName());
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		setProjectName(Activator.getCurrentProjectCreationName());
	}
}
