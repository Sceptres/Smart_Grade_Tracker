package com.aaa.schooltracker.util.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains all the data of a year
 * @author Abdallah Alqashqish
 * @version v1.0
 */

public class Year implements Parcelable {

    // Field variables
    private final int id;
    private String schoolName;
    private String grade;
    private int begin;
    private int end;

    /**
     *
     * @since v1.0
     * @param id SQL year id
     * @param schoolName Name of the school
     * @param grade Grade level
     * @param begin The beginning of the school year
     * @param end The end year of the school year
     */
    public Year(int id, String schoolName, String grade, int begin, int end) {
        this.id = id;
        this.schoolName = schoolName;
        this.grade = grade;
        this.begin = begin;
        this.end = end;
    }

    /**
     *
     * @since v1.0
     * @param schoolName Name of the school
     * @param grade Grade level
     * @param begin The beginning of the school year
     * @param end The end year of the school year
     */
    public Year(String schoolName, String grade, int begin, int end) {
        this.id = -1;
        this.schoolName = schoolName;
        this.grade = grade;
        this.begin = begin;
        this.end = end;
    }

    /**
     *
     * @since v1.0
     * @param in Parcel to use to recreate instance
     */
    protected Year(Parcel in) {
        int[] intVal = new int[3];
        in.readIntArray(intVal);

        String[] strVal = new String[2];
        in.readStringArray(strVal);

        this.id = intVal[0];
        this.schoolName = strVal[0];
        this.grade = strVal[1];
        this.begin = intVal[1];
        this.end = intVal[2];
    }

    /**
     * {@link Year#id} getter method
     * @since v1.0
     * @return Year id
     */
    public int getId() {
        return this.id;
    }

    /**
     * {@link Year#schoolName} getter method
     * @since v1.0
     * @return School year school name
     */
    public String getSchoolName() {
        return this.schoolName;
    }

    /**
     * {@link Year#schoolName} setter method
     * @since v1.0
     * @param schoolName The name of the school for this year
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * {@link Year#grade} getter method
     * @since v1.0
     * @return School year grade level
     */
    public String getGrade() {
        return this.grade;
    }

    /**
     * {@link Year#grade} setter method
     * @since v1.0
     * @param grade The grade/level for this year
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * {@link Year#begin} getter method
     * @since v1.0
     * @return School year beginning year
     */
    public int getBegin() {
        return this.begin;
    }

    /**
     * {@link Year#begin} setter method
     * @since v1.0
     * @param begin The beginning year of the academic year
     */
    public void setBegin(int begin) {
        this.begin = begin;
    }

    /**
     * {@link Year#end} getter method
     * @since v1.0
     * @return School year ending year
     */
    public int getEnd() {
        return this.end;
    }

    /**
     * {@link Year#end} setter method
     * @since v1.0
     * @param end The year when the academic year ends
     */
    public void setEnd(int end) {
        this.end = end;
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
        // [School name, Grade]
        dest.writeStringArray(new String[]{this.schoolName, this.grade});
        // [Id, Begin, End]
        dest.writeIntArray(new int[]{this.id, this.begin, this.end});
    }

    public static final Creator<Year> CREATOR = new Creator<Year>() {
        @Override
        public Year createFromParcel(Parcel in) {
            return new Year(in);
        }

        @Override
        public Year[] newArray(int size) {
            return new Year[size];
        }
    };
}
