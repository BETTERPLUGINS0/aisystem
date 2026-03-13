/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.seasons.handlers.sub;

public class CalendarData {
    private int yearDay;
    private int transitionPassedTime = 0;

    public void increaseYearDay() {
        ++this.yearDay;
    }

    CalendarData(int n, int n2) {
        this.yearDay = n;
        this.transitionPassedTime = n2;
    }

    public static CalendarDataBuilder builder() {
        return new CalendarDataBuilder();
    }

    public int getYearDay() {
        return this.yearDay;
    }

    public int getTransitionPassedTime() {
        return this.transitionPassedTime;
    }

    public void setYearDay(int n) {
        this.yearDay = n;
    }

    public void setTransitionPassedTime(int n) {
        this.transitionPassedTime = n;
    }

    public static class CalendarDataBuilder {
        private int yearDay;
        private int transitionPassedTime;

        CalendarDataBuilder() {
        }

        public CalendarDataBuilder yearDay(int n) {
            this.yearDay = n;
            return this;
        }

        public CalendarDataBuilder transitionPassedTime(int n) {
            this.transitionPassedTime = n;
            return this;
        }

        public CalendarData build() {
            return new CalendarData(this.yearDay, this.transitionPassedTime);
        }

        public String toString() {
            return "CalendarData.CalendarDataBuilder(yearDay=" + this.yearDay + ", transitionPassedTime=" + this.transitionPassedTime + ")";
        }
    }
}

