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

    public RegexCore(Pattern pattern,
                     InputStream inputStream,
                     PrintStream printStream,
                     String fileName,
                     boolean printCountsOnly)
        throws IOException
    {
        this.pattern = pattern;
        this.inputStream = inputStream;
        this.printStream = printStream;
        this.printCountsOnly = printCountsOnly;
        if ((fileName != null) && (fileName.length() > 0)) {
            this.fileString = fileName + ": ";
        }

        reader = new BufferedReader(
                     new InputStreamReader(inputStream));
    }

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

