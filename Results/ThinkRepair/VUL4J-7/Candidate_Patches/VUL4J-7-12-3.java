private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesRead >= csize) {
        return -1; // End of stream reached, return -1 indicating completion
    }

    int bytesReadTotal = 0; // Track total bytes read including stream and buffer
    while (current.bytesRead < csize && bytesReadTotal < length) {
        if (buf.position() >= buf.limit()) {
            buf.position(0);
            final int l = in.read(buf.array());
            if (l == -1) {
                break; // Exit the loop if end of the stream
            }
            buf.limit(l);

            count(l);
            current.bytesReadFromStream += l;
        }

        int toRead = Math.min(buf.remaining(), length - bytesReadTotal);
        if ((csize - current.bytesRead) < toRead) {
            // if it is smaller than toRead then it fits into an int
            toRead = (int) (csize - current.bytesRead);
        }
        buf.get(buffer, offset + bytesReadTotal, toRead);
        current.bytesRead += toRead; // Update bytes read from the stream
        bytesReadTotal += toRead; // Update total bytes read
    }

    if (current.bytesRead >= csize) {
        return bytesReadTotal; // Return total bytes read
    }

    return bytesReadTotal; // Return total bytes read even if not all bytes were read
}