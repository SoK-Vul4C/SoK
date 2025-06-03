private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();

        if (n < 0) {
            break;
        }

        if (n >= 0 && n <= 128) {
            for (int i = 0; i < n; i++) {
                int value = in.read();
                if (value < 0) {
                    break;
                }
                out[position++] = (byte) value;
            }
        } else {
            int value = in.read();
            if (value < 0) {
                break;
            }
            for (int i = 0; i < (n & 0x7F); i++) {
                out[position++] = (byte) value;
            }
        }
    }
}