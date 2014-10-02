package pjplugin.builder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

public class ErrorDetector {
	public String detect(IResource resource) throws Exception{

		File pjFile=resource.getLocation().toFile();
		BufferedReader br=new BufferedReader(new FileReader(pjFile));
		StringBuffer sb = new StringBuffer(pjFile.toString());
		sb.delete(sb.length() - 3, sb.length());
		sb.append(".java");
		String s1 = sb.toString();
		BufferedWriter bw=new BufferedWriter(new FileWriter(s1));
		String buffer=null;
		while((buffer=br.readLine())!=null){
			bw.write(buffer);
			bw.newLine();
		}		
		br.close();
		bw.close();
		return s1;
	}
	

}
