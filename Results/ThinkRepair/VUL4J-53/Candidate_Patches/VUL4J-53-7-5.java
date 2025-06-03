void setTo(Calendar c, int field, int offset) {
    int value = c.get(field);
    c.set(field, value + offset);
}