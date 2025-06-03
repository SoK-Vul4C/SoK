void setTo(Calendar c, int field, int i, int offset) {
    c.add(field, i - offset);
}