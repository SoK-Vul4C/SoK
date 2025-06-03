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
            if (cb.hasRemaining()) {
                continue;
            } else {
                cb.compact();
            }
        }

        if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            ByteBuffer newOut = ByteBuffer.allocate(out.capacity() + increment);
            out.flip();
            newOut.put(out);
            out = newOut;
        }

        if (res.isUnmappable() || res.isMalformed()) {
            int surrogateSize = 6 * res.length();
            if (out.remaining() < surrogateSize) {
                int newSize = out.capacity() + surrogateSize;
                ByteBuffer newOut = ByteBuffer.allocate(newSize);
                out.flip();
                newOut.put(out);
                out = newOut;
            }
            
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            for (int i = 0; i < res.length(); ++i) {
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }
        }
    }

    // Finish the encoding
    enc.encode(cb, out, true);
    enc.flush(out);

    out.flip();
    return out;
}