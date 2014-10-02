package pjplugin.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import pjplugin.Activator;

public class PreferenceManager {
	
	private IPreferenceStore store = null;
	
	private static PreferenceManager instance = null;

	private PreferenceManager(){
		this.store = Activator.getDefault().getPreferenceStore();;
	}

	public static PreferenceManager getInstance(){
		if(null == instance){
			instance = new PreferenceManager();
		}
		return instance;
	}
	
	public String getJarPath(){
		return store.getString(PreferenceConstants.PYJAMA_COMPILER_PATH);
	}
	
	public void setJarPath(String jarPath){
		store.setValue(PreferenceConstants.PYJAMA_COMPILER_PATH, jarPath);
	}
	
	// TODO: for other pref constants add the APIs
}
