@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();

    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.hasRemaining()) {
        CoderResult res = enc.encode(cb, out, false);

        if (res.isOverflow()) {
            // Resize the buffer and retry the encoding
            out = growBuffer(out, cb.remaining());
        }
    }

    // Flush any remaining characters
    enc.encode(cb, out, true);

    // Prepare the buffer for reading
    out.flip();
    return out;
}
```

2. Define a helper method to resize the buffer in case of an overflow:
```java
private ByteBuffer growBuffer(ByteBuffer buffer, int additionalCapacity) {
    ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + additionalCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    return newBuffer;
}