public Calendar ceil(Calendar cal) {
    Calendar twoYearsFuture = (Calendar) cal.clone();
    twoYearsFuture.add(Calendar.YEAR, 2);
    
    OUTER:
    while (true) {
        // Check if calendar exceeds two years into the future
        if (cal.compareTo(twoYearsFuture) > 0) {
            throw new RareOrImpossibleDateException();
        }

        boolean fieldModified = false;

        for (CalendarField f : CalendarField.ADJUST_ORDER) {
            int cur = f.valueOf(cal);
            int next = f.ceil(cal, cur);

            if (cur != next) {
                // Field modified, set the new value and clear lower fields
                f.setTo(cal, next);
                f.clearLowerFields(cal);
                fieldModified = true;
                
                if (f.redoAdjustmentIfModified) {
                    // Redo adjustment from the beginning if needed
                    continue OUTER;
                }
            }
        }

        // If no field modification triggers redo adjustment, return the adjusted calendar
        if (!fieldModified) {
            return cal;
        }
    }
}