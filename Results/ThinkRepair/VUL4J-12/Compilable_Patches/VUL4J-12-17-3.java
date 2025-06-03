private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1); // Calculate the value for vt based on t

    if (v < vt && v >= -vt) { // Check if v is within the target range
        if (v < 0 && t > 1) {
            v += (1 << t) - 1; // Extend v to fit the range if v is negative
        } else if (v >= 0 && t > 0) {
            v -= (1 << t); // Extend v to fit the range if v is non-negative
        }
    }
    
    return v;
}