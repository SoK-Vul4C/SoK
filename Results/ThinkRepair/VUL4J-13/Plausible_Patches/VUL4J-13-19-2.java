private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();

        if (n == -1) {
            break; // End of input stream
        }

        if (n > 128) {
            int value = in.read();

            for (int i = 0; i < (n & 0x7F) && position < total; i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n && position < total; i++) {
                int data = in.read();
                out[position++] = (byte) data;
            }
        }
    }
}