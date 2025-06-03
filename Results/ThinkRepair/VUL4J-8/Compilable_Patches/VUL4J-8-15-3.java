@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        if (!cb.hasRemaining() && enc.flush(out).isUnderflow()) {
            break;
        }
        CoderResult res = enc.encode(cb, out, true);

        if (res.isUnderflow()) {
            res = enc.flush(out);
        }

        if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = ZipEncodingHelper.growBufferBy(out, increment);
        } else if (res.isError()) {
            // Handle encoding errors
            cb.position(cb.position() + res.length()); 
        }
    }

    out.flip();
    return out;
}