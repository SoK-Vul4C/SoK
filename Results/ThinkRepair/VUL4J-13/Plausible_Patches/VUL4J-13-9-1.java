private static void decompress(final InputStream in, final byte[] out)
        throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[1024]; // Buffer size can be adjusted for performance
   
    while (position < total) {
        int n = in.read();
        
        if (n < 0) { // Check for end of stream
            break;
        }

        if (n > 128) {
            int value = in.read();

            for (int i = 0; i < (n & 0x7f); i++) {
                out[position++] = (byte) value;
            }
        } else {
            int bytesRead = in.read(buffer, 0, Math.min(n, total - position));
            if (bytesRead == -1) {
                break;
            }

            System.arraycopy(buffer, 0, out, position, bytesRead);
            position += bytesRead;
        }
    }
}