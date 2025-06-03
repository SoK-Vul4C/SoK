@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnmappable() || res.isMalformed()) {
            // handle unmappable and malformed characters
            int spaceForSurrogate = estimateIncrementalEncodingSize(enc, 6 * res.length());
            if (spaceForSurrogate > out.remaining()) {
                // expand buffer size if needed
                int totalExtraSpace = estimateIncrementalEncodingSize(enc, cb.remaining() + 1);
                out = ZipEncodingHelper.growBufferBy(out, totalExtraSpace - out.remaining());
            }
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            for (int i = 0; i < res.length(); ++i) {
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }
        } else if (res.isOverflow()) {
            // compact the buffer and increase capacity
            out = ZipEncodingHelper.compactBuffer(out);
            out = ZipEncodingHelper.growBufferBy(out, cb.remaining() * 2);
        }
    }

    // flush any remaining characters
    enc.encode(cb, out, true);

    // finalize the buffer
    out.flip();
    return out;
}