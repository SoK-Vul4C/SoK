// Fixed Function
@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();
    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        CoderResult res = enc.encode(cb, out, true);
        if (res.isOverflow()) {
            int newBufferSize = out.capacity() * 2;
            ByteBuffer newBuffer = ByteBuffer.allocate(newBufferSize);
            out.flip();
            newBuffer.put(out);
            out = newBuffer;
        } else if (res.isError()) {
            res.throwException();
        }
    }

    res = enc.flush(out);
    if (res.isOverflow()) {
        int newBufferSize = out.capacity() * 2;
        ByteBuffer newBuffer = ByteBuffer.allocate(newBufferSize);
        out.flip();
        newBuffer.put(out);
        out = newBuffer;
        enc.flush(out);
    } else if (res.isError()) {
        res.throwException();
    }

    out.flip();
    return out;
}