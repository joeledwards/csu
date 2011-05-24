import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Summarizer
{
 // Program Static Methods
    public static void main(String[] args)
    {
        if (args.length < 1) {
            usage("No file name supplied.");
        }
        if (args.length > 1) {
            usage("Too many arguments.");
        }

        File file = new File(args[0]);
        if (!file.exists()) {
            error("File '" +file+ "' does not exist.");
        }
        if (!file.isFile()) {
            error("Path '" +file+ "' does not refer to a normal file.");
        }
        if (!file.canRead()) {
            error("File '" +file+ "' is not readable.");
        }

        try {
            Summarizer engine = new Summarizer(file);
            engine.generateSummary();
            System.out.println(engine.getSummary());
        } catch (IOException e) {
            error("Reading of file '" +file+  "' failed.");
        }
    }

    public static void usage() 
    {
        error(null, true);
    }
    
    public static void usage(String message)
    {
        error(message, true);
    }

    public static void error(String message)
    {
        error(message, false);
    }

    public static void error(String message, boolean showUsage)
    {
        if ((message != null) && (message.length() > 0)) {
            System.out.println("E: " + message);
        }
        if (showUsage) {
            System.out.println("Usage: summarizer <file_name>");
            System.out.println("       file_name - the text file to summarize");
        }
        System.exit(1);
    }

 // Class Data
    private StringBuffer summary;
    private File file;

    private String longestWord = null;
    private String shortestWord = null;
    private int lineCount = 0;
    private int wordCount = 0;

 // Class Methods
    public Summarizer(File file)
    {
        summary = new StringBuffer();
        this.file = file;
    }

    public void generateSummary() 
    throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file))));
        StringTokenizer tokenizer = null;
        String line = reader.readLine();
        String token = null;
        while (line != null) {
            tokenizer = new StringTokenizer(line);
            lineCount++;
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                if ((longestWord == null) || (longestWord.length() < token.length())) {
                    longestWord = token;
                }
                if ((shortestWord == null) || (shortestWord.length() > token.length())) {
                    shortestWord = token;
                }
                wordCount++;
            }
            line = reader.readLine();
        }

        summary.append("Summary for file '" +file+ "'\n");
        summary.append("  Lines:         " +lineCount+ "\n");
        summary.append("  Words:         " +wordCount+ "\n");
        summary.append("  Shortest Word: '" +shortestWord+ "' (" +shortestWord.length()+ " characters)\n");
        summary.append("  Longest Word:  '" +longestWord+ "' (" +longestWord.length()+ " characters)\n");
    }

    public String getSummary()
    {
        return summary.toString();
    }
}
