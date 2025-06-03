private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    byte[] buffer = new byte[1024]; // Buffer for reading input bytes

    while (position < total) {
        int n = in.read(buffer); // Read a block of bytes into the buffer

        if (n == -1) {
            throw new EOFException("Unexpected end of input stream");
        }

        for (int i = 0; i < n && position < total; i++) {
            int value = buffer[i] & 0xFF; // Convert to unsigned int
            if (value > 128) {
                value = in.read();
                if (value == -1) {
                    throw new EOFException("Unexpected end of input stream");
                }
                for (int j = 0; j < (value & 0x7F) && position < total; j++) {
                    out[position++] = (byte) value;
                }
            } else {
                out[position++] = (byte) value;
            }
        }
    }
}