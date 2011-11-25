import java.awt.Image;
import javax.swing.ImageIcon;                                                                  

public class Resources {
    public static ImageIcon getAsImageIcon(String name, int height, int width) {
        return new ImageIcon((new ImageIcon(ClassLoader.getSystemResource(name))).getImage().getScaledInstance(height, width, Image.SCALE_SMOOTH));
    }
}

