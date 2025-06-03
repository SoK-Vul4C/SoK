private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {
    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesRead >= csize) {
        return -1; // End of stored entry
    }

    if (buf.position() >= buf.limit()) {
        buf.position(0);
        final int l = in.read(buf.array());
        if (l == -1) {
            // Check if all data has been read from the input stream
            if (csize == current.bytesRead) {
                return -1; // End of stored entry
            } else {
                throw new IOException("Truncated ZIP file"); // Handle truncated zip file
            }
        }
        buf.limit(l);
        
        count(l);
        current.bytesReadFromStream += l;
    }

    int remaining = (int) (csize - current.bytesRead);
    int toRead = Math.min(Math.min(buf.remaining(), length), remaining);

    buf.get(buffer, offset, toRead);
    current.bytesRead += toRead;

    return toRead;
}