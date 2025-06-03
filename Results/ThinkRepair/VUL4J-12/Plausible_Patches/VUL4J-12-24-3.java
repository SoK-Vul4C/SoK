private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1);
    int shift = 32 - t;
    int rangeMin = -(1 << (t - 1));
    int rangeMax = (1 << (t - 1)) - 1;

    while (v < rangeMin || v > rangeMax) {
        v = (v << shift) >> shift;
    }
    return v;
}