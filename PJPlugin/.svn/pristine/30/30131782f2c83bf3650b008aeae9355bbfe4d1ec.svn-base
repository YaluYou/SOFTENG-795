
package pjplugin.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
	private InputStream fInputStream;
	private String fType;
	
	private String fMsg;
	public StreamGobbler(InputStream inputStream, String type) {
//		System.out.println("String Gobbler is used");//wk
		this.fInputStream = inputStream;
		this.fType = type;
	}
	
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(fInputStream);
			BufferedReader buf = new BufferedReader(isr);
			String line = null;
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = buf.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			fMsg = sb.toString();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public String getMsg() {
		return fMsg;
	}
	
	public String getType() {
		return fType;
	}
}
