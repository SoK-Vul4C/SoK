public Calendar ceil(Calendar cal) {
    Calendar twoYearsFuture = (Calendar) cal.clone();
    twoYearsFuture.add(Calendar.YEAR, 2);
    OUTER:
    while (true) {
        if (cal.compareTo(twoYearsFuture) > 0) {
            // we went too far into the future
            throw new RareOrImpossibleDateException();
        }
        boolean modificationMade = false; // Flag to track if any modification is made in this iteration
        boolean redoAdjustment = false; // Flag to track if redo adjustment is needed

        for (CalendarField f : CalendarField.ADJUST_ORDER) {
            int cur = f.valueOf(cal);
            int next = f.ceil(this, cur);

            if (cur == next) {
                continue; // No modification needed, move to the next field
            }

            // Clear lower level fields as we are modifying this field
            for (CalendarField l = f.lowerField; l != null; l = l.lowerField) {
                l.clear(cal);
            }

            if (next < 0) {
                // Roll over to the next field
                f.rollUp(cal, 1);
                f.setTo(cal, f.first(this));
                redoAdjustment = true;

                // Restart from the top as higher-order field is affected
                continue OUTER;
            } else {
                f.setTo(cal, next);
                modificationMade = true;

                if (f.redoAdjustmentIfModified) {
                    redoAdjustment = true; // Need to redo adjustment if modified
                }
            }
        }

        if (modificationMade && redoAdjustment) {
            // If modification is made and redo adjustment is needed, continue adjusting fields
            continue OUTER;
        } else {
            // Return the modified calendar
            return cal;
        }
    }
}