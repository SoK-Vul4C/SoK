private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();

    if (current.bytesRead >= csize) {
        return -1; // End of stream reached
    }

    int totalRead = 0;
    while (totalRead < length) {
        if (buf.position() >= buf.limit()) {
            buf.position(0);
            final int l = in.read(buf.array());
            if (l == -1) {
                break; // End of stream reached
            }
            buf.limit(l);

            count(l);
            current.bytesReadFromStream += l;
        }

        int toRead = Math.min(buf.remaining(), length - totalRead);
        if ((csize - current.bytesRead) < toRead) {
            // if it is smaller than toRead then it fits into an int
            toRead = (int) (csize - current.bytesRead);
        }
        buf.get(buffer, offset + totalRead, toRead);
        current.bytesRead += toRead;
        totalRead += toRead;
        
        if (toRead == 0) {
            break; // Prevent infinite loop in case toRead becomes 0
        }
    }

    return totalRead == 0 ? -1 : totalRead;
}