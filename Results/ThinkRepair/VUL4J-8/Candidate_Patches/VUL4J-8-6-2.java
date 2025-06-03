// Fixed Function
@Override
public ByteBuffer encode(final String name) {
    final CharsetEncoder enc = newEncoder();
    final CharBuffer cb = CharBuffer.wrap(name);
    ByteBuffer out = ByteBuffer.allocate(estimateInitialBufferSize(enc, cb.remaining()));

    while (cb.remaining() > 0) {
        final CoderResult res = enc.encode(cb, out, false);

        if (res.isUnderflow()) {
            if (cb.remaining() == 0) {
                break; // Break the loop if there are no more characters to encode
            }
            out = ensureRemainingCapacity(out, enc.maxBytesPerChar()); // Ensure enough space for the next character
        } else if (res.isOverflow()) {
            out = growBuffer(out); // Grow the buffer if an overflow occurs
        } else if (res.isUnmappable() || res.isMalformed()) {
            handleUnmappableOrMalformed(res.length(), enc, cb, out); // Handle unmappable or malformed characters
        }
    }

    // Flush any remaining characters
    while (enc.encode(cb, out, true).isOverflow()) {
        out = growBuffer(out);
    }

    out.flip(); // Prepare the buffer for reading
    return out;
}

// Helper method to ensure enough remaining capacity in the ByteBuffer
private ByteBuffer ensureRemainingCapacity(ByteBuffer buffer, int additionalCapacity) {
    if (buffer.remaining() < additionalCapacity) {
        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + additionalCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    } else {
        return buffer;
    }
}

// Helper method to grow the ByteBuffer when an overflow occurs
private ByteBuffer growBuffer(ByteBuffer buffer) {
    int newCapacity = buffer.capacity() * 2; // Double the capacity
    ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    return newBuffer;
}

// Helper method to handle unmappable or malformed characters
private void handleUnmappableOrMalformed(int length, CharsetEncoder encoder, CharBuffer charBuffer, ByteBuffer outputBuffer) {
    // Implement the logic to handle unmappable or malformed characters
    // For example, you can encode the characters in a specific way or throw an exception
    // This logic should be customized based on the requirements
}