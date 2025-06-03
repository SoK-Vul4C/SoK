private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        final int n = in.read();

        if (n < 0) {
            break; // End of stream
        }

        if (n >= 0 && n <= 127) {
            for (int i = 0; i < n + 1; i++) {
                int value = in.read();
                if (value < 0) {
                    break; // End of stream
                }
                out[position++] = (byte) value;
            }
        } else {
            int value = in.read();
            if (value < 0) {
                break; // End of stream
            }

            for (int i = 0; i < (n & 0x7F) + 1; i++) {
                out[position++] = (byte) value;
            }
        }
    }
}