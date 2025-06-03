private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    int n;

    while (position < total && (n = in.read()) != -1) {
        if (n > 128) {
            int value = in.read();

            if (value == -1) {
                throw new IOException("Unexpected end of input stream");
            }

            for (int i = 0; i < (n & 0x7f); i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n && position < total; i++) {
                int newValue = in.read();

                if (newValue == -1) {
                    throw new IOException("Unexpected end of input stream");
                }

                out[position++] = (byte) newValue;
            }
        }
    }

    if (position < total) {
        throw new IOException("Not enough data in input stream");
    }
}