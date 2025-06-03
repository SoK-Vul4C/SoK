private static void decompress(final InputStream in, final byte[] out) throws IOException {
    int position = 0;
    final int total = out.length;
    byte[] buffer = new byte[1024]; // buffer size can be adjusted

    while (position < total) {
        int n = in.read(buffer); 

        if (n == -1) {
            // End of input stream
            break;
        }

        int i = 0;
        while (i < n) {
            int value = buffer[i] & 0xFF;

            if (value > 128) {
                if (i + 1 < n) {
                    int repeatValue = buffer[i+1] & 0xFF;
                    if (position < total) {
                        for (int j = 0; j < (repeatValue & 0x7F); j++) {
                            out[position++] = (byte) value;
                        }
                    } else {
                        break; // Break out of the loop if the output array is filled
                    }
                    i += 2;
                } else {
                    // Handle incomplete data for repeat value
                    // Or raise an error based on your error handling strategy
                }
            } else {
                if (position < total) {
                    out[position++] = (byte) value;
                } else {
                    break; // Break out of the loop if the output array is filled
                }
                i++;
            }
        }
    }
}