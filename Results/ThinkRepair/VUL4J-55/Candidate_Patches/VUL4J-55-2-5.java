public Calendar ceil(Calendar cal) {
    // Create a clone of the input calendar
    Calendar originalCal = (Calendar) cal.clone();

    // Define the two-years future calendar for comparison
    Calendar twoYearsFuture = (Calendar) cal.clone();
    twoYearsFuture.add(Calendar.YEAR, 2);

    // Outer loop to iterate over adjustments
    OUTER: while (true) {
        if (cal.compareTo(twoYearsFuture) > 0) {
            // If the calendar date is too far in the future, throw an exception
            throw new RareOrImpossibleDateException();
        }
        
        boolean modified = false; // Flag to track if modifications are made
        
        // Iterate over calendar fields in adjust order
        for (CalendarField f : CalendarField.ADJUST_ORDER) {
            int cur = f.valueOf(cal); // Get current value of the field
            int next = f.ceil(this, cur); // Get the next value after adjustment
            
            if (cur == next) continue; // Move to the next field if no adjustment needed
            
            for (CalendarField l = f.lowerField; l != null; l = l.lowerField) {
                l.clear(cal); // Clear lower level fields if adjustments are made
            }

            if (next < 0) {
                f.rollUp(cal, 1); // Roll over to the next field if necessary
                f.setTo(cal, f.first(this)); // Set the field to the initial value
                continue OUTER; // Restart the adjustment process from the beginning
            } else {
                f.setTo(cal, next); // Set the field to the next value
                if (f.redoAdjustmentIfModified(cur, next)) {
                    modified = true; // Set modified flag if adjustment is redone
                    break; // Break the loop to restart adjustment process
                }
            }
        }
        
        if (!modified) {
            return cal; // Return the adjusted calendar if no further modifications needed
        }
    }
}