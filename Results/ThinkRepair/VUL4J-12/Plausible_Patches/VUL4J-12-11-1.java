private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = (1 << (t - 1));
    int originalVt = vt; // store the original vt value

    // Ensure vt is positive
    if (vt < 0) {
        vt = 0;
    }

    while (v < vt) {
        vt = (originalVt << 1); // Update vt by shifting left
        v += vt; // Update v by adding vt
    }
    return v;
}