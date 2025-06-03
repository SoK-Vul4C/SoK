private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    int n;
    while ((n = in.read()) >= 0 && position < total) {
        if (n > 128) {
            int value = in.read();
            if (value < 0) {
                break; // Handle end of stream
            }
            for (int i = 0; i < (n & 0x7F) && position < total; i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n && position < total; i++) {
                int value = in.read();
                if (value < 0) {
                    break; // Handle end of stream
                }
                out[position++] = (byte) value;
            }
        }
    }
}