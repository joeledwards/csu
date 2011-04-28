import java.awt.Label;

public class ScrollingLabel
extends Label
implements Runnable
{
    public static final long serialVersionUID = 1L;

    private int delay = 1000;
    private boolean running = false;
    private boolean playing = false;
    private Thread owner = null;

    public ScrollingLabel(String text, int delay) {
        super(text);
        this.delay = delay;
    }

    public void setOwner(Thread owner) {
        this.owner = owner;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Thread getOwner() {
        return owner;
    }

    public int getDelay() {
        return delay;
    }

    public void run() {
        running = true;
        while (running) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {;}
            if (playing) {
                setScrollingText(null);
            }
        }
    }

    public synchronized void setScrollingText(String text) {
        if (text == null) {
            String previous = getText();
            setText(previous.substring(1) + previous.substring(0,1));
        } else {
            setText(text);
        }
    }

    public void play() {
        playing = true;
    }

    public void pause() {
        playing = false;
    }

    public void halt() {
        running = false;
        if (owner != null) {
            owner.interrupt();
        }
    }
}
