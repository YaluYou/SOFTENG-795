package pjplugin.editors;

/**
 * Nothing but a class to fill up the context info.
 * Importantly, the information here should be good and correct.
 * This is what we propagate to the user.
 * 
 * @author vikassingh
 *
 */
public class ContextInfoTexts {

	private static ContextInfoTexts INSTANCE = null;

	private ContextInfoTexts() {
		// nothing to do
	}

	public static ContextInfoTexts getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new ContextInfoTexts();
		}
		return INSTANCE;
	}

	// supported strings
	private final String[] replacements = { "omp", "parallel", "firstprivate",
			"lastprivate", "shared", "private", "default", "none", "reduction",
			"schedule", "dynamic", "guided", "runtime", "ordered", "sections",
			"section", "single", "master", "critical", "atomic", "flush",
			"barrier", "nowait", "threadprivate", "freeguithread", "gui" };

	public String[] getOMPInfo() {
		String[] info = {
				"<html><b>Defines a Pyjama directive.</b>"
						+ "<br>"
						+ "<br>"
						+ "These directives specify directives like OpenMP 2.5 specification."
						+ "</html>",

				"<html><b><font color=blue>Defines a <i>parallel<i> directive.</b></font>"
						+ "<br>"
						+ "<br>"
						+ "<font color=black>This is the fundamental construct that defines a parallel region,"
						+ "by using the parallel "
						+ "directive. The encountering thread creates a parallel region by spawning an "
						+ "additional zero or more threads, then becomes the master thread of the thread "
						+ "team and the code is executed by all the member threads, including the master."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>parallel for<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>for<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>parallel sections<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>sections<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>section<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>single<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>master<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>atomic<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>critical<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>threadprivate<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>freeguithread<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a <i>gui<i> directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a combines GUI-aware directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>",

				"<html><b><font color=blue>Defines a combines GUI-aware directive.</b></font>"
						+ "<br>"
						+ "<font color=black>These directives specify directives like OpenMP 2.5 specification."
						+ "</font></html>", };
		return info;
	}

	public String[] getOMPCompletions() {
		String[] completions = { "#omp", "#omp parallel", "#omp parallel for",
				"#omp for", "#omp parallel sections", "#omp sections",
				"#omp section", "#omp single", "#omp master", "#omp atomic",
				"#omp critical", "#omp threadprivate", "#omp freeguithread",
				"#omp gui", "#omp parallel freeguithread",
				"#omp parallel for freeguithread" };
		return completions;
	}

	private final String[] DEFAULT_REPLACE_STR = { "none", "shared" };

	public String[] getDefaultCompletions() {
		return DEFAULT_REPLACE_STR;
	}

	public String[] getDefaultInfo() {
		String[] info = {
				"<html><b>Defines 'none' as default type.</b>"
						+ "<br>"
						+ "<br>"
						+ "Specifies that all the data elements must be defined for their data types."
						+ "</html>",

				"<html><b>Defines 'shared' as default type.</b></font>"
						+ "<br>"
						+ "<br>"
						+ "Specifies that all the data elements are of shared data types."
						+ "</html>" };
		return info;
	}

	private final String[] REDUCTION_REPLACE_STR = { "+:", "-:", "*:", "&:",
			"|:", "^:", "&&:", "||:", "IntegerMAX:", "IntegerMIN:",
			"IntegerSUM:", "DoubleMAX:", "DoubleMIN:", "DoubleSUM:" };

	public String[] getReductionCompletions() {
		return REDUCTION_REPLACE_STR;
	}

	public String[] getReductionInfo() {
		String[] info = {
				"<html><b>Defines ADD operation.</b>"
						+ "</html>",

				"<html><b>Defines SUBTRACTION operation.</b></font>"
						+ "</html>",
				"<html><b>Defines MULTIPLICATION operation.</b></font>"
						+ "</html>",
				"<html><b>Defines BITWISE AND operation.</b></font>"
						+ "</html>",
				"<html><b>Defines BITWISE OR operation.</b></font>"
						+ "</html>",
				"<html><b>Defines XOR operation.</b></font>"
						+ "</html>",
				"<html><b>Defines Logical AND operation.</b></font>"
						+ "</html>",
				"<html><b>Defines Logical OR operation.</b></font>"
						+ "</html>",
				"<html><b>Defines MAX operation on Integer type of elements.</b></font>"
						+ "</html>",
				"<html><b>Defines MIN operation on Integer type of elements.</b></font>"
						+ "</html>",
				"<html><b>Defines SUM operation on Integer type of elements.</b></font>"
						+ "</html>",
				"<html><b>Defines MAX operation on Double type of elements.</b></font>"
						+ "</html>",
				"<html><b>Defines MIN operation on Double type of elements.</b></font>"
						+ "</html>",
				"<html><b>Defines SUM operation on Double type of elements.</b></font>"
						+ "</html>"};
		return info;
	}

	private final String[] SCHEDULE_REPLACE_STR = { "static, ", "dynamic, ",
			"guided, " };

	public String[] getScheduleCompletions() {
		return SCHEDULE_REPLACE_STR;
	}

	public String[] getScheduleInfo() {
		String[] info = {
				"<html><b>Defines 'static' scheduling for iterations.</b>"
						+ "<br>"
						+ "<br>"
						+ "The iterations are divided and statically assigned to the threads in a round robin manner."
						+ "</html>",

				"<html><b>Defines 'dynamic' scheduling for iterations.</b></font>"
						+ "<br>"
						+ "<br>"
						+ "Assignments to threads are done at runtime, and threads request new chunk once they complete execution."
						+ "</html>",

				"<html><b>Defines 'guided' scheduling for iterations.</b></font>"
						+ "<br>"
						+ "<br>"
						+ "Assignments is similar to dynamic, chunk-sizes are calculated based on remaining iterations."
						+ "</html>" };
		return info;
	}
	
	public String getRFForMessage(){
		return "Found a for loop without parallelisation. "
						+ "Please consider parallelising this with Pyjama directives.";
	}
	
	public String getRFGuiMessage(){
		return "Looks like this will block your GUI! "
						+ "Please consider using GUI-aware Pyjama directives.";
	}
}
