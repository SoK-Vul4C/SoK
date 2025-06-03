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
            // Code for handling unmappable or malformed characters
        } else if (res.isOverflow()) {
            int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
            out = growBuffer(out, increment);
        } else if (res.isUnderflow()) {
            break; // Exit the loop when underflow is encountered
        }
    }

    // tell the encoder we are done
    enc.encode(cb, out, true);
    out.limit(out.position());
    out.rewind();
    return out;
}