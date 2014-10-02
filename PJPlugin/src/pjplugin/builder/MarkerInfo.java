package pjplugin.builder;

public class MarkerInfo {
	/**
	 * The Constructor. Currently sets all data fields to <code>null</code>.
	 */
	public MarkerInfo() {
		fCharStart= null;
		fCharEnd= null;
		fMsg= null;
		fLocation= null;
		fLineNumber= null;
		fSeverity= null;
	}
	/**
	 * The zero-relative character offset to the start location of the marker in the text file. 
	 */
	public Integer fCharStart;
	/**
	 * The zero-relative character offset to the end location of the marker in the text file. 
	 */
	public Integer fCharEnd;
	/**
	 * The message the marker contains
	 */
	public String fMsg;
	/**
	 * The 1-relative line location of the marker (as a {@link java.lang.String})
	 */
	public String fLocation;
	/**
	 * The 1-relative line number the marker is located on.
	 */
	public Integer fLineNumber;
	/**
	 * The severity of the marker. Can be one of:
	 * <ul>
	 * 	<li>{@link org.eclipse.core.resources.IMarker#SEVERITY_INFO}</li>
	 *  <li>{@link org.eclipse.core.resources.IMarker#SEVERITY_WARNING}</li>
	 *  <li>{@link org.eclipse.core.resources.IMarker#SEVERITY_ERROR}</li>
	 * </ul>
	 */
	public Integer fSeverity;
}
