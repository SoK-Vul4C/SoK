@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        final CoderResult res = enc.encode(cb, out, true);

        if (res.isUnderflow()) {
            enc.encode(cb, out, false);
        } else if (res.isOverflow()) {
            int remaining = cb.remaining() + 2; // Ensure space for full char or surrogate pair
            out = growBuffer(out, estimateIncrementalEncodingSize(enc, remaining));
        } else if (res.isMalformed() || res.isUnmappable()) {
            // Handle malformed or unmappable characters
            // You may modify this section based on the behavior you want
            cb.position(cb.position() - res.length()); // Reset position to encode characters
        }
    }

    // Flush any remaining characters
    enc.encode(cb, out, true);

    out.flip();
    return out;
}

private ByteBuffer growBuffer(ByteBuffer buffer, int additional) {
    int newSize = buffer.capacity() + additional;
    ByteBuffer newBuffer = ByteBuffer.allocate(newSize);
    buffer.flip();
    newBuffer.put(buffer);

    return newBuffer;
}