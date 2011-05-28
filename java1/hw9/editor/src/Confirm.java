import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Confirm
implements ActionListener
{
    private String title = null;
    private String question = null;

    public Confirm(String title, String question)
    {
        this.title = title;
        this.question = question;
    }

    public void actionPerformed(ActionEvent evt) {
        JOptionPane confirm = new JOptionPane(title,
                                              JOptionPane.QUESTION_MESSAGE,
                                              JOptionPane.YES_NO_OPTION,
                                              UIManager.getIcon("OptionPane.questionIcon"));
        JDialog dialog = confirm.createDialog(question);
        dialog.setVisible(true);
        Object selectedValue = confirm.getValue();
        if (selectedValue != null) {
            int value = (Integer)selectedValue;
            if (value == JOptionPane.YES_OPTION) {
                // Close Tab
            }
        }
    }
}
