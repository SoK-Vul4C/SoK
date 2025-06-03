void setTo(Calendar c, int field, int offset, int i) {
    c.add(field, i - offset);
}