public Calendar ceil(Calendar cal) {
    Calendar twoYearsFuture = (Calendar) cal.clone();
    twoYearsFuture.add(Calendar.YEAR, 2);

    OUTER:
    while (true) {
        if (cal.compareTo(twoYearsFuture) > 0) {
            throw new RareOrImpossibleDateException();
        }

        boolean modified = false;
        
        for (CalendarField f : CalendarField.ADJUST_ORDER) {
            int cur = f.valueOf(cal);
            int next = f.ceil(this, cur);
            
            if (cur == next) {
                // this field is already in a good shape, move on to the next field
                continue;
            }

            for (CalendarField l = f.lowerField; l != null; l = l.lowerField) {
                l.clear(cal);
            }

            if (next < 0) {
                f.rollUp(cal, 1);
                f.setTo(cal, f.first(this));
                continue OUTER;
            } else {
                f.setTo(cal, next);
                if (f.redoAdjustmentIfModified) {
                    // If any field was modified, adjust all fields again from the beginning
                    modified = true;
                    break; // Exit the loop to restart from the beginning
                }
            }
        }
        
        if (modified) {
            continue OUTER; // Restart adjusting all fields from the beginning
        } else {
            return cal; // all fields adjusted
        }
    }
}