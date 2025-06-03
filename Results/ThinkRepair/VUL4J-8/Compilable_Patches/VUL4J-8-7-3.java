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
            int spaceForSurrogate = estimateIncrementalEncodingSize(enc, 6 * res.length());
            if (spaceForSurrogate > out.remaining()) {
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
            // Resize the buffer by a certain percentage increase based on the remaining characters
            int remainingChars = cb.remaining();
            int newCapacity = (int) (out.capacity() * 1.5); // Increase by 50%
            out = ZipEncodingHelper.growBufferBy(out, newCapacity - out.remaining());
        }
    }

    // tell the encoder we are done
    enc.encode(cb, out, true);

    out.limit(out.position());
    out.rewind();
    return out;
}