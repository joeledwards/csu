import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author      Joel Edwards
 * @version     1.0
 *
 * The Regex class compiles a regular expression against which one or a Collection
 * of files can be matched repeatedly. The output will be printed to the supplied
 * PrintStream object, allowing output to be directed by the parent program.
 *
 */
public class Regex
{
    private Pattern pattern = null;
    private PrintStream outputStream = null;

    private boolean printCountsOnly = false;
    private boolean printFileNames = true;
    private boolean recursive = false;

    /**
     * Creates a new instance of the Regex class.
     *
     * @param patternText   The text to be compiled into the pattern for matching
     * @param outputStream  The PrintStream to which output should be sent
     * @param ignoreCase    Determines whether the matches should be case sensitive
     *
     * @throws PatternSyntaxException   If the patternText is not a valid regular 
     *                                  expression
     */
    public Regex(String patternText,
                 PrintStream outputStream,
                 boolean ignoreCase)
        throws PatternSyntaxException
    {
        int flags = 0; 
        if (ignoreCase) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        pattern = Pattern.compile(patternText, flags);
        this.outputStream = outputStream;
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
     * Checks every line in a file against the pattern, and prints out those which
     * match the pattern.
     *
     * @param file  The file to test for matches.
     *
     * @return  True if at least one match was found, otherwise false.
     */
    public boolean searchFile(File file)
    {
        if (file == null) {
            return false;
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

            // Open the file for reading.
            BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(
                                            new DataInputStream(
                                                new FileInputStream(file))));

            String fileNameString = "";
            if (printFileNames) {
                // Filename prefix for matched lines if enabled
                fileNameString = file.getName()+ ":";
            }

            // Read every line from the file and compare it against the pattern
            Matcher matcher = null;
            String line = reader.readLine();
            while (line != null) {
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    matchCount++;
                    if (!printCountsOnly) {
                        outputStream.printf("%s%s\n", fileNameString, line);
                    }
                }
                line = reader.readLine();
            }
            if (printCountsOnly) {
                outputStream.printf("%s%d\n", fileNameString, matchCount);
            }
        }
        catch (FileNotFoundException ex) {
            outputStream.printf("%s: file not found\n", file.getName());
        }
        catch (IOException ex) {
            outputStream.printf("%s: could not read from file\n", file.getName());
        }
        return matchCount > 0;
    }

    /**
     * Checks every line in a file against the pattern, and prints out those which
     * match the pattern.
     *
     * @param fileName  The name of the file to test for matches.
     *
     * @return  True if at least one match was found, otherwise false.
     */
    public boolean searchFile(String fileName)
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
    public boolean searchFiles(Collection<File> files)
    {
        boolean result = false;
        for (File file: files) {
            result = searchFile(file) || result;
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
    public boolean searchFilesByName(Collection<String> fileNames)
    {
        boolean result = false;
        for (String fileName: fileNames) {
            result = searchFile(fileName) || result;
        }
        return result;
    }
}

