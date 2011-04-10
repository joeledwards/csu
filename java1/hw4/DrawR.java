import java.awt.*;
import java.applet.Applet;
public class DrawR extends Applet{
    Rectangle r1=new Rectangle(50,75,Color.blue);
    Rectangle r2=new Rectangle(60,80,Color.red);
    public void paint(Graphics g) {
        r1.drawAt(g,10,10);
        r2.drawAt(g,100,100);
        g.drawLine(100,100,200,200);
    }

}
