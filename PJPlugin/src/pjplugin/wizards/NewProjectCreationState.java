package pjplugin.wizards;

/**
 * This class specific the state of project creation
 * We also define the list of sample projects here
 * 
 * @author vikassingh
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewProjectCreationState {
	
	public boolean bOnGoingCreation;
	
	public boolean bIsSample;
	
	public String projectName;
	
	public static Object[] pages;
	
	/*
	 * we use these strings for the project names
	 * in the sample projects.
	 * at the same time, the .pj files in the 
	 * respective files also use the same name
	 * and those files are copied from the /sources
	 */
	public static final List<String> samplesProjects = new ArrayList<String>(Arrays.asList(
			"HelloWorld",
			"WorkSharingDir",
			"ParallelDir",
			"CombinedDir",
			"GuiAware",
			"MandelBrot",
			"ReductionTest"
	));

    public static final List<String> samples = new ArrayList<String>(Arrays.asList(
			"\t Hello World",
			"\t Work Sharing constructs",
			"\t Parallel constructs",
			"\t Combined constructs",
			"\t GUI-aware constucts",
			"\t Mandelbrot UI Example",
			"\t Reduction Example"
	));
}
