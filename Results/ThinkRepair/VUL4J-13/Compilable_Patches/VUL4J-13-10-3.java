private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[1024]; // Buffer size can be adjusted
    
    while (position < total) {
        int n = in.read(buffer);

        if (n == -1) {
            break; // End of input stream
        }
        
        int i = 0;
        while (i < n && position < total) {
            int value = buffer[i] & 0xFF;
            i++;

            if (value > 128) {
                if (i < n) {
                    int repeatValue = buffer[i] & 0xFF;
                    i++;
                    for (int j = 0; j < (repeatValue & 0x7F) && position < total; j++) {
                        out[position++] = (byte) value;
                    }
                } else {
                    // Handle incomplete data for repeat value or break/throw an error
                    break;
                }
            } else {
                out[position++] = (byte) value;
            }
        }
    }
}