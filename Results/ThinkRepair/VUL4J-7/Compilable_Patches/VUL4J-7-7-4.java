private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesRead >= csize) {
        return -1; // End of entry
    }

    if (buf.position() >= buf.limit()) {
        buf.position(0);
        final int l = in.read(buf.array());
        if (l < 0) {
            return -1; // EOF reached, no more data
        } else if (l == 0) {
            // Nothing was read, should not happen, but retry just in case
            return readStored(buffer, offset, length);
        }
        buf.limit(l);

        count(l);
        current.bytesReadFromStream += l;
    }

    int remaining = (int) (csize - current.bytesRead);
    int toRead = Math.min(remaining, Math.min(buf.remaining(), length));
    buf.get(buffer, offset, toRead);
    current.bytesRead += toRead;
    return toRead;
}