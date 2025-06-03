@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();
    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        CoderResult res = enc.encode(cb, out, true);
        
        if (res.isUnderflow()) {
            if (cb.hasRemaining()) {
                cb.compact();
            } else {
                break;
            }
        } else if (res.isOverflow()) {
            out = growBufferBy(out, cb.remaining() * 2);
        } else if (res.isUnmappable() || res.isMalformed()) {
            cb.position(cb.position() + 1); // Skip the unmappable or malformed character
        }
    }

    out.flip();
    return out;
}

private ByteBuffer growBufferBy(ByteBuffer buffer, int increment) {
    int newCapacity = buffer.capacity() + increment;
    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    return newBuffer;
}