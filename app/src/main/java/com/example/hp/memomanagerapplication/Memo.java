package com.example.hp.memomanagerapplication;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by hp on 11/03/2018.
 */

public class Memo {
    //TODO: MEMO class
    private String title;
    private String category;
    private String deadline;
    private String priorityLevel;
    private String notificationIntervals;
    private String notificationTime;
    private String status;
    private String note;

    public static final String TITLE_CODE="title";
    public static final String CATEGORY_CODE="category";
    public static final String DEADLINE_CODE="deadline";
    public static final String PRIORITYLEVEL_CODE="";
    public static final String NOTIFICATIONINTERVALS_CODE="notificiationIntervals";
    public static final String NOTIFICATIONTIME_CODE="notificationTime";
    public static final String STATUS_CODE="status";
    public static final String NOTE_CODE="note";

    public Memo(String title, String category, String deadline,
                String priorityLevel, String notificationIntervals,
                String notificationTime, String status, String note) {
        this.title = title;
        this.category = category;
        this.deadline = deadline;
        this.priorityLevel = priorityLevel;
        this.notificationIntervals = notificationIntervals;
        this.notificationTime = notificationTime;
        this.status = status;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getNotificationIntervals() {
        return notificationIntervals;
    }

    public void setNotificationIntervals(String notificationIntervals) {
        this.notificationIntervals = notificationIntervals;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
