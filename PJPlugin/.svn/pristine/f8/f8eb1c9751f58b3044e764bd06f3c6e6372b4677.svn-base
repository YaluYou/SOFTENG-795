package pjplugin.preferences;

import pjplugin.Activator;
import pjplugin.preferences.PreferenceConstants;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.PYJAMA_SHOW_HIDDEN_ERRORS, true);
		
		if(0 == store.getString(PreferenceConstants.PYJAMA_COMPILER_PATH).length()){
		    store.setDefault(PreferenceConstants.PYJAMA_COMPILER_PATH, new String());
		}
		store.setDefault(PreferenceConstants.PYJAMA_ASSOCIATE_NATURE, true);
		store.setDefault(PreferenceConstants.PYJAMA_USE_CUSTOM_COMPILER, false);
		store.setDefault(PreferenceConstants.PYJAMA_DEBUG_MODE, false);
		store.setDefault(PreferenceConstants.PYJAMA_AUTO_BUILD, true);
		store.setDefault(PreferenceConstants.PYJAMA_AUTO_COMPLETE, true);
		store.setDefault(PreferenceConstants.PYJAMA_HIGHLIGHT_DIRECTIVES, true);
		try{
			Color color = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
			PreferenceConverter.setDefault(store, PreferenceConstants.PYJAMA_HIGHLIGHT_DIRECTIVES_COLOR, color.getRGB());
		}catch(Exception e){}
		
	}

}
