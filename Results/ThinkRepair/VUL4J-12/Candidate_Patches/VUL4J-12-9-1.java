private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1);
    int threshold = (1 << t) - 1;
    
    while (v < vt) {
        v += threshold;
    }
    
    return v;
}