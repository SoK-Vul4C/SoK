@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.remaining() > 0) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnderflow()) {
            if (cb.remaining() == 0) {
                cb.compact();
                cb.flip();
            }
        } else if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = ZipEncodingHelper.growBufferBy(out, increment);
        } else if (res.isUnmappable() || res.isMalformed()) {
            int remaining = res.length();
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            while (remaining > 0) {
                if (cb.remaining() == 0) {
                    cb.compact();
                    cb.put(tmp.flip());
                    tmp.clear();
                }
                int codeUnits = Math.min(cb.remaining(), remaining);
                cb.get(tmp.array(), tmp.position(), codeUnits);
                tmp.limit(codeUnits);
                remaining -= codeUnits;
                out = encodeFully(enc, tmp, out);
            }
        }
    }

    // Flush remaining characters
    while (cb.hasRemaining()) {
        enc.encode(cb, out, true);
    }

    // Finish encoding
    enc.flush(out);
    out.flip();
    return out;
}