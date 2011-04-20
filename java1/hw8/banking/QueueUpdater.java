import java.lang.InterruptedException;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class QueueUpdater
    extends SwingWorker<JTextArea, String>
{
    private boolean running = false;
    private BlockingQueue<String> queue;
    private JTextArea viewArea = null;

    public QueueUpdater(BlockingQueue<String> queue, JTextArea viewArea) {
        this.queue = queue;
        this.viewArea = viewArea;
    }

    protected void process(List<String> chunks) {
        for (String chunk: chunks) {
            viewArea.append(chunk);
        }
    }

    protected JTextArea doInBackground() {
        running = true;
        try {
            while (running) {
                publish(queue.take());
            }
        } catch (InterruptedException e) {;}
        return viewArea;
    }

    public void halt() {
        running = false;
        queue.offer("");
    }
}
