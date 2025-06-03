private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    while (position < total) {
        int n = in.read();
        
        if (n == -1) {
            break; // Exit the loop if end of stream is reached
        }

        if (n > 128) {
            int value = in.read();

            if (value == -1) {
                break; // Handle premature end of stream
            }

            for (int i = 0; i < (n & 0x7f); i++) {
                out[position++] = (byte) value;
            }
        } else {
            for (int i = 0; i < n; i++) {
                int data = in.read();
                
                if (data == -1) {
                    break; // Handle premature end of stream
                }
                
                out[position++] = (byte) data;
            }
        }
    }
}