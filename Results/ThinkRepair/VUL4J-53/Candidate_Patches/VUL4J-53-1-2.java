void setTo(Calendar c, int i) {
    switch (field) {
        case Calendar.YEAR:
            c.set(Calendar.YEAR, i - offset);
            break;
        case Calendar.MONTH:
            c.set(Calendar.MONTH, i - offset);
            break;
        case Calendar.DAY_OF_MONTH:
            c.set(Calendar.DAY_OF_MONTH, i - offset);
            break;
        // Add cases for other Calendar fields as needed
        default:
            // Handle the case when the field is not recognized
            System.out.println("Unsupported Calendar field");
            break;
    }
}