// Fixed Function
void setTo(Calendar c, int field, int offset, int i) {
    int convertedOffset = (field == Calendar.MONTH) ? (i - offset + c.get(Calendar.MONTH)) : (i - offset); 
    c.set(field, convertedOffset);
}