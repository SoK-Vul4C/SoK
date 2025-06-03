private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1); // Calculate the threshold value for extension
    int mask = (1 << t) - 1; // Calculate a mask with t bits set to 1
    while (v < vt) {
        v += mask + 1; // Extend the value by adding the mask + 1
    }
    return v;
}