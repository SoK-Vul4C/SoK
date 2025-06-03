private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    byte[] scanLineStart = new byte[] { 0x02, 0x02 }; // Expected start of each scan line

    // Read first scan line
    for (int i = 0; i < scanLineStart.length; i++) {
        int value = in.read();
        if (value != scanLineStart[i]) {
            throw new IOException("Scan line does not start with expected values");
        }
        out[position++] = (byte) value;
        if (position >= total) {
            return; // Filled the output buffer
        }
    }

    while (position < total) {
        int n = in.read();
        if (n == -1) {
            break; // Reached end of input stream
        }

        for (int i = 0; i < n; i++) {
            int value = in.read();
            out[position++] = (byte) value;
            if (position >= total) {
                break; // Filled the output buffer
            }
        }
    }
}