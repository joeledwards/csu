import javax.swing.JFrame;

public class Main
{
    public static void main(String[] args)
    {
        Window window = new Window("Print Test");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setVisible(true);
    }
}
