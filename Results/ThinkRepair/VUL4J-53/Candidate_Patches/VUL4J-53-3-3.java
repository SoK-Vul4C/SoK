// Define the instance variable 'field' at the class level
private int field;

// Corrected setTo method
void setTo(Calendar c, int i) {
    c.set(field, i - offset);
}