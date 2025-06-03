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
        buf.clear();
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

    int remainingData = (int) (csize - current.bytesRead);
    int toRead = Math.min(buf.remaining(), Math.min(length, remainingData));
    
    if (toRead == 0 && remainingData > 0) {
        return 0; // Return 0 to indicate no data currently available, more data may arrive
    }
    
    if (toRead == 0) {
        return -1; // Return -1 to indicate end of input stream
    }

    buf.get(buffer, offset, toRead);
    current.bytesRead += toRead;

    return toRead;
}