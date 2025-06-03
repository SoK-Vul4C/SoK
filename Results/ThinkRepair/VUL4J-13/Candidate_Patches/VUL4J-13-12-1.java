private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[1024]; // Buffer to read multiple bytes at once

    while (position < total) {
        final int n = in.read(buffer);

        if (n == -1) {
            break; // End of input stream reached
        }

        for (int i = 0; i < n; i++) {
            if (buffer[i] > 0) {
                final int value = in.read();

                for (int j = 0; j < (buffer[i] & 0x7f); j++) {
                    out[position++] = (byte) value;
                }
            } else {
                for (int j = 0; j < buffer[i] & 0x7f; j++) {
                    out[position++] = (byte) in.read();
                }
            }
        }
    }
}