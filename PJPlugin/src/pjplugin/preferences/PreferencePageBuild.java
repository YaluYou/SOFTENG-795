package pjplugin.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pjplugin.Activator;
import pjplugin.builder.FileBuilderNature;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;



/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PreferencePageBuild
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private static final String PROJECT_LOGO_LARGE = "icons/parait.png";
	/*
	 * The Strings that we will use
	 * TODO: put them as localized strings
	 */
	private static final String TITLE_STR = "Pyjama Plugin Preference";
	private static final String DESCRIPTION_STR = "Set the Build Preferences.";
	
	private BooleanFieldEditor fAutoBuild;
	/**
	 * The ID for the Preference page.
	 */
	public static final String ID = Activator.PLUGIN_ID + ".preferences.PyjamaPreferencePageBuild";
	
	private static final String AUTOBUILD_STR = "Automatically build the .java files when Pyjama files are changed.";
	

	public PreferencePageBuild() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		
		fAutoBuild = new BooleanFieldEditor(PreferenceConstants.PYJAMA_AUTO_BUILD, 
				AUTOBUILD_STR, getFieldEditorParent());
		addField(fAutoBuild);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setTitle(TITLE_STR);
		
		setDescription(DESCRIPTION_STR);
		
		ImageDescriptor desc = Activator.getImageDescriptor(PROJECT_LOGO_LARGE);
        setImageDescriptor(desc);		
	}
	
	/**
	 * Event handler for when a property is changes in the preferences page.
	 * Current implementation updates the preference page widgets as options are selected on the page.
	 * 
	 * @param event The property change event.
	 *
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		super.performDefaults();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		boolean ret= super.performOk();
		return ret;
	}
}