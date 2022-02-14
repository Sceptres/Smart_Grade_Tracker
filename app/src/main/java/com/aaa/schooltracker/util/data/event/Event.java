package com.aaa.schooltracker.util.data.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains all the data of an Event
 * @author Abdallah Alqashqish
 * @version v1.0
 */
public class Event implements Parcelable {

    private final int id;
    private String day;
    private String month;
    private String subject;
    private EventType type;
    private int status;

    /**
     *
     * @since v1.0
     * @param id ID in SQL database
     * @param day Day of event
     * @param month Month of event
     * @param subject Subject of event
     * @param type Type of event
     * @param status Status of event
     */
    public Event(int id, String day, String month, String subject, EventType type, int status) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.subject = subject;
        this.type = type;
        this.status = status;
    }

    /**
     * @since v1.0
     * @param day Day of event
     * @param month Month of event
     * @param subject Subject of event
     * @param type Type of event
     * @param status Status of event
     */
    public Event(String day, String month, String subject, EventType type, int status) {
        this.id = -1;
        this.day = day;
        this.month = month;
        this.subject = subject;
        this.type = type;
        this.status = status;
    }

    /**
     *
     * @since v1.0
     * @param in Parcel used to recreate the instance
     */
    protected Event(Parcel in) {
        int[] intVal = new int[2];
        in.readIntArray(intVal);

        String[] strVal = new String[3];
        in.readStringArray(strVal);

        this.id = intVal[0];
        this.day = strVal[0];
        this.month = strVal[1];
        this.subject = strVal[2];
        this.type = in.readParcelable(ClassLoader.getSystemClassLoader());
        this.status = 0;
    }

    /**
     * {@link Event#id} getter method
     * @since v1.0
     * @return Event id
     */
    public int getId() {
        return this.id;
    }

    /**
     * {@link Event#day} getter method
     * @since v1.0
     * @return Day of event
     */
    public String getDay() {
        return this.day;
    }

    /**
     * {@link Event#day} setter method
     * @since v1.0
     * @param day The new day in the month for the event
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * {@link Event#month} getter method
     * @since v1.0
     * @return Month of event
     */
    public String getMonth() {
        return this.month;
    }

    /**
     * {@link Event#month} setter method
     * @since v1.0
     * @param month The new month of the event
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * {@link Event#month} getter method
     * @since v1.0
     * @return Subject of event
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * {@link Event#subject} setter method
     * @since v1.0
     * @param subject The subject of the event
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * {@link Event#type} getter method
     * @since v1.0
     * @return Type of event. {@link EventType}
     */
    public EventType getType() {
        return this.type;
    }

    /**
     * {@link Event#type} setter method
     * @since v1.0
     * @param type The type of the event
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * {@link Event#status} getter method
     * @since v1.0
     * @return Status of event
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * {@link Event#status} setter method
     * @since v1.0
     * @param status The status of the event
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes data of instance to parcel so the instance can be recreated by createFromParcel method
     * @since v1.0
     * @param dest The destination parcel
     * @param flags Any flags that affect writing
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // [id, status]
        dest.writeIntArray(new int[]{this.id, this.status});
        // [day, month, subject]
        dest.writeStringArray(new String[]{this.day, this.month, this.subject});
        dest.writeParcelable(this.type, flags);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
