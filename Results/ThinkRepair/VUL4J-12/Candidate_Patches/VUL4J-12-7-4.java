private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1); // Calculate the threshold value
    int extendMask = (1 << t) - 1; // Calculate the extend mask for t bits
    int signBit = 1 << (t - 1); // Calculate the sign bit for t bits

    while (v < vt) {
        v += (v < signBit) ? -extendMask : extendMask; // Extend value based on sign bit
    }

    return v;
}