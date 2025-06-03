// Assuming field and offset are class-level fields
void setTo(Calendar c, int i) {
    c.set(field, i - offset); // Fixed Line
}