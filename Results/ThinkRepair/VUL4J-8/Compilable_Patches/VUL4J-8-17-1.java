// Fixed Function
@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.remaining() > 0) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnmappable() || res.isMalformed()) {

            // write the unmappable characters in utf-16
            // pseudo-URL encoding style to ByteBuffer.

            int spaceForSurrogate = estimateIncrementalEncodingSize(enc, 6 * res.length());
            if (spaceForSurrogate > out.remaining()) {
                // if the destination buffer isn't oversized, assume that the presence of one
                // unmappable character makes it likely that there will be more. Find all the
                // unencoded characters and allocate space based on those estimates.
                int charCount = 0;
                for (int i = cb.position(); i < cb.limit(); i++) {
                    charCount += !enc.canEncode(cb.get(i)) ? 6 : 1;
                }
                int totalExtraSpace = estimateIncrementalEncodingSize(enc, charCount);
                out = ZipEncodingHelper.growBufferBy(out, totalExtraSpace - out.remaining());
            }
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            for (int i = 0; i < res.length(); ++i) {
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }

        } else if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = ZipEncodingHelper.growBufferBy(out, increment);
        }
    }
    // tell the encoder we are done
    enc.encode(cb, out, true);
    // may have caused underflow, but that's been ignored traditionally

    out.limit(out.position());
    out.rewind();
    return out;
}