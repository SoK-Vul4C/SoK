private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0; 
    final int total = out.length;

    while (position < total) {
        int n = in.read(); // Read the count of bytes to decompress

        if (n < 0) {
            break; // End of stream
        }

        if (n == 128) {
            n = in.read(); // Read the actual count of bytes to decompress
            if (n < 0) {
                break; // End of stream
            }
            n += 128;
        }

        for (int i = 0; i < n; i++) {
            int value = in.read();
            if (value < 0) {
                break; // End of stream
            }
            out[position++] = (byte) value;
        }
    }
}