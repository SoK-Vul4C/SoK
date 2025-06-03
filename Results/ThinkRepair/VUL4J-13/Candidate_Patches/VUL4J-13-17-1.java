private static void decompress(final InputStream in, final byte[] out)
        throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        final int n = in.read();
        if (n == -1) {
            throw new EOFException("Premature end of stream");
        }

        if (n < 0) {
            throw new IOException("Invalid value read from input stream");
        }

        if (n > 128) {
            int value = in.read();
            if(value == -1){
                throw new EOFException("Premature end of stream");
            }
            for (int i = 0; i < (n & 0x7f); i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n; i++) {
                int val = in.read();
                if (val == -1) {
                    throw new EOFException("Premature end of stream");
                }
                out[position++] = (byte) val;
            }
        }
    }
}