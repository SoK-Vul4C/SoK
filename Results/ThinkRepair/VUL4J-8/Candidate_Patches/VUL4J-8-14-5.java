} else if (res.isOverflow()) {
    int spaceNeeded = estimateIncrementalEncodingSize(enc, cb.remaining());
    int newCapacity = out.capacity() + spaceNeeded;
    ByteBuffer newOut = ByteBuffer.allocate(newCapacity);
    out.flip();
    newOut.put(out);
    out = newOut;
}