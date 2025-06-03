private static void decompress(final InputStream in, final byte[] out)
        throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();
        if (n == -1) {
            break; // End of input stream
        }

        if (n > 128) {
            // Run length encoding
            int value = in.read();
            for (int i = 0; i < (n & 0x7F); i++) {
                out[position++] = (byte) value;
            }
        } else {
            // Raw data
            byte[] buffer = new byte[n];
            int bytesRead = in.read(buffer, 0, n);
            if (bytesRead == -1) {
                break; // End of input stream
            }
            System.arraycopy(buffer, 0, out, position, bytesRead);
            position += bytesRead;
        }
    }
}