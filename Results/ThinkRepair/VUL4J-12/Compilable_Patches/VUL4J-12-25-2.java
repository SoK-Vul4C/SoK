private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1);
    while (v < vt) {
        int tmp = (1 << t) - 1; // Calculate the maximum value that can be represented by t bits
        v += tmp; // Add the maximum value to v to extend it correctly
    }
    return v;
}