import java.io.IOException;
import java.io.OutputStream;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.Queue;

public class BankingPrinter 
    extends OutputStream
{
    private Queue<String> queue;
    private ArrayList<Byte> buffer;
    private boolean closed = false;

    public BankingPrinter(Queue<String> queue) {
        super();
        this.queue = queue;
        this.buffer = new ArrayList<Byte>();
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
        if (!queue.offer(new String(contents))) {
            throw new IOException();
        }
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
