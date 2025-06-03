private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        final int n = in.read();

        if (n == -1) {
            throw new IOException("Unexpected end of input stream");
        }

        if (n >= 0 && n <= 127) {
            for (int i = 0; i < n + 1; i++) {
                int value = in.read();
                if (value == -1) {
                    throw new IOException("Unexpected end of input stream");
                }
                out[position++] = (byte) value;
            }
        } else {
            int value = in.read();
            if (value == -1) {
                throw new IOException("Unexpected end of input stream");
            }

            for (int i = 0; i < (n & 0x7f) + 1; i++) {
                out[position++] = (byte) value;
            }
        }
    }
}