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
        for (CalendarField f : CalendarField.ADJUST_ORDER) {
            int cur = f.valueOf(cal);
            int next = f.ceil(this, cur);
            if (cur == next) {
                // This field is already in a good shape, move on to the next field
                continue;
            }

            // We are modifying this field, so clear all the lower level fields
            for (CalendarField l = f.lowerField; l != null; l = l.lowerField) {
                l.clear(cal);
            }

            if (next < 0) {
                // We need to roll over to the next field
                f.rollUp(cal, 1);
                f.setTo(cal, f.first(this));
                // Restart adjustment for all fields from the top
                continue OUTER;
            } else {
                f.setTo(cal, next);
                if (f.redoAdjustmentIfModified) {
                    // Redo adjustment for all fields from the top
                    // Break out of the inner loop to restart from the top
                    continue OUTER;
                }
            }
        }
        return cal; // All fields adjusted
    }
}