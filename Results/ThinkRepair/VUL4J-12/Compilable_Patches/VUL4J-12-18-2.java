private int extend(int v, final int t) {
    int vt = (1 << (t - 1));
    while (v < vt) {
        vt = (-1 << (t - 1)) + 1;
        v += vt;
    }
    return v;
}
```

The issue lies in how `vt` is calculated within the `while` loop. The current implementation replaces `vt` with a new value rather than updating it based on the current value of `t`.

To accurately update `vt` based on the current `t`, the correct calculation should be: `vt = (-1 << (t - 1));`.

Here is the fixed version of the function:
```java
private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = (1 << (t - 1));
    while (v < vt) {
        vt = (-1 << (t - 1));
        v += vt;
    }
    return v;
}