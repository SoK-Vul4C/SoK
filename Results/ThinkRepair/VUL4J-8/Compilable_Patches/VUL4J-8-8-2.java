@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();
    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        while (true) {
            CoderResult res = enc.encode(cb, out, true);
            if (res.isUnderflow()) {
                break;
            }
            if (res.isOverflow()) {
                out = growBufferBy(out, cb.remaining());
            } else if (res.isUnmappable() || res.isMalformed()) {
                cb.get(); // Skip the unmappable or malformed character
            }
        }
    }

    out.limit(out.position());
    out.rewind();
    return out;
}

private ByteBuffer growBufferBy(ByteBuffer buffer, int increment) {
    int newCapacity = buffer.capacity() + increment;
    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    return newBuffer;
}