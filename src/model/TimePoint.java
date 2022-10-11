package model;

import java.time.LocalDate;

public class TimePoint {
    private final LocalDate year;
    private final LocalDate month;
    private final LocalDate day;

    public TimePoint(LocalDate year, LocalDate month, LocalDate day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public LocalDate getYear() {
        return year;
    }

    public LocalDate getMonth() {
        return month;
    }

    public LocalDate getDay() {
        return day;
    }
}
