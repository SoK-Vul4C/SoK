// Fixed Function
@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();
    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    int estimateSize = estimateInitialBufferSize(enc, cb.remaining());
    ByteBuffer out = ByteBuffer.allocate(estimateSize);

    while (cb.remaining() > 0) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnmappable() || res.isMalformed()) {
            for (int i = 0; i < res.length(); ++i) {
                if (out.remaining() < 6) {
                    int remainingChars = cb.remaining() + res.length() - i; // remaining characters left to encode
                    int additionalSize = estimateIncrementalEncodingSize(enc, remainingChars * 6);
                    out = ZipEncodingHelper.growBufferBy(out, additionalSize);
                }
                if (tmp == null) {
                    tmp = CharBuffer.allocate(6);
                }
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }
        } else if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = ZipEncodingHelper.growBufferBy(out, increment);
        }
    }
    
    // Encode the remaining characters and handle overflow
    while (true) {
        final CoderResult res = enc.encode(cb, out, true);
        if (res.isUnderflow()) {
            break;
        } else if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = ZipEncodingHelper.growBufferBy(out, increment);
        }
    }

    out.limit(out.position());
    out.rewind();
    return out;
}