private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();
        
        if (n == -1) {
            // End of stream, break out of the loop
            break;
        }

        if (n > 128) {
            int value = in.read();

            for (int i = 0; i < (n & 0x7f); i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n; i++) {
                int byteValue = in.read();

                if (byteValue == -1) {
                    // End of stream, break out of the loop
                    break;
                }

                out[position++] = (byte) byteValue;
            }
        }
    }
}