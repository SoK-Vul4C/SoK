@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.remaining() > 0) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnmappable() || res.isMalformed()) {
            int spaceForSurrogate = estimateIncrementalEncodingSize(enc, 6 * res.length());
            if (spaceForSurrogate > out.remaining()) {
                // Expand the buffer if needed
                out = growBufferIfNeeded(out, spaceForSurrogate);
            }
            
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            for (int i = 0; i < res.length(); ++i) {
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }
        } else if (res.isOverflow()) {
            out = growBufferIfNeeded(out, estimateIncrementalEncodingSize(enc, cb.remaining()));
        }
    }

    // Flush the encoder
    enc.encode(cb, out, true);

    out.limit(out.position());
    out.rewind();
    return out;
}

private ByteBuffer growBufferIfNeeded(ByteBuffer buffer, int additionalSpace) {
    int newCapacity = buffer.capacity() + additionalSpace;
    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
    buffer.flip(); // Reset position to beginning of buffer
    newBuffer.put(buffer); // Copy existing data to new buffer
    return newBuffer;
}