} else if (res.isOverflow()) {
```

The correct syntax should be to close the conditional block before the comment. Here is the fixed version:

```java
} else if (res.isOverflow()) {
        int increment = estimateIncrementalEncodingSize(enc, cb.remaining());
        out = ZipEncodingHelper.growBufferBy(out, increment);
    }