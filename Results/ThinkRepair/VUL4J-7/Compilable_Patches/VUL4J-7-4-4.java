if (current.bytesRead >= csize || buf.position() >= buf.limit()) {
    return -1; // Return -1 when reached end of stream or buffer
}
```

2. Ensure that when reaching the end of the stream, we fill the remaining bytes in the buffer with 0s and update the remaining bytes to read:
```java
if (buf.position() >= buf.limit()) {
    buf.rewind();
    int bytesRead = in.read(buf.array());
    if (bytesRead == -1) {
        return -1; // Return -1 when reached end of stream
    }
    buf.limit(bytesRead);
}
```

3. Adjust the logic for reading from the buffer and updating the current bytes read:
```java
int toRead = Math.min(buf.remaining(), length);
if ((csize - current.bytesRead) < toRead) {
    toRead = (int) (csize - current.bytesRead);
}