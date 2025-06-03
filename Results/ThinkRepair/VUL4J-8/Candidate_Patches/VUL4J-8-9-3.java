// Fixed Function
@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    CharBuffer tmp = null;
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining() || enc.hasRemaining()) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isError()) {
            res.throwException();
        }

        if (res.isUnderflow()) {
            enc.flush(out);
            cb.compact();
        } else if (res.isOverflow()) {
            out = growBuffer(out, cb.remaining());
        } else if (res.isMalformed() || res.isUnmappable()) {
            if (tmp == null) {
                tmp = CharBuffer.allocate(6);
            }
            for (int i = 0; i < res.length(); i++) {
                out = encodeFully(enc, encodeSurrogate(tmp, cb.get()), out);
            }
            cb.position(cb.position() + res.length());
        }
    }

    if (cb.position() < cb.limit()) {
        while (enc.encode(cb, out, true).isOverflow()) {
            out = growBuffer(out, cb.remaining());
        }
        enc.flush(out);
    }

    out.flip();
    return out;
}