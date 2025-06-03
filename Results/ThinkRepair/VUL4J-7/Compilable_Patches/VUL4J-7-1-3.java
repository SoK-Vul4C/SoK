private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesRead >= csize) {
        return -1; // End of the stream
    }

    int totalBytesRead = 0; // Track total bytes read from both the buffer and the stream

    while (totalBytesRead < length) {
        if (buf.remaining() <= 0) {
            buf.position(0);
            int bytesReadFromStream = in.read(buf.array());
            if (bytesReadFromStream == -1) {
                if (totalBytesRead == 0) {
                    return -1; // End of the stream, no data read
                } else {
                    break; // End of the stream, return with total bytes read
                }
            }
            buf.limit(bytesReadFromStream);
            count(bytesReadFromStream);
        }

        int remainingLength = length - totalBytesRead;
        int bytesToRead = Math.min(buf.remaining(), remainingLength);
        buf.get(buffer, offset + totalBytesRead, bytesToRead);
        totalBytesRead += bytesToRead;
    }

    current.bytesRead += totalBytesRead;
    return totalBytesRead;
}