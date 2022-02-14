package com.aaa.schooltracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.aaa.schooltracker.util.data.Grade;
import com.aaa.schooltracker.util.data.Subject;
import com.aaa.schooltracker.util.data.Year;
import com.aaa.schooltracker.util.data.event.Event;
import com.aaa.schooltracker.util.data.event.EventType;
import java.util.ArrayList;
import java.util.Locale;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Controls and maintains the sql database
 * @author Abdallah Alqashqish
 * @version v3.1
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
    // Database name
    private static final String DATABASE_NAME = "SmartGradeTracker.db";
    private static final int DATABASE_VERSION = 3;

    // Years table
    private static final String YEAR_TABLE = "Year_Table";
    private static final String YEAR_ID = "year_id";
    private static final String COL_1Y = "School_Name";
    private static final String COL_2Y = "Grade";
    private static final String COL_3Y = "Beginning_Year";
    private static final String COL_4Y = "Ending_Year";

    // Subject Table
    private static final String SUBJECT_TABLE = "Subject_Table";
    private static final String SUBJECT_ID = "subject_id";
    private static final String COL_1S = "Subject_Name";
    private static final String COL_2S = YEAR_ID;

    // Grades table
    private static final String GRADES_TABLE = "Grades_Table";
    private static final String GRADE_ID = "grade_id";
    private static final String COL_1G = "Grade_Name";
    private static final String COL_2G = "Grade";
    private static final String COL_3G = "Full_Grade";
    private static final String COL_4G = "Average";
    private static final String COL_5G = SUBJECT_ID;
    private static final String COL_6G = YEAR_ID;

    // Events table
    private static final String EVENT_TABLE = "Event_Table";
    private static final String EVENT_ID = "event_id";
    private static final String COL_1E = "Day";
    private static final String COL_2E = "Month";
    private static final String COL_3E = "Subject";
    private static final String COL_4E = "Type";
    private static final String COL_5E = "isDone";

    /**
     *
     * @param context The context of the activity
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        // Create years table
        db.execSQL(
                String.format(
                        "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)",
                        YEAR_TABLE,
                        YEAR_ID,
                        COL_1Y,
                        COL_2Y,
                        COL_3Y,
                        COL_4Y
                )
        );

        // Create subject table
        db.execSQL(
                String.format(
                        "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER)",
                        SUBJECT_TABLE,
                        SUBJECT_ID,
                        COL_1S,
                        COL_2S
                )
        );

        // Create grades table
        db.execSQL(
                String.format(
                        "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s DOUBLE, %s DOUBLE, %s DOUBLE, %s INTEGER, %s INTEGER)",
                        GRADES_TABLE,
                        GRADE_ID,
                        COL_1G,
                        COL_2G,
                        COL_3G,
                        COL_4G,
                        COL_5G,
                        COL_6G
                )
        );

        // Create events table
        db.execSQL(
                String.format(
                        "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
                        EVENT_TABLE,
                        EVENT_ID,
                        COL_1E,
                        COL_2E,
                        COL_3E,
                        COL_4E,
                        COL_5E
                )
        );
    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade database from v1 to v3

        Log.d("DB", "Running upgrade");

        if (newVersion > oldVersion) {

            Log.d("DB", "Running upgrade tables");

            this.onCreate(db);

            // Copy year data
            db.execSQL(
                    String.format(
                            "INSERT INTO %s SELECT * FROM %s",
                            YEAR_TABLE,
                            "YearTable"
                    )
            );

            // Copy subject data
            db.execSQL(
                    String.format(
                            "INSERT INTO %s SELECT %s, %s, %s FROM %s GROUP BY %s",
                            SUBJECT_TABLE,
                            "ID",
                            "Subjects_Name",
                            "G_id",
                            "SubjectStorage",
                            "Subjects_Name"
                    )
            );

            // Copy grade data
            db.execSQL(
                    String.format(
                            "INSERT INTO %s(%s, %s, %s, %s, %s, %s) SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s",
                            GRADES_TABLE,
                            COL_1G,
                            COL_2G,
                            COL_3G,
                            COL_4G,
                            COL_5G,
                            COL_6G,
                            "Grades_Name",
                            "Grade",
                            "Full_Grade",
                            "Percentage",
                            "(SELECT Subject_Table.subject_id FROM Subject_Table WHERE Subject_Table.Subject_name=SubjectStorage.Subjects_Name)",
                            "G_id",
                            "SubjectStorage",
                            "Grades_Name != \"\""
                    )
            );

            // Copy event data
            db.execSQL(
                    String.format(
                            "INSERT INTO %s SELECT * FROM %s",
                            EVENT_TABLE,
                            "DateTeable"
                    )
            );

            db.execSQL("DROP TABLE IF EXISTS YearTable");
            db.execSQL("DROP TABLE IF EXISTS SubjectStorage");
            db.execSQL("DROP TABLE IF EXISTS DateTeable");
        }
    }

    /*The functions that get, add and delete information from the YearTable
     * insertYear()
     * getYears()
     * deleteYear()
     */

    /**
     *
     * Insert a year into the database
     * @since v3.1
     * @param year The year to insert into the database
     */
    public final void insertYear(@NotNull Year year) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Save values to corresponding table columns
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1Y, year.getSchoolName());
        contentValues.put(COL_2Y, year.getGrade());
        contentValues.put(COL_3Y, year.getBegin());
        contentValues.put(COL_4Y, year.getEnd());

        // Insert into database
        db.insert(YEAR_TABLE, COL_1Y, contentValues);
    }

    /**
     *
     * Get all the years in the database
     * @since v3.1
     * @return An ArrayList with all the Years in the database
     */
    @NotNull
    public final ArrayList<Year> getYears() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = String.format("SELECT * FROM %s", YEAR_TABLE);

        // Get year records
        Cursor res = db.rawQuery(query, null);

        ArrayList<Year> years = new ArrayList<Year>();

        // Save the retrieved Year records
        while(res.moveToNext()) {
            years.add(
                    new Year(
                            res.getInt(0),      // Database ID for this Year record
                            res.getString(1),   // The name of the school attended in this year
                            res.getString(2),   // The grade level of this year
                            res.getInt(3),      // Beginning year of the academic year
                            res.getInt(4)       // Ending year of the academic year
                    )
            );
        }

        res.close();

        return years;
    }

    /**
     *
     * Delete a year from the database
     * @since v3.1
     * @param year The year to delete from the database
     */
    public final void deleteYear(@NotNull Year year) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = String.format("WHERE %s = %s", YEAR_ID, year.getId());

        // Delete all records belonging to this year
        db.delete(YEAR_TABLE, whereClause, null);
        db.delete(SUBJECT_TABLE, whereClause, null);
        db.delete(GRADES_TABLE, whereClause, null);
    }

    /*
     * The functions that get, add and delete information from the Subject_Table
     * insertSubject()
     * getSubjects()
     * getSubjectNames()
     * deleteSubject()
     */

    /**
     *
     * Inserts a new subject into the database
     * @since v3.1
     * @param subject The subject to insert into the database
     */
    public final void insertSubject(@NotNull Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Add subject values to corresponding columns
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1S, subject.getName());
        contentValues.put(COL_2S, subject.getYearId());

        db.insert(SUBJECT_TABLE, COL_1S, contentValues);
    }

    /**
     *
     * Gets all subjects corresponding to inputted year
     * @since v3.1
     * @param year The year corresponding to the subjects
     * @return An ArrayList with all the subjects
     */
    @NotNull
    public final ArrayList<Subject> getSubjects(@NotNull Year year) {
        SQLiteDatabase db = this.getWritableDatabase();

        String tableColFormat = "%s.%s";

        /**
         * Query:
         * SELECT *,
         *        (SELECT SUM(Grade) FROM Grades_Table WHERE Grades_Table.subject_id=Subject_Table.subject_id),
         *        (SELECT SUM(Full_Grade) FROM Grades_Table WHERE Grades_Table.subject_id=Subject_Table.subject_id)
         * FROM Subject_Table
         * WHERE Subject_Table.year_id=yearID
         * GROUP BY Subject_Table.subject_id
         * ORDER BY Subject_Table.subject_id
         */
        String query = String.format(
                "SELECT %s, (SELECT SUM(%s) FROM %s WHERE %s=%s), (SELECT SUM(%s) FROM %s WHERE %s=%s) " +
                "FROM %s WHERE %s=%s GROUP BY %s ORDER BY %s",
                "*",                                                        // *
                COL_2G,                                                     // Grade
                GRADES_TABLE,                                               // Grades_Table
                String.format(tableColFormat, GRADES_TABLE, COL_5G),        // Grades_Table.subject_id
                String.format(tableColFormat, SUBJECT_TABLE, SUBJECT_ID),   // Subject_Table.subject_id
                COL_3G,                                                     // Full_Grade
                GRADES_TABLE,                                               // Grades_Table
                String.format(tableColFormat, GRADES_TABLE, COL_5G),        // Grades_Table.subject_id
                String.format(tableColFormat, SUBJECT_TABLE, SUBJECT_ID),   // Subject_Table.subject_id
                SUBJECT_TABLE,                                              // Subject_Table
                COL_2S,                                                     // year_id
                year.getId(),                                               // The ID of the year
                String.format(tableColFormat, SUBJECT_TABLE, SUBJECT_ID),   // Subject_Table.subject_id
                String.format(tableColFormat, SUBJECT_TABLE, SUBJECT_ID)    // Subject_Table.subject_id
        );

        // Get subject records
        Cursor subjectData = db.rawQuery(query, null);

        ArrayList<Subject> subjectArray = new ArrayList<Subject>();

        // Save the retrieved Subject records
        while(subjectData.moveToNext()) {
            // Calculate subject average
            double achievedGrade = subjectData.getDouble(3);
            double maxAchievableGrade = subjectData.getDouble(4);
            double average = maxAchievableGrade == 0 ? 0 : (achievedGrade / maxAchievableGrade) * 100;

            subjectArray.add(
                    new Subject(
                            subjectData.getInt(0),                              // Subject database ID
                            subjectData.getInt(2),                              // Subject year database ID
                            subjectData.getString(1),                           // The subjects name
                            average                                             // The average
                    )
            );
        }

        subjectData.close();

        return subjectArray;
    }

    /**
     *
     * Get all names of subjects corresponding to the inputted year
     * @since v3.1
     * @param year The year the user is currently browsing
     * @return An arraylist with all the subject names corresponding to the inputted year
     */
    @NotNull
    public final ArrayList<String> getSubjectNames(@NotNull Year year) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = String.format(
                "SELECT %s FROM %s WHERE %s=%s",
                COL_1S,
                SUBJECT_TABLE,
                COL_2S,
                year.getId()
        );

        // Get subject names
        Cursor subjectNames = db.rawQuery(query, null);

        ArrayList<String> subjectNamesArr = new ArrayList<String>();

        // Save all subject names
        while(subjectNames.moveToNext()) {
            subjectNamesArr.add(subjectNames.getString(0));
        }

        subjectNames.close();

        return subjectNamesArr;
    }

    /**
     *
     * Deletes a specific subject from the database
     * @since v3.1
     * @param subject The subject to delete
     */
    public final void deleteSubject(@NotNull Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = String.format("%s=%s", SUBJECT_ID, subject.getId());

        db.delete(SUBJECT_TABLE, whereClause, null);
        db.delete(GRADES_TABLE, whereClause, null);
    }

    /*
     * The functions that get, add and delete information from the Grades_Table
     * insertGrade()
     * updateGrade()
     * getGrades()
     */

    /**
     *
     * Inserts a new grade into the database
     * @since v3.1
     * @param grade The grade to insert into the database
     */
    public final void insertGrade(@NotNull Grade grade) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Save grade values to corresponding column
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1G, grade.getName());
        contentValues.put(COL_2G, grade.getGrade());
        contentValues.put(COL_3G, grade.getMaxGrade());
        contentValues.put(COL_4G, grade.getAverage());
        contentValues.put(COL_5G, grade.getSubjectId());
        contentValues.put(COL_6G, grade.getYearId());

        db.insert(GRADES_TABLE, COL_1G, contentValues);
    }

    /**
     *
     * Updates a specific grade
     * @since v3.1
     * @param grade The grade to update
     */
    public final void updateGrade(@NotNull Grade grade) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Save grade values to corresponding column
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1G, grade.getName());
        contentValues.put(COL_2G, grade.getGrade());
        contentValues.put(COL_3G, grade.getMaxGrade());
        contentValues.put(COL_4G, grade.getAverage());
        contentValues.put(COL_5G, grade.getSubjectId());
        contentValues.put(COL_6G, grade.getYearId());

        /**
         * whereClause:
         * grade_id=gradeID AND subject_id=subjectID AND year_id=yearID
         */
        String whereClause = String.format(
                "%s=%s AND %s=%s AND %s=%s",
                GRADE_ID,
                grade.getId(),
                SUBJECT_ID,
                grade.getSubjectId(),
                YEAR_ID,
                grade.getYearId()
        );

        db.update(GRADES_TABLE, contentValues, whereClause, null);
    }

    /**
     *
     * Gets the all the grades corresponding to a specific subject
     * @since v3.1
     * @param subject The subject of the grades
     * @return An ArrayList of grades corresponding to the subject
     */
    @NotNull
    public final ArrayList<Grade> getGrades(@NotNull Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        /**
         * query:
         * SELECT * FROM Grades_Table
         * WHERE subject_id=subjectID AND year_id=yearID
         */
        String query = String.format(
                "SELECT * FROM %s WHERE %s=%s AND %s=%s",
                GRADES_TABLE,
                SUBJECT_ID,
                subject.getId(),
                YEAR_ID,
                subject.getYearId()
        );

        // Get the grade records
        Cursor gradesCursor = db.rawQuery(query, null);

        ArrayList<Grade> grades = new ArrayList<Grade>();

        // Save all the retrieved Grade records
        while(gradesCursor.moveToNext()) {
            grades.add(
                    new Grade(
                            gradesCursor.getInt(0),     // Grade database ID
                            gradesCursor.getInt(5),     // Grade subject database ID
                            gradesCursor.getInt(6),     // Grade year database ID
                            gradesCursor.getString(1),  // Grade name
                            gradesCursor.getDouble(2),  // Achieved grade
                            gradesCursor.getDouble(3)   // Max achievable grade
                    )
            );
        }

        gradesCursor.close();

        return grades;
    }

    /**
     *
     * Delete a grade from the database
     * @since v3.1
     * @param grade The grade to delete from the database
     */
    public final void deleteGrade(@NotNull Grade grade) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = String.format("%s=%s", GRADE_ID, grade.getId());

        db.delete(GRADES_TABLE, whereClause, null);
    }

    /*The functions that get, add and delete information from the DateTable
     * insertEvent()
     * updateEvent()
     * getEvent()
     * deleteEvent()
     * clearEvents()
     */

    /**
     *
     * Inserts an event into the database
     * @since v3.1
     * @param event The event to insert into the database
     */
    public final void insertEvent(@NotNull Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Save Event values to corresponding column
        ContentValues values = new ContentValues();
        values.put(COL_1E, event.getDay());
        values.put(COL_2E, event.getMonth());
        values.put(COL_3E, event.getSubject());
        values.put(COL_4E, event.getType().toString());
        values.put(COL_5E, event.getStatus());

        db.insert(EVENT_TABLE, COL_1E, values);
    }

    /**
     *
     * Updates an event in the database
     * @since v3.1
     * @param event The event with new data to update
     */
    public final void updateEvent(@NotNull Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Save Event values to corresponding column
        ContentValues values = new ContentValues();
        values.put(COL_1E, event.getDay());
        values.put(COL_2E, event.getMonth());
        values.put(COL_3E, event.getSubject());
        values.put(COL_4E, event.getType().toString());
        values.put(COL_5E, event.getStatus());

        String whereClause = String.format("%s=%s", EVENT_ID, event.getId());

        db.update(EVENT_TABLE, values, whereClause, null);
    }

    /**
     *
     * Gets all the events from the database
     * @since v3.1
     * @return An ArrayList with all the events in the database
     */
    @NotNull
    public final ArrayList<Event> getEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = String.format("SELECT * FROM %s", EVENT_TABLE);

        // Get event records
        Cursor dbEvents = db.rawQuery(query,null);

        ArrayList<Event> events = new ArrayList<Event>();

        while(dbEvents.moveToNext()) {
            events.add(
                    new Event(
                            dbEvents.getInt(0),                         // Event database ID
                            dbEvents.getString(1),                      // Day of the month
                            dbEvents.getString(2),                      // Month of the year 1-12
                            dbEvents.getString(3),                      // Event subject name
                            EventType.valueOf(dbEvents.getString(4)),   // Event type
                            dbEvents.getInt(5)                          // Event status
                    )
            );
        }

        dbEvents.close();

        return events;
    }

    /**
     * Deletes an event from the database
     * @since v3.1
     * @param event The event to delete from the database
     */
    public final void deleteEvent(@NotNull Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = String.format("%s=%s", EVENT_ID, event.getId());

        db.delete(EVENT_TABLE, whereClause, null);
    }

    /**
     *
     * Deletes all events in the database
     * @since v3.1
     */
    public final void clearEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(EVENT_TABLE, null, null);
    }
}
