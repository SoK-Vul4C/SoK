private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();
        if (n == -1) {
            throw new EOFException("Unexpected end of input stream");
        }

        if (n > 128) {
            int value = in.read();
            if (value == -1) {
                throw new EOFException("Unexpected end of input stream");
            }

            n &= 0x7F; // Masking the MSB to get the count
            if (position + n > total) {
                throw new IOException("Not enough space in the output buffer");
            }

            for (int i = 0; i < n; i++) {
                out[position++] = (byte) value;
            }
        } else {
            int bytesRead = in.read(out, position, n);
            if (bytesRead == -1) {
                throw new EOFException("Unexpected end of input stream");
            }
            
            position += bytesRead;
        }
    }
}