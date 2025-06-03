private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();

        if (n == -1) {
            throw new IOException("Unexpected end of input stream");
        }

        if (n == 0) {
            break; // End of data
        }

        if (n >= 0 && n <= 127) {
            for (int i = 0; i < n; i++) {
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
                for (int i = 0; i < (n & 0x7F); i++) {
                    out[position++] = (byte) value;
                }
            } else {
                throw new IOException("Output buffer overflow");
            }
        }
    }

    // Check if the output buffer is not completely filled
    if (position < total) {
        throw new IOException("Not enough data for decompression");
    }

    // Check for any remaining data in the input stream
    if (in.read() != -1) {
        throw new IOException("Input data remaining after decompression");
    }
}