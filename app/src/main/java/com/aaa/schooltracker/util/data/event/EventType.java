package com.aaa.schooltracker.util.data.event;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Represents the different types of events
 * <br>
 * 1. Test
 * <br>
 * 2. Quiz
 * <br>
 * 3. Assignment
 * @author Abdallah Alqashqish
 * @version v1.0
 */
public enum EventType implements Parcelable {
    TEST("Test"),
    QUIZ("Quiz"),
    ASSIGNMENT("Assignment");

    String name;

    /**
     *
     * @since v1.0
     * @param name Name of event type
     */
    EventType(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * This method is used as a replacement to the valueOf method as it cannot be overridden
     * @param str The string representation of the enum
     * @return The enum with a string value of str
     */
    public static EventType getEnum(String str) {
        if (str.equals(TEST.toString()))
            return TEST;
        else if (str.equals(QUIZ.toString()))
            return QUIZ;
        else if (str.equals(ASSIGNMENT.toString()))
            return ASSIGNMENT;
        else
            throw new IllegalArgumentException("An enum with the value of " + str + " does not exist!");
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
        dest.writeString(this.toString());
    }

    public static final Creator<EventType> CREATOR = new Creator<EventType>() {
        @Override
        public EventType createFromParcel(Parcel in) {
            return EventType.valueOf(EventType.class, in.readString());
        }

        @Override
        public EventType[] newArray(int size) {
            return new EventType[size];
        }
    };
}
