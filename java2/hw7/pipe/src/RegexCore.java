import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
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
    private BufferedReader inputStream = null;
    private PrintStream printStream = null;
    private PipedInputStream inputPipe = null;
    private PipedOutputStream sourcePipe = null;
    private PipedOutputStream finalPipe = null;

    private boolean printCountsOnly = false;
    private boolean finalStage = false;
    private boolean done = false;
    private String fileString = "";

    public RegexCore(Pattern pattern,
                     PipedOutputStream sourcePipe,
                     PrintStream printStream,
                     String fileName,
                     boolean printCountsOnly)
        throws IOException
    {
        this.pattern = pattern;
        this.sourcePipe = sourcePipe;
        this.printStream = printStream;
        this.printCountsOnly = printCountsOnly;
        if ((fileName != null) && (fileName.length() > 0)) {
            this.fileString = fileName + ": ";
        }

        inputPipe = new PipedInputStream((int)(1024*1024));
        inputPipe.connect(sourcePipe);
        inputStream = new BufferedReader(
                          new InputStreamReader(inputPipe));
        finalPipe = new PipedOutputStream();
    }

    public void run()
    {
        int matchCount = 0;
        Matcher matcher = null;
        String line = null;

        try {
            line = inputStream.readLine();
        }
        catch (IOException ex) {
            ;
        }
        while (!done) {
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

            try {
                line = inputStream.readLine();
            }
            catch (IOException ex) {
                if (!finalStage) {
                    // Connecte a local pipe in order to prevent errors
                    // when reading all the remaining data from the pipe.
                    try {
                        finalPipe.connect(inputPipe);
                        finalPipe.write((int)'\r');
                        finalPipe.write((int)'\n');
                        finalPipe.flush();
                        finalStage = true;
                    } catch (IOException ex2) {
                        done = true;
                    }
                }
            }
            // If we are disconnected from the feeder thread, and
            // there is not more data to read, we are done.
            try {
                if (finalStage && (inputPipe.available() < 1)) {
                    done = true;
                    finalPipe.close();
                    inputPipe.close();
                }
            } catch (IOException ex) {
                done = true;
            }
        }
        if (printCountsOnly) {
            printStream.printf("%s%d\n", fileString, matchCount);
        }
    }
}

