import java.io.IOException;
import java.io.OutputStream;
import java.lang.StringBuffer;
import java.util.Queue;
import java.util.Vector;

public class BankingPrinter 
    extends OutputStream
{
    private Queue<String> queue;
    private Vector<Byte> buffer;
    private boolean closed = false;
    private boolean mirrorToStdout = false;

    public BankingPrinter(Queue<String> queue, boolean mirrorToStdout) {
        super();
        this.queue = queue;
        this.buffer = new Vector<Byte>();
        this.mirrorToStdout = mirrorToStdout;
    }

    public void setQueue(Queue<String> queue) {
        this.queue = queue;
    }

    public Queue<String> getQueue() {
        return queue;
    }

    private void testClosed() throws IOException {
        if (closed) {
            throw new IOException("BankingPrinter has been closd()");
        }
    }

    public void close() throws IOException {
        testClosed();
        buffer = null;
        closed = true;
    }

    public void flush() throws IOException {
        testClosed();
        byte[] contents = new byte[buffer.size()];
        for (int i=0; i < contents.length; i++) {
            contents[i] = buffer.get(i);
        }
        String printString = new String(contents);
        boolean queued = queue.offer(new String(contents));
        if (!queued) {
            throw new IOException();
        }
        System.out.print(printString);
        buffer.clear();
    }

    public void write(byte[] str) throws IOException {
        testClosed();
        for (Byte b: str) {
            buffer.add(b);
        }
    }

    public void write(byte[] str, int off, int len) throws IOException {
        testClosed();
        for (int i=0; i < len; i++) {
            buffer.add(str[off+i]);
        }
    }

    public void write(int b) throws IOException {
        testClosed();
        buffer.add((byte)b);
    }
} // class BankingPrinter
