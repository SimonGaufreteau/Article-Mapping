package parser_utils;

import java.io.*;

public class FileUtils {
	private FileUtils(){}

	public static String getFileContent(String filepath) throws IOException {
		File file = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder s = new StringBuilder();
		String line = br.readLine();
		while(line!=null){
			s.append(line);
			line = br.readLine();
		}
		return s.toString();
	}
}
