private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesRead >= csize) {
        return -1; // Signal end of stream
    }

    if (buf.position() >= buf.limit()) {
        buf.rewind(); // Reset the buffer position
        int bytesRead = in.read(buf.array());
        if (bytesRead == -1) {
            if (current.bytesRead < csize) {
                throw new IOException("Unexpected end of stream. Expected more data.");
            } else {
                return -1; // Actual end of the stream
            }
        }
        buf.limit(bytesRead);
        count(bytesRead);
        current.bytesReadFromStream += bytesRead;
    }

    int toRead = Math.min(buf.remaining(), length);
    toRead = (int) Math.min(toRead, csize - current.bytesRead); // Limit to remaining data
    if (toRead <= 0) {
        return -1; // End of stream
    }

    buf.get(buffer, offset, toRead);
    current.bytesRead += toRead;
    return toRead;
}