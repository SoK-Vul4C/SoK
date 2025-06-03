private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;

    byte[] buffer = new byte[1024]; // Buffer for reading input bytes
    int bufferIndex = 0;
    int bufferLength = 0;
    
    while (position < total) {
        if (bufferIndex >= bufferLength) {
            bufferLength = in.read(buffer);
            
            if (bufferLength == -1) {
                break; // End of input stream
            }
            
            bufferIndex = 0;
        }
        
        int n = buffer[bufferIndex++] & 0xFF;
        
        if (n > 128) {
            if (bufferIndex >= bufferLength) {
                int value = in.read();
                if (value == -1) {
                    break; // End of input stream
                }
                for (int i = 0; i < (n & 0x7F) && position < total; i++) {
                    out[position++] = (byte) value;
                }
            } else {
                int value = buffer[bufferIndex++] & 0xFF;
                for (int i = 0; i < (n & 0x7F) && position < total; i++) {
                    out[position++] = (byte) value;
                }
            }
        } else {
            for (int i = 0; i < n && position < total; i++) {
                if (bufferIndex >= bufferLength) {
                    int value = in.read();
                    if (value == -1) {
                        break; // End of input stream
                    }
                    out[position++] = (byte) value;
                } else {
                    out[position++] = buffer[bufferIndex++];
                }
            }
        }
    }
}