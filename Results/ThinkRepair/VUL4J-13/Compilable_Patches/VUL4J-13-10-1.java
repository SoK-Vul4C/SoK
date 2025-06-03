private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[total];

    while (position < total) {
        int n = in.read(buffer, 0, Math.min(total - position, buffer.length)); 

        if (n < 0) {
            // Handle end of input stream
            break;
        }

        int i = 0;
        while (i < n) {
            int value = buffer[i] & 0xFF;

            if (value > 128) {
                value = in.read() & 0xFF;
                for (int j = 0; j < (value & 0x7F); j++) {
                    out[position++] = (byte) value;
                }
            } else {
                for (int j = 0; j < value; j++) {
                    out[position++] = (byte) in.read();
                }
            }
            i++;
        }
    }
}