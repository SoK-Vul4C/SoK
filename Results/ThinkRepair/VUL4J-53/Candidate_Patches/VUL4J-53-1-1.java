void setTo(Calendar c, int i) {
    if (field >= Calendar.YEAR && field <= Calendar.MILLISECOND) {
        int fieldValue = i - offset;
        c.set(field, fieldValue);
    }
}