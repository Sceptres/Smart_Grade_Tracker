package com.aaa.schooltracker.util.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.aaa.schooltracker.util.Constants;

/**
 * Contains all data of a single grade
 * @author Abdallah Alqashqish
 * @version v1.0
 */
public class Grade implements Parcelable {

    // Field variables
    private final int id;
    private final int subjectId;
    private final int yearId;
    private String name;
    private double grade;
    private double maxGrade;
    private double average;

    /**
     *
     * @since v1.0
     * @param id ID in SQL database
     * @param subjectId The database id of the Subject corresponding to this grade
     * @param yearId The database id of the Year corresponding to this grade's Subject
     * @param name Name of the grade
     * @param grade Achieved grade
     * @param maxGrade Maximum achievable grade
     */
    public Grade(Integer id, Integer subjectId, Integer yearId, String name, double grade, double maxGrade) {
        this.id = id;
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.name = name;
        this.grade = grade;
        this.maxGrade = maxGrade;
        this.setAverage();
    }

    /**
     *
     * @since v1.0
     * @param name Name of the grade
     * @param grade Achieved grade
     * @param maxGrade Maximum achievable grade
     */
    public Grade(Integer subjectId, Integer yearId, String name, double grade, double maxGrade) {
       this(-1, subjectId, yearId, name, grade, maxGrade);
    }

    /**
     *
     * @since v1.0
     * @param in Parcel to recreate the instance
     */
    protected Grade(Parcel in) {
        int[] ids = new int[3];
        in.readIntArray(ids);

        this.id = ids[0];
        this.subjectId = ids[1];
        this.yearId = ids[2];

        this.name = in.readString();

        double[] doubleVal = new double[2];
        in.readDoubleArray(doubleVal);

        this.grade = doubleVal[0];
        this.maxGrade = doubleVal[1];
        this.setAverage();
    }

    /**
     * {@link Grade#id} getter method
     * @since v1.0
     * @return Grade SQL id
     */
    public int getId() {
        return this.id;
    }

    /**
     *
     * {@link Grade#subjectId} getter method
     * @since v1.0
     * @return The grade Subject id
     */
    public int getSubjectId() {
        return this.subjectId;
    }

    /**
     *
     * {@link Grade#yearId} getter method
     * @since v1.0
     * @return The grade Subject year id
     */
    public int getYearId() {
        return this.yearId;
    }

    /**
     * {@link Grade#name} getter method
     * @since v1.0
     * @return Grade name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * {@link Grade#name} setter method
     * @since v1.0
     * @param name The name of the grade
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@link Grade#grade} getter method
     * @since v1.0
     * @return Grade grade achieved
     */
    public double getGrade() {
        return this.grade;
    }

    /**
     *
     * {@link Grade#grade} setter method
     * @since v1.0
     * @param grade The grade
     */
    public void setGrade(double grade) {
        this.grade = grade;
    }

    /**
     * {@link Grade#maxGrade} getter method
     * @since v1.0
     * @return Grade maximum achievable grade
     */
    public double getMaxGrade() {
        return this.maxGrade;
    }

    /**
     *
     * {@link Grade#maxGrade} setter method
     * @since v1.0
     * @param maxGrade The maximum achievable grade
     */
    public void setMaxGrade(double maxGrade) {
        this.maxGrade = maxGrade;
    }

    /**
     * {@link Grade#average} getter method
     * @since v1.0
     * @return Grade average percentage
     */
    public double getAverage() {
        String roundedAvg = Constants.df.format(this.average);
        return Double.parseDouble(roundedAvg);
    }

    /**
     *
     * {@link Grade#average} setter method
     * @since v1.0
     */
    private void setAverage() {
        this.average = (this.grade / this.maxGrade) * 100;
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
        dest.writeIntArray(new int[]{this.id, this.subjectId, this.yearId});
        dest.writeString(this.name);
        // [grade, maxGrade]
        dest.writeDoubleArray(new double[]{this.grade, this.maxGrade});
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };
}
