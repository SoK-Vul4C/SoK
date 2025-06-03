void setTo(Calendar c, int i) {
    switch (field) {
        case Calendar.ERA:
        case Calendar.YEAR:
        case Calendar.MONTH:
        case Calendar.WEEK_OF_YEAR:
        case Calendar.WEEK_OF_MONTH:
        case Calendar.DAY_OF_MONTH:
        case Calendar.DAY_OF_YEAR:
        case Calendar.DAY_OF_WEEK:
        case Calendar.DAY_OF_WEEK_IN_MONTH:
        case Calendar.AM_PM:
        case Calendar.HOUR:
        case Calendar.HOUR_OF_DAY:
        case Calendar.MINUTE:
        case Calendar.SECOND:
        case Calendar.MILLISECOND:
            int adjustedValue = i - offset;
            c.set(field, adjustedValue);
            break;
        default:
            System.out.println("Invalid Calendar field: " + field);
            break;
    }
}