package com.example.calendarapp;

public class DayData {
    private String startTime;
    private String endTime;
    private String note;
    private boolean isAlertEnabled;

    public DayData(String startTime, String endTime, String note, boolean isAlertEnabled) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.note = note;
        this.isAlertEnabled = isAlertEnabled;
    }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public boolean isAlertEnabled() { return isAlertEnabled; }
    public void setAlertEnabled(boolean alertEnabled) { isAlertEnabled = alertEnabled; }
}

