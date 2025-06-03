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

            for (int i = 0; i < (n & 0x7F); i++) {
                if (position >= total) {
                    throw new IOException("Not enough space in the output buffer");
                }
                out[position++] = (byte) value;
            }
        } else {
            byte[] buffer = new byte[n];
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) {
                throw new EOFException("Unexpected end of input stream");
            }

            if (position + bytesRead > total) {
                throw new IOException("Not enough space in the output buffer");
            }

            System.arraycopy(buffer, 0, out, position, bytesRead);
            position += bytesRead;
        }
    }
}