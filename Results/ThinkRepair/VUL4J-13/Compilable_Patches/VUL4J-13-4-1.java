private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[1024]; // Buffer size can be adjusted for performance

    while (position < total) {
        int n = in.read(buffer);
        if (n == -1) {
            break; // Reached end of input stream
        }

        for (int i = 0; i < n; i++) {
            int value = buffer[i];

            if (value > 128) {
                value = in.read();
                i++; // Move to next byte in the buffer
                for (int j = 0; j < (value & 0x7f); j++) {
                    out[position++] = (byte) buffer[i];
                }
            } else {
                out[position++] = (byte) value;
            }

            if (position >= total) {
                break; // Filled the output buffer
            }
        }
    }
}