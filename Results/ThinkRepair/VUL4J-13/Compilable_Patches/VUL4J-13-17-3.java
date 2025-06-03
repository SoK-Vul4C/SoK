import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();

        if (n == -1) {
            throw new EOFException("Premature end of stream");
        }

        if (n == 0) {
            throw new IOException("Invalid value read from input stream");
        }

        if (n > 0 && n <= 128) {
            for (int i = 0; i < n; i++) {
                int val = in.read();

                if (val == -1) {
                    throw new EOFException("Premature end of stream");
                }

                out[position++] = (byte) val;
            }
        } else {
            int value = in.read();

            if (value == -1) {
                throw new EOFException("Premature end of stream");
            }

            for (int i = 0; i < (n & 0x7F); i++) {
                out[position++] = (byte) value;
            }
        }
    }
}