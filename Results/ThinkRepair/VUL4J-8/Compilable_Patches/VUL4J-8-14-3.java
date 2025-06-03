} else if (res.isOverflow()) {
    int spaceNeeded = estimateIncrementalEncodingSize(enc, cb.remaining());
    ByteBuffer newOut = ByteBuffer.allocate(out.capacity() + spaceNeeded);
    out.flip();
    newOut.put(out);
    out = newOut;
    // Remaining space for additional input, continue encoding
}