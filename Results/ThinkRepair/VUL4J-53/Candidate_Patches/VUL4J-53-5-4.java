void setTo(Calendar c, int i) {
    c.set(field, i - c.getOffset(field)); // Fixed Line
}