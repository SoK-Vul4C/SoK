void setTo(Calendar c, int i) {
    if (i >= offset) {
        c.set(field, i - offset);
    } else {
        // Handle invalid values, throw an exception, or log a message
        System.out.println("Invalid value: " + i);
    }
}