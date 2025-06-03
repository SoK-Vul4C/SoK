// Fixed Function
private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = (1 << (t - 1)) - 1; // Calculate the extend mask

    if ((v & (1 << (t - 1))) != 0) { // Check if the value is negative
        v |= ~vt; // Extend negative values
    }

    return v;
}