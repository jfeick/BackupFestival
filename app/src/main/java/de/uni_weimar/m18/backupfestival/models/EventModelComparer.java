package de.uni_weimar.m18.backupfestival.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class EventModelComparer implements Comparator<EventModel> {
    @Override
    public int compare(EventModel lhs, EventModel rhs) {
        // TODO: Handle null x or y values
        int startComparison = compare(lhs.getStartTimeString(), rhs.getStartTimeString());
        return startComparison != 0 ? startComparison
                : compare(lhs.getEndTimeString(), rhs.getEndTimeString());
    }

    // I don't know why this isn't in Long...
    private static int compare(String lhs, String rhs) {
        DateFormat formatter = new SimpleDateFormat("hh:mm");
        try {
            Date lhsDate = formatter.parse(lhs);
            Date rhsDate = formatter.parse(rhs);
            return lhsDate.before(rhsDate) ? -1
                    : lhsDate.after(rhsDate) ? 1
                    : 0;
        } catch (ParseException e) {
            return 1;
        }
    }
}