@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        final CoderResult res = enc.encode(cb, out, true);

        if (res.isOverflow()) {
            out = growBuffer(out);
        } else if (res.isError()) {
            // Handle error condition gracefully
            throw new IllegalStateException("Encoding error occurred");
        }
    }

    enc.encode(cb, out, true); // Flush remaining characters

    out.flip(); // Prepare for reading

    return out;
}

private ByteBuffer growBuffer(ByteBuffer buffer) {
    int newCapacity = buffer.capacity() * 2; // Double the buffer capacity
    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
    buffer.flip(); // Reset position to beginning of buffer
    newBuffer.put(buffer); // Copy existing data to new buffer
    return newBuffer;
}