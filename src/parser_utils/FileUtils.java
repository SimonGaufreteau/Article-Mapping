package parser_utils;

import java.io.*;

/**
 * Static utils class for a File. Mainly used to get the content of a file easily.
 * @author GAUFRETEAU Simon
 */
public class FileUtils {
	private FileUtils(){}

	/**
	 * Returns the content of a file given by its path. Uses {@link BufferedReader} to read the file.
	 */
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
