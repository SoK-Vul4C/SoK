private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    byte[] scanLineStart = new byte[] { 0x02, 0x02 }; // Expected start of each scan line
    int scanLineStartIndex = 0; // Index to track the expected scan line start position

    while (position < total) {
        int value = in.read();
        if (value == -1) {
            throw new IOException("Unexpected end of input stream");
        }

        if (value == scanLineStart[scanLineStartIndex]) {
            out[position++] = (byte) value;
            if (scanLineStartIndex == scanLineStart.length - 1) {
                scanLineStartIndex = 0; // Reset the scan line start index
            } else {
                scanLineStartIndex++;
            }
        } else {
            throw new IOException("Scan line does not start with expected values");
        }

        if (position >= total) {
            break; // Filled the output buffer
        }
    }

    while (position < total) {
        int n = in.read();
        if (n == -1) {
            break; // Reached end of input stream
        }

        for (int i = 0; i < n; i++) {
            int value = in.read();
            if (value == -1) {
                throw new IOException("Unexpected end of input stream");
            }
            out[position++] = (byte) value;
            if (position >= total) {
                break; // Filled the output buffer
            }
        }
    }
}