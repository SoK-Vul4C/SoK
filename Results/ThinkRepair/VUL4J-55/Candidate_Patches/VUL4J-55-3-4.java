public Calendar ceil(Calendar cal) {
    // Clone the calendar to have a separate instance
    Calendar twoYearsFuture = (Calendar) cal.clone();
    twoYearsFuture.add(Calendar.YEAR, 2);

    OUTER:
    while (true) {
        if (cal.compareTo(twoYearsFuture) > 0) {
            // We went too far into the future
            throw new RareOrImpossibleDateException();
        }

        boolean fieldModified = false;

        for (CalendarField f : CalendarField.ADJUST_ORDER) {
            int cur = f.valueOf(cal);
            int next = f.ceil(cal, cur);

            if (cur != next) {
                // Field was modified
                fieldModified = true;
                f.setTo(cal, next);
                f.clearLowerFields(cal);
            }

            if (fieldModified && f.redoAdjustmentIfModified) {
                continue OUTER; // Redo adjustment for all fields from the top
            }
        }

        return cal; // All fields adjusted
    }
}