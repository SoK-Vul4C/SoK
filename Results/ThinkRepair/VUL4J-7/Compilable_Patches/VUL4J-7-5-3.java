private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesReadFromStream >= csize) {
        return -1;  // Already read all the data from the current entry
    }

    if (buf.position() >= buf.limit()) {
        buf.position(0);
        final int l = in.read(buf.array());
        if (l == -1) {
            return -1;  // EOF reached
        }
        buf.limit(l);

        count(l);
        current.bytesReadFromStream += l;
    }

    int toRead = Math.min(buf.remaining(), length);
    toRead = (int) Math.min(csize - current.bytesReadFromStream, toRead);

    if (toRead > 0) {
        buf.get(buffer, offset, toRead);
        current.bytesReadFromStream += toRead;
        return toRead;  // Return the number of bytes read
    }

    return -1;  // Return -1 if no data was read
}