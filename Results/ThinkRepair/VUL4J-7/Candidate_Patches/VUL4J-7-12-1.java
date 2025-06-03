private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesRead >= csize) {
        return -1;
    }

    if (buf.position() >= buf.limit()) {
        buf.position(0);
        final int l = in.read(buf.array());
        if (l == -1) {
            return -1; // End of stream reached, return -1 indicating completion
        }
        buf.limit(l);

        count(l);
        current.bytesReadFromStream += l;
    }

    int toRead = Math.min(buf.remaining(), length);
    if ((csize - current.bytesRead) < toRead) {
        // if it is smaller than toRead then it fits into an int
        toRead = (int) (csize - current.bytesRead);
    }
    buf.get(buffer, offset, toRead);
    current.bytesRead += toRead;

    if (current.bytesRead >= csize) {
        return -1; // Returning -1 after all bytes are read from the stream
    }

    return toRead;
}