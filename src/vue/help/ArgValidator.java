package vue.help;

import parser_utils.Pair;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A class used to validate the inputs given while running this project.
 * This class and its methods are used in {@link vue.graph.MainGraph} and {@link vue.Main}
 */
public class ArgValidator {
	private final static String HELP_MESSAGE = "To use this class, please use one the the following options :\n\n --help / -h :\n Displays the help for this command\n\n --url / -u :\nDisplays the result for the URL given right after this option. Example : -u https://fr.wikipedia.org/wiki/Pachtounes\n\n -- file / -f :\nDisplays the result for the given filePath. Example : -f C:/Users/myName/testfile.txt\n\n--example / -e :\nShows an example of the results of this program.";
	private final static String ERROR_MESSAGE = "Error detected in the arguments. Please check the parameters called (you may have provided too many, please give only one option and only one file if required).\nFor further explanation please use the --help or -h option.";
	private final static String UNRECOGNIZED_OPTION_MESSAGE = "Error detected in the arguments. Please check the syntax of the argument given.\n See --help or -h for further details on the correct arguments.";

	private ArgValidator(){}

	public static Pair<String,String> validateArgs(String[] args) throws Exception {
		String option = args[0];
		if (option.equals("--help") || option.equals("-h")) {
			System.out.println(HELP_MESSAGE);
			return null;
		}else if(option.equals("--example")|| option.equals("-e")){
			return new Pair<>("example","example");
		}else if (args.length >= 2) {
			if (option.equals("--url") || option.equals("-u"))
				return new Pair<>("url", args[1]);

			else if (option.equals("--file") || option.equals("-f"))
				return new Pair<>("file", args[1]);

			else {
				System.out.println(UNRECOGNIZED_OPTION_MESSAGE);
				return null;
			}
		} else {
			throw new Exception(ERROR_MESSAGE);
		}
	}
}
