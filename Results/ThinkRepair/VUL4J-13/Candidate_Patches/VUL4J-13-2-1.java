private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[1024]; // buffer size can be adjusted for performance

    while (position < total) {
        int n = in.read();

        if (n == -1) {
            throw new IOException("Unexpected end of stream");
        }

        if (n > 128) {
            int value = in.read();

            if (value == -1) {
                throw new IOException("Unexpected end of stream");
            }

            for (int i = 0; i < (n & 0x7f); i++) {
                out[position++] = (byte) value;
            }
        } else {
            int bytesRead = in.read(buffer, 0, Math.min(n, buffer.length));
            if (bytesRead == -1) {
                throw new IOException("Unexpected end of stream");
            }

            System.arraycopy(buffer, 0, out, position, bytesRead);
            position += bytesRead;
        }
    }
}