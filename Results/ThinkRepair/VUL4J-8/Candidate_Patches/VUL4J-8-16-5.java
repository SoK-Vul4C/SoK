} else if (res.isOverflow()) {
    int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
    out = ZipEncodingHelper.growBufferBy(out, increment);
}