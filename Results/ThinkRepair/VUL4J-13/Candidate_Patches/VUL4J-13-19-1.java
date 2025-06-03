private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[1024]; // Read in chunks of 1024 bytes

    while (position < total) {
        int n = in.read(buffer); // Read into the buffer

        if (n == -1) {
            break; // End of input stream
        }

        for (int i = 0; i < n && position < total; i++) {
            out[position++] = buffer[i];
        }
    }
}