import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author      Joel Edwards
 * @version     2.0
 *
 * The Regex class compiles a regular expression against which one or a Collection
 * of files can be matched repeatedly. The output will be printed to the supplied
 * PrintStream object, allowing output to be directed by the parent program.
 *
 */
public class Regex
{
    private PrintStream printStream = null;
    private RegexCore core = null;
    private Pattern pattern = null;

    private boolean printCountsOnly = false;
    private boolean printFileNames = true;
    private boolean recursive = false;

    /**
     * Creates a new instance of the Regex class.
     *
     * @param patternText   The text to be compiled into the pattern for matching
     * @param printStream   The PrintStream to which output should be sent
     * @param ignoreCase    Determines whether the matches should be case sensitive
     *
     * @throws PatternSyntaxException   If the patternText is not a valid regular 
     *                                  expression
     */
    public Regex(String patternText,
                 PrintStream printStream,
                 boolean ignoreCase)
        throws PatternSyntaxException
    {
        int flags = 0; 
        if (ignoreCase) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        pattern = Pattern.compile(patternText, flags);
        this.printStream = printStream;
    }


    /**
     * Enables or disables printing the file name as a prefix before the matched
     * lines.
     * Enabled by default.
     *
     * @param enable    True to enable printing of file names; false to disable.
     */
    public void setPrintFileNames(boolean enable)
    {
        printFileNames = enable;
    }

    /**
     * Enables or disables printing of match count for each file.
     * Disabled by default.
     *
     * @param enable    True to enable printing of counts per file;
     *                  false to disable.
     */
    public void setPrintCountsOnly(boolean enable)
    {
        printCountsOnly = enable;
    }

    /**
     * Enables or disables recursive directory processing.
     * Disabled by default.
     *
     * @param recursive True to enable recursive directory processing;
     *                  false to disable.
     */
    public void setRecursive(boolean recursive)
    {
        this.recursive = recursive;
    }


    /**
     * Reports whether file name printing is enabled.
     *
     * @return  True if file name printing is enabled, otherwise false.
     */
    public boolean getPrintFileNames()
    {
        return printFileNames;
    }

    /**
     * Reports whether match count printing is enabled.
     *
     * @return  True if match count printing is enabled, otherwise false.
     */
    public boolean getPrintCountsOnly()
    {
        return printCountsOnly;
    }

    /**
     * Reports whether directories should be processed recursively.
     *
     * @return  True if directories should be processed recursively,
     *          otherwise false.
     */
    public boolean getRecursive()
    {
        return recursive;
    }

    /**
     * Checks every line read from the stream against the pattern, and prints
     * out those which match the pattern.
     *
     * @param inputStream   The input stream from which to read lines.
     * @param fileName      Prefix each result with this file name.
     *                      No prefix if null or empty.
     *
     * @return  True if at least one match was found, otherwise false.
     */
    public int searchStream(InputStream inputStream,
                            String prefix)
    {
        int matchCount = 0;
        try {
            BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(inputStream));
            PipedOutputStream outputPipe = new PipedOutputStream();
            PipedInputStream inputPipe = new PipedInputStream(outputPipe);
            PrintStream outputStream = new PrintStream(outputPipe, true);
            RegexCore core = new RegexCore(pattern,
                                           inputPipe,
                                           System.out, 
                                           prefix,
                                           printCountsOnly);
            Thread coreThread = new Thread(core);
            coreThread.start();

            // Read every line from the file and pipe it to the core
            String line = reader.readLine();
            byte[] bytes = null;
            while (line != null) {
                // Write the line to the core for processing
                outputStream.println(line);
                outputPipe.flush();
                line = reader.readLine();
            }
            outputPipe.close();
            inputPipe.close();

            try {
                coreThread.join();
            }
            catch (InterruptedException ex) {
                printStream.printf("Input was interrupted.\n");
            }
        }
        catch (IOException ex) {
            ;
        }
        return matchCount;
    }

    /**
     * Checks every line in a file against the pattern, and prints out those which
     * match the pattern.
     *
     * @param file  The file to test for matches.
     *
     * @return  True if at least one match was found, otherwise false.
     */
    public int searchFile(File file)
    {
        if (file == null) {
            return 0;
        }

        int matchCount = 0;
        try {
            // If this is a directory, pass its contents onto the
            // searchFiles(Collection<File>) method, which will in turn
            // pass each file back here for processing, following all
            // directories until we have encountered every file in the
            // tree.
            if (file.isDirectory() && recursive) {
                File[] fileArray = file.listFiles();
                ArrayList<File> files = new ArrayList(fileArray.length);
                for (File subFile: fileArray) {
                    files.add(subFile);
                }
                return searchFiles(files);
            }

            // Pass opened file stream to searchStream method
            matchCount = searchStream(new DataInputStream(
                                          new FileInputStream(file)),
                                      printFileNames ? file.getName() : null);

        }
        catch (FileNotFoundException ex) {
            printStream.printf("%s: file not found\n", file.getName());
        }
        catch (IOException ex) {
            printStream.printf("%s: could not read from file\n", file.getName());
        }
        return matchCount;
    }

    /**
     * Checks every line in a file against the pattern, and prints out those which
     * match the pattern.
     *
     * @param fileName  The name of the file to test for matches.
     *
     * @return  True if at least one match was found, otherwise false.
     */
    public int searchFile(String fileName)
    {
        return searchFile(new File(fileName));
    }

    /**
     * Checks every line in each of the supplied files against the pattern, and 
     * prints out those which match the pattern.
     *
     * @param files  The files to test for matches.
     *
     * @return  True if at least one match was found, otherwise false.
     */
    public int searchFiles(Collection<File> files)
    {
        int result = 0;
        for (File file: files) {
            result += searchFile(file);
        }
        return result;
    }

    /**
     * Checks every line in each of the specified files against the pattern, and 
     * prints out those which match the pattern.
     *
     * @param fileNames  The names of the files to test for matches.
     *
     * @return  True if at least one match was found, otherwise false.
     */
    public int searchFilesByName(Collection<String> fileNames)
    {
        int result = 0;
        for (String fileName: fileNames) {
            result += searchFile(fileName);
        }
        return result;
    }
}

