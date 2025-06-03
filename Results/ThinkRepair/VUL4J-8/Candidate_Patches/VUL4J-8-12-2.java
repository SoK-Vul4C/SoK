et's address the issue in the encode function step by step. The failing test is due to a timeout, so we need to optimize the encoding process to prevent it from taking too long.

The encode function needs to handle encoding errors such as unmappable or malformed characters more efficiently to prevent the timeout issue.

// Optimized Fix for the encode function
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