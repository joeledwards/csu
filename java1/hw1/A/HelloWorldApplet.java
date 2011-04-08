import java.applet.Applet;
import java.awt.Graphics;

public class HelloWorldApplet
    extends Applet
{
    public void paint(Graphics g) {
        g.drawString("Hello World!",    50, 25);
        g.drawString("Good bye World!", 50, 50);
    }
}
