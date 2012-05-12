import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.transform.stream.StreamSource;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main
{
    private static final Logger logger = Logger.getLogger("Main");

    public static void main(String argv[])
    {
        String order = "order.xml";
        String domDigest;
        String saxDigest;

        DOMReader dom = new DOMReader();
        SAXReader sax = new SAXReader();

        // DOM parser and analysis
        domDigest = dom.read(order);
        System.out.printf("\n");

        // SAX perser and analysis
        saxDigest = sax.read(order);
        System.out.printf("\n");

        // Document parse mode comparison
        System.out.printf("DOMReader SHA-1 Digest [%s]\n", domDigest);
        System.out.printf("SAXReader SHA-1 Digest [%s]\n", saxDigest);
    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

