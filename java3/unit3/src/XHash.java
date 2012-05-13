import java.util.Arrays;

public class XHash
{
    private byte[] xbytes = null;

    public void update(byte[] data, int offset, int length)
    {
        byte[] temp = null;
        byte[] shorter = null;

        if (length > 0) {
            if (data != null) {
                temp = Arrays.copyOfRange(data, offset, offset + length);
            }
            if (xbytes == null) {
                xbytes = temp;
            }
            else if (temp != null) {
                int min = Math.min(temp.length, xbytes.length);
                if (temp.length > xbytes.length) {
                    shorter = xbytes;
                    xbytes = temp;
                }
                else {
                    shorter = temp;
                }

                for (int i = 0; i < min; i++) {
                    xbytes[i] = (byte)((xbytes[i] ^ shorter[i]) & 0xFF);
                }
            }
        }
    }

    public byte[] hash()
    {
        return xbytes;
    }
}

