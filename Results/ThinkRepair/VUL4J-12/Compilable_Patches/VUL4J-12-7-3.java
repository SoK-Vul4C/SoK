private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1); // Calculate the threshold value
    while (v < vt) {
        int shiftAmount = t - 1; // Calculate the shift amount based on bit depth
        int additional = (-1 << shiftAmount); // Calculate the additional value to extend
        v += additional; // Extend the value v
        vt = 1 << (t - 1); // Recalculate the threshold value
    }
    return v;
}