import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author      Joel Edwards
 * @version     1.0
 *
 * The RegexCore class matches a pattern against lines read from a stream,
 * and prints the matched lines or a summary on another stream.
 *
 */
public class RegexCore
    implements Runnable
{
    private Pattern pattern = null;
    private BufferedReader reader = null;
    private InputStream inputStream = null;
    private PrintStream printStream = null;
    private PipedInputStream inputPipe = null;
    private PipedOutputStream finalPipe = null;

    private boolean printCountsOnly = false;
    private boolean finalStage = false;
    private String fileString = "";

    /**
     * Creates a new instance of the RegexCore class.
     *
     * @param pattern           the pattern to be used for matching
     * @param inputStream       the InputStream from which lines will be read
     * @param printStream       the PrintStream to which output should be sent
     * @param prefix            an optional prefix to print before each matched 
     *                          line. (null or empty String for none)
     * @param printCountsOnly   true to print only the number of lines matched,
     *                          otherwise false.
     *
     * @throws IOException      if a reader could not be attached to inputStream.
     *
     */
    public RegexCore(Pattern pattern,
                     InputStream inputStream,
                     PrintStream printStream,
                     String prefix,
                     boolean printCountsOnly)
        throws IOException
    {
        this.pattern = pattern;
        this.inputStream = inputStream;
        this.printStream = printStream;
        this.printCountsOnly = printCountsOnly;
        if ((prefix != null) && (prefix.length() > 0)) {
            this.fileString = prefix + ": ";
        }

        reader = new BufferedReader(
                     new InputStreamReader(inputStream));
    }

    /**
     * Implements the Runnable interface's run() method.
     * This is where the "magic" happens.
     */
    public void run()
    {
        int matchCount = 0;
        Matcher matcher = null;
        String line = null;
        boolean done = false;

        while (!done) {
            try {
                line = reader.readLine();
                if (line != null) {
                    matcher = pattern.matcher(line);
                    // Search for matches in this line.
                    if (matcher.find()) {
                        matchCount++;
                        if (!printCountsOnly) {
                            printStream.printf("%s%s\n", fileString, line);
                        }
                    }
                }
            }
            catch (IOException ex) {
                done = true;
            }
        }

        if (printCountsOnly) {
            printStream.printf("%s%d\n", fileString, matchCount);
        }
    }
}

