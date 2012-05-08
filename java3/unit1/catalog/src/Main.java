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
        StreamSource[] schemas = new StreamSource[] { new StreamSource( new File("Publication.xsd")),
                                                       new StreamSource( new File("Catalog.xsd"))
                                                    };
        String catalog = "catalog.xml";

        XMLValidator reader = new XMLValidator(schemas);
        reader.validate(catalog);

    }

    public static void error(String message)
    {
        System.err.println("E: " +message);
    }
}

