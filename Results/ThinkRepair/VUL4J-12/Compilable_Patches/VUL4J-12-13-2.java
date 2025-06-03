private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = (1 << (t - 1));
    while (v < vt) {
        // Calculate the midpoint between v and vt
        int midpoint = (vt - v) / 2; 
        if (midpoint == 0) {
            break; // Avoid infinite loop
        }
        v += midpoint;
    }
    return v;
}