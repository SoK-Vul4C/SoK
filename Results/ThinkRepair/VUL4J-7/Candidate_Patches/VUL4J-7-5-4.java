private int readStored(final byte[] buffer, final int offset, final int length) throws IOException {

    if (current.hasDataDescriptor) {
        if (lastStoredEntry == null) {
            readStoredEntry();
        }
        return lastStoredEntry.read(buffer, offset, length);
    }

    final long csize = current.entry.getSize();
    if (current.bytesReadFromStream >= csize) {
        current.bytesRead = 0;
        current.bytesReadFromStream = 0;
        return -1; // All data from the current entry has been read
    }

    int totalRead = 0;
    while (totalRead < length) {
        int remaining = length - totalRead;
        int available = buf.remaining();

        if (available <= 0) {
            buf.position(0);
            int bytesRead = in.read(buf.array());
            if (bytesRead == -1) {
                break; // End of file reached
            }
            buf.limit(bytesRead);
            available = bytesRead;
        }

        int bytesToRead = Math.min(remaining, available);
        buf.get(buffer, offset + totalRead, bytesToRead);

        totalRead += bytesToRead;
        current.bytesRead += bytesToRead;
        current.bytesReadFromStream += bytesToRead;
    }

    return totalRead > 0 ? totalRead : -1; // Return -1 if nothing was read
}