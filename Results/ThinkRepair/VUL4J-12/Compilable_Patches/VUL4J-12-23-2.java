private int extend(int v, final int t) {
    // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
    int vt = 1 << (t - 1); // Calculate vt as 2^(t-1)
    
    int target = (1 << t) - 1; // Calculate the target value based on t
    
    while (v < vt) {
        v += target; // Increment v by target to meet the condition
    }
    
    return v;
}