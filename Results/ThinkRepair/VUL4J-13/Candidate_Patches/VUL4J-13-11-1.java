private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read(); // Read the next byte

        if (n < 0) {
            throw new EOFException("Unexpected end of input stream");
        }

        if (n > 128) {
            int value = in.read();

            if (value < 0) {
                throw new EOFException("Unexpected end of input stream");
            }

            for (int i = 0; i < (n & 0x7F); i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n; i++) {
                int data = in.read();

                if (data < 0) {
                    throw new EOFException("Unexpected end of input stream");
                }

                out[position++] = (byte) data;
            }
        }
    }
}