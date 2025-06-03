public Calendar ceil(Calendar cal) {
    Calendar twoYearsFuture = (Calendar) cal.clone();
    twoYearsFuture.add(Calendar.YEAR, 2);
    OUTER:
    while (true) {
        if (cal.compareTo(twoYearsFuture) > 0) {
            // we went too far into the future
            throw new RareOrImpossibleDateException();
        }
        for (CalendarField f : CalendarField.ADJUST_ORDER) {
            int cur = f.valueOf(cal);
            int next = f.ceil(this, cur);
            if (cur == next) continue;   // this field is already in a good shape. move on to next

            // we are modifying this field, so clear all the lower level fields
            for (CalendarField l = f.lowerField; l != null; l = l.lowerField)
                l.clear(cal);

            if (next < 0) {
                // we need to roll over to the next field.
                f.rollUp(cal, 1);
                f.setTo(cal, f.first(this));
                // since higher order field is affected by this, we need to restart from all over
                continue OUTER;
            } else {
                f.setTo(cal, next);
                if (f.redoAdjustmentIfModified(cur, next)) // Fixed Line
                    continue OUTER; // when we modify DAY_OF_MONTH and DAY_OF_WEEK, do it all over from the top
            }
        }
        return cal; // all fields adjusted
    }
}