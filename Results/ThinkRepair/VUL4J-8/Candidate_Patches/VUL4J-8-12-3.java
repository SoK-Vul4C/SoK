// Fixed and Optimized Version of the encode function
@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();
    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    CharBuffer tmp = null;
    int charCount = 0;

    while (cb.hasRemaining()) {
        CoderResult res = enc.encode(cb, out, true);

        if (res.isError()) {
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }

            for (int i = cb.position() - res.length(); i < cb.position(); i++) {
                char c = cb.get(i);
                if (!enc.canEncode(c)) {
                    charCount += 6;
                } else {
                    charCount++;
                }
            }

            int totalExtraSpace = estimateIncrementalEncodingSize(enc, charCount);
            out = ZipEncodingHelper.growBufferBy(out, totalExtraSpace);
            cb.position(cb.position() - res.length());
        } else if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = ZipEncodingHelper.growBufferBy(out, increment);
        }
    }

    enc.encode(CharBuffer.wrap(""), out, true);

    out.limit(out.position());
    out.rewind();
    return out;
}