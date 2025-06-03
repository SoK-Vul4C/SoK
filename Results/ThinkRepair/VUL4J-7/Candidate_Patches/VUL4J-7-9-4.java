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
            int bufferSize = Math.min((int) (csize - current.bytesRead), Math.max(length, 1024)); // Dynamic buffer size
            buf = ByteBuffer.allocate(bufferSize);
            final int l = in.read(buf.array()); // Read data into the buffer
            if (l == -1) {
                break; // End of stream reached
            }
            buf.limit(l);

            count(l);
            current.bytesReadFromStream += l;
        }

        int toRead = Math.min(buf.remaining(), length - totalRead);
        if (toRead <= 0) {
            break; // No more data to read
        }

        buf.get(buffer, offset + totalRead, toRead);
        current.bytesRead += toRead;
        totalRead += toRead;
    }

    return totalRead == 0 ? -1 : totalRead;
}