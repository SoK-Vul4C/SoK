private int extend(int v, final int t) {
    // Calculate the sign bit
    int signBit = 1 << (t - 1);

    // Calculate the maximum positive value that can be represented with t bits
    int maxValue = (1 << (t - 1)) - 1;

    // Check if the original value is negative and adjust it
    if ((v & signBit) != 0) {
        v -= (1 << t);
    }

    // Extend the value if needed to reach the maximum positive value
    while (v < maxValue) {
        v += (1 << t);
    }

    return v;
}