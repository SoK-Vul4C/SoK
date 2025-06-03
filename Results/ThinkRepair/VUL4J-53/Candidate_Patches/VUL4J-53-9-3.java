void setTo(Calendar c, int i) {
    if (field >= Calendar.ERA && field <= Calendar.DST_OFFSET) {
        // Adjust the offset based on the field type
        int adjustedValue = i - offset;
        c.set(field, adjustedValue);
    } else {
        System.out.println("Invalid Calendar field: " + field);
    }
}