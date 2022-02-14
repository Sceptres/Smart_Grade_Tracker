package com.aaa.schooltracker.util.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains all data of a subject
 * @author Abdallah Alqashqish
 * @version v1.0
 */
public class Subject implements Parcelable {

    // Field variables
    private final int id;
    private final int yearId;
    private String name;
    private double average;
    private char sChar;

    /**
     *
     * @since v1.0
     * @param id The database id of the subject
     * @param yearId The database id of the year that the subject belongs to
     * @param name Subject name
     * @param average Subject average
     */
    public Subject(int id, int yearId, String name, double average) {
        this.id = id;
        this.yearId = yearId;
        this.name = name;
        this.average = average;
        this.setsChar();
    }

    /**
     *
     * @since v1.0
     * @param yearId The id of the year this subject belongs to
     * @param name Subject name
     * @param average Subject average
     */
    public Subject(int yearId, String name, double average) {
        this.id = -1;
        this.yearId = yearId;
        this.name = name;
        this.average = average;
        this.setsChar();
    }

    /**
     *
     * @since v1.0
     * @param in Parcel to recreate the instance
     */
    protected Subject(Parcel in) {
        // Read data from parcel
        int[] ids = new int[2];
        in.readIntArray(ids);
        String subjectName = in.readString();
        double subjectAverage = in.readDouble();

        // Save data
        this.id = ids[0];
        this.yearId = ids[1];
        this.name = subjectName;
        this.setAverage(subjectAverage);
        this.setsChar();
    }

    /**
     *
     * {@link Subject#id} getter method
     * @since v1.0
     * @return The subject database id
     */
    public int getId() {
        return this.id;
    }

    /**
     *
     * {@link Subject#yearId} getter method
     * @since v1.0
     * @return Subject year id
     */
    public int getYearId() {
        return this.yearId;
    }

    /**
     * {@link Subject#name} getter method
     * @since v1.0
     * @return Subject name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * {@link Subject#name} setter method
     * @since v1.0
     * @param name Subject name
     */
    public void setName(String name) {
        this.name = name;
        this.setsChar();
    }

    /**
     * {@link Subject#average} getter method
     * @since v1.0
     * @return Subject average
     */
    public double getAverage() {
        return this.average;
    }

    /**
     * {@link Subject#average} setter method
     * @since v1.0
     * @param average The subject average
     */
    public void setAverage(double average) {
        this.average = average * 100;
    }

    /**
     * {@link Subject#sChar} getter method
     * @since v1.0
     * @return First character in subject name
     */
    public char getChar() {
        return this.sChar;
    }

    /**
     * {@link Subject#sChar} setter method
     * @since v1.0
     */
    private void setsChar() {
        this.sChar = Character.toUpperCase(this.name.charAt(0));
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
        dest.writeIntArray(new int[]{this.id, this.yearId});
        dest.writeString(this.name);
        dest.writeDouble(this.average);
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}
