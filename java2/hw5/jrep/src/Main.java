import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.PatternSyntaxException;
import java.util.Set;
import java.util.TreeSet;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main
{
    public static String programName = "";
    public static OptionParser parser = null;

    public static void main(String argv[])
    {
        parser = new OptionParser() {
            {
                accepts("c", "Print number of matches in each file instead of printing each matching line");
                accepts("e", "Extended search pattern support");
                accepts("h", "Prints this help message");
                accepts("i", "Ignore case");
                accepts("r", "Recrusively search directories");
            }
        };

        programName = "jrep.jar";
        OptionSet options = parser.parse(argv);

        if (options.has("h")) {
            usage();
        }
        // Assemble list of files

        boolean countsOnly = options.has("c");
        boolean extended   = options.has("e");
        boolean ignoreCase = options.has("i");
        boolean recursive  = options.has("r");
        boolean fileNames  = true;

        List<String> args = options.nonOptionArguments();

        if (args.size() < 2) {
            usage();
        }

        String searchText = args.get(0);

        ArrayList<File> files = new ArrayList<File>(256);
        for (int i = 1; i < args.size(); i++) {
            files.add(new File(args.get(i)));
        }

        if ((files.size() <= 1) && (!files.get(0).isDirectory())) {
            fileNames = false;
        }

        try {
            Regex reg = new Regex(searchText, System.out, ignoreCase, extended);
            reg.setPrintCountsOnly(countsOnly);
            reg.setPrintFileNames(files.size() > 1);
            reg.setRecursive(recursive);

            reg.searchFiles(files);
        }
        catch (PatternSyntaxException ex) {
            usage("Invalid Search Pattern Syntax");
        }
    }

    public static void usage()
    {
        System.out.println("Usage: " +programName+ " [Options] <search-pattern> <files1> [files2]...\n");
        try {
            parser.printHelpOn(System.out);
        }
        catch (IOException ex) {
            System.out.println("Failed to print usage due to an I/O error. Details: " +ex);
        }
        System.exit(1);
    }

    public static void usage(String message)
    {
        error(message);
        usage();
    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

