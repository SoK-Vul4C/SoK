// Fixed Function
private int extend(int v, final int t) {
    // Calculate the minimum value that can be represented by t bits
    int vt = -1 << (t - 1);
    
    // Check if v needs to be extended to t bits
    while (v < vt) {
        v += (1 << t);
    }
    
    return v;
}