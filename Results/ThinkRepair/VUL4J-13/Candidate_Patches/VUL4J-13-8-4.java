import org.apache.commons.imaging.ImageReadException;

private static void decompress(final InputStream in, final byte[] out) throws ImageReadException {
    int position = 0;
    final int total = out.length;

    int n;

    try {
        while (position < total && (n = in.read()) != -1) {
            if (n > 128) {
                int value = in.read();

                if (value == -1) {
                    throw new ImageReadException("Unexpected end of input stream");
                }

                for (int i = 0; i < (n & 0x7f); i++) {
                    if (position < total) {
                        out[position++] = (byte) value;
                    } else {
                        throw new ImageReadException("Not enough space in output array");
                    }
                }
            } else {
                for (int i = 0; i < n && position < total; i++) {
                    int newValue = in.read();

                    if (newValue == -1) {
                        throw new ImageReadException("Unexpected end of input stream");
                    }

                    out[position++] = (byte) newValue;
                }
            }
        }

        if (position < total) {
            throw new ImageReadException("Not enough data in input stream");
        }
    } catch (IOException e) {
        throw new ImageReadException("Error occurred during decompression", e);
    }
}