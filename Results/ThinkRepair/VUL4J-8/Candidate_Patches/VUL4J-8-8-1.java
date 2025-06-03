@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    int estimatedIncrement;
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.remaining() > 0) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnmappable() || res.isMalformed()) {
            // Handle unmappable characters
            int spaceForSurrogate = estimateIncrementalEncodingSize(enc, 6 * res.length());
            if (spaceForSurrogate > out.remaining()) {
                // Increase buffer size if needed
                // Estimate additional space needed based on unmappable characters
                int charCount = 0;
                for (int i = cb.position(); i < cb.limit(); i++) {
                    charCount += !enc.canEncode(cb.get(i)) ? 6 : 1;
                }
                int totalExtraSpace = estimateIncrementalEncodingSize(enc, charCount);
                out = growBufferBy(out, totalExtraSpace - out.remaining());
            }
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            for (int i = 0; i < res.length(); ++i) {
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }
        } else if (res.isOverflow()) {
            // Handle overflow by increasing buffer size
            estimatedIncrement = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = growBufferBy(out, estimatedIncrement);
        }
    }

    // Finalize encoding process
    enc.encode(cb, out, true);

    out.limit(out.position());
    out.rewind();
    return out;
}