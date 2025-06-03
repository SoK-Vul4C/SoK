private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1);
    
    // Check if v is already sign-extended
    if ((v & (1 << (t - 1))) == 0) {
        return v;
    }

    // Calculate the sign extension value
    int mask = (1 << t) - 1;
    int signExtension = vt - (v & (vt - 1));

    // Perform sign extension until v is fully extended
    while ((v & vt) != 0) {
        v = (v | vt) & mask;
        vt >>= 1;
    }

    return v;
}