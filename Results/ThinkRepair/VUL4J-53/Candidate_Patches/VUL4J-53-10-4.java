void setTo(Calendar c, int field, int offset, int i) {
    c.set(field, c.get(field) + (i - offset));
}