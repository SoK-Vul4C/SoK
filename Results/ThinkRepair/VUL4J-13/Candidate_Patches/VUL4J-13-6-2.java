private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    byte[] buffer = new byte[1024]; // Buffer for reading input bytes

    while (position < total) {
        int n = in.read(buffer); // Read a block of bytes into the buffer

        if (n == -1) {
            throw new EOFException("Unexpected end of input stream");
        }

        int index = 0;

        while (index < n && position < total) {
            int value = buffer[index] & 0xFF; // Convert to unsigned int
            index++;

            if (value > 128) {
                if (index == n) {
                    value = in.read();
                    if (value == -1) {
                        throw new EOFException("Unexpected end of input stream");
                    }
                }
                for (int j = 0; j < (value & 0x7F) && position < total; j++) {
                    out[position++] = (byte) value;
                }
            } else {
                for (int j = 0; j < value && position < total; j++) {
                    if (index == n) {
                        value = in.read();
                        if (value == -1) {
                            throw new EOFException("Unexpected end of input stream");
                        }
                        index = 0;
                    }
                    out[position++] = (byte) buffer[index++];
                }
            }
        }
    }
}