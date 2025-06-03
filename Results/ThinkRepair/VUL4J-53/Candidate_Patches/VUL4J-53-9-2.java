void setTo(Calendar c, int i) {
    if (field >= Calendar.ERA && field <= Calendar.DST_OFFSET) {
        // Adjust the offset based on the field type to prevent issues with negative values
        int adjustedValue = (field == Calendar.ERA) ? i + offset : i - offset;
        c.set(field, adjustedValue);
    } else {
        System.out.println("Invalid field type for Calendar: " + field);
    }
}