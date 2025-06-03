private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();
        if (n < 0) {
            throw new EOFException("Unexpected end of input stream");
        }

        if (n > 128) {
            int value = in.read();
            if (value < 0) {
                throw new EOFException("Unexpected end of input stream");
            }

            n = n - 128; // Get the actual count of bytes to read

            for (int i = 0; i < n; i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n; i++) {
                int readByte = in.read();
                if (readByte < 0) {
                    throw new EOFException("Unexpected end of input stream");
                }
                out[position++] = (byte) readByte;
            }
        }
    }
}