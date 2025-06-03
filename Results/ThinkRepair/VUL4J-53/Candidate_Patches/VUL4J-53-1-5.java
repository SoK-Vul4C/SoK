void setTo(Calendar c, int i) {
    if (field < Calendar.YEAR || field > Calendar.MILLISECOND) {
        System.out.println("Invalid Calendar field");
        return;
    }

    int fieldValue = i - offset;

    switch (field) {
        case Calendar.YEAR:
            c.set(Calendar.YEAR, fieldValue);
            break;
        case Calendar.MONTH:
            c.set(Calendar.MONTH, fieldValue);
            break;
        case Calendar.DAY_OF_MONTH:
            c.set(Calendar.DAY_OF_MONTH, fieldValue);
            break;
        case Calendar.HOUR_OF_DAY:
            c.set(Calendar.HOUR_OF_DAY, fieldValue);
            break;
        case Calendar.MINUTE:
            c.set(Calendar.MINUTE, fieldValue);
            break;
        case Calendar.SECOND:
            c.set(Calendar.SECOND, fieldValue);
            break;
        case Calendar.MILLISECOND:
            c.set(Calendar.MILLISECOND, fieldValue);
            break;
        default:
            System.out.println("Unsupported Calendar field");
            break;
    }
}