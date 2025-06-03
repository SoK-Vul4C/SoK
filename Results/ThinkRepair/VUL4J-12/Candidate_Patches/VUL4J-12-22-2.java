private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1);
    while (v < vt) {
        v <<= 1; // Shift left to add another bit
        v |= 1; // Set the least significant bit to 1
    }
    return v;
}