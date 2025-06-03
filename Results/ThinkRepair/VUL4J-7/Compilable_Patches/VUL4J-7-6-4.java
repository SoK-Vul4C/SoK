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

    int bytesRead = 0;
    while (bytesRead < length && current.bytesRead < csize) {
        if (buf.position() >= buf.limit()) {
            // Refill buffer
            buf.position(0);
            int bytesReadFromStream = in.read(buf.array());
            if (bytesReadFromStream == -1) {
                if (current.bytesRead < csize) {
                    throw new IOException("Truncated ZIP file");
                } else {
                    return bytesRead > 0 ? bytesRead : -1; // End of stored entry
                }
            }
            buf.limit(bytesReadFromStream);
            current.bytesReadFromStream += bytesReadFromStream;
        }

        int remaining = (int) (csize - current.bytesRead);
        int toRead = Math.min(Math.min(buf.remaining(), length - bytesRead), remaining);

        buf.get(buffer, offset + bytesRead, toRead);
        current.bytesRead += toRead;
        bytesRead += toRead;
    }

    return bytesRead > 0 ? bytesRead : -1;
}