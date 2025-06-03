private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();

        if (n == -1) {
            throw new IOException("Unexpected end of input stream");
        }

        if (n >= 0 && n <= 127) {
            for (int i = 0; i < n + 1; i++) {
                int value = in.read();
                if (value == -1) {
                    throw new IOException("Unexpected end of input stream");
                }

                if (position < total) {
                    out[position++] = (byte) value;
                } else {
                    throw new IOException("Output buffer overflow");
                }
            }
        } else {
            int value = in.read();
            if (value == -1) {
                throw new IOException("Unexpected end of input stream");
            }

            if (position < total) {
                for (int i = 0; i < (n & 0x7F) + 1; i++) {
                    out[position++] = (byte) value;
                }
            } else {
                throw new IOException("Output buffer overflow");
            }
        }
    }

    // Check for any remaining data in the input stream
    if (in.read() != -1) {
        throw new IOException("Input data remaining after decompression");
    }
}