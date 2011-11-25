import java.io.File;
import javax.swing.filechooser.FileFilter;

public class PNGFileFilter
    extends FileFilter
{
    public PNGFileFilter()
    {
        super();
    }

    public boolean accept(File f)
    {
        return f.getName().toLowerCase().endsWith(".png");
    }

    public String getDescription()
    {
        return "PNG File Filter";
    }
}
