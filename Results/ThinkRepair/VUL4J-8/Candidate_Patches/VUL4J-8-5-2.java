// Fixed Function
@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnderflow()) {
            enc.encode(cb, out, true); // complete the encoding
            break;
        }

        if (res.isOverflow()) {
            // Grow the buffer to accommodate more data
            int newCapacity = out.capacity() * 2;
            ByteBuffer newOut = ByteBuffer.allocate(newCapacity);
            out.flip();
            newOut.put(out);
            out = newOut;
        }

        if (res.isError()) {
            throw new RuntimeException("Encoding error occurred");
        }

        if (res.isMalformed() || res.isUnmappable()) {
            int increment = estimateIncrementalEncodingSize(enc, 6); // allocate space for potential surrogate pair
            out = ZipEncodingHelper.growBufferBy(out, increment);
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            for (int i = cb.position() - 1; i < cb.limit(); i++) {
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }
        }
    }

    out.limit(out.position());
    out.rewind();
    return out;
}