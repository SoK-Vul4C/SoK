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

    while (buf.position() >= buf.limit()) {
        buf.position(0);
        int toRead = (int) Math.min(buf.capacity(), csize - current.bytesRead);
        if (toRead <= 0) {
            break;
        }
        int bytesRead = in.read(buf.array(), 0, toRead);
        if (bytesRead == -1) {
            break;
        }
        buf.limit(bytesRead);

        count(bytesRead);
        current.bytesReadFromStream += bytesRead;
    }

    int toRead = Math.min(buf.remaining(), length);
    if ((csize - current.bytesRead) < toRead) {
        // if it is smaller than toRead then it fits into an int
        toRead = (int) (csize - current.bytesRead);
    }
    if (toRead == 0) {
        return -1;
    }
    buf.get(buffer, offset, toRead);
    current.bytesRead += toRead;
    return toRead;
}