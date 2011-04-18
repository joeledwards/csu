import java.lang.InterruptedException;
import java.util.concurrent.BlockingQueue;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;

public class QueueNotifier<E>
    extends Thread
{
    private Vector<E> buffer;
    private boolean running = false;

    private BlockingQueue<E> queue;
    private AbstractButton notifier;

    public QueueNotifier(BlockingQueue<E> queue, AbstractButton notifier) {
        this.queue = queue;
        this.notifier = notifier;
        this.buffer = new Vector<E>();
    }

    public E peek() {
        try {
            return buffer.get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public E take() 
    {
        try {
            return buffer.remove(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void halt(E force) {
        running = false;
        if (force != null) {
            queue.offer(force);
        }
    }

    public void run() {
        running = true;
        while (running) {
            try {
                E item = queue.take();
                buffer.add(item);
                SwingUtilities.invokeLater(new Clicker(notifier));
            } catch (InterruptedException e) {
                ;
            }
        }
    }
}
