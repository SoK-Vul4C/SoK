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

    int totalRead = 0;

    while (totalRead < length && current.bytesRead < csize) {
        if (buf.position() >= buf.limit()) {
            buf.clear();
            int l = in.read(buf.array());
            if (l == -1) {
                if (totalRead == 0) {
                    return -1;
                } else {
                    break;
                }
            }
            buf.limit(l);

            count(l);
            current.bytesReadFromStream += l;
        }

        int toRead = Math.min(buf.remaining(), length - totalRead);
        if ((csize - current.bytesRead) < toRead) {
            toRead = (int) (csize - current.bytesRead);
        }
        buf.get(buffer, offset + totalRead, toRead);
        totalRead += toRead;
        current.bytesRead += toRead;
    }

    return totalRead > 0 ? totalRead : -1;
}