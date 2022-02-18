package com.aaa.schooltracker.util;

import java.text.DecimalFormat;

/**
 * A class with all required constants used throughout the application
 *
 * @author Abdallah Alqashqish
 * @version v1.0
 */
public class Constants {

    // Key data for YearAddPopup Dialog
    public static final String ADD_YEAR_KEY =           "ADDYEAR";
    public static final String YEAR_KEY =               "YEAR";

    // Key data for AddSubjectPopup
    public static final String ADD_SUBJECT_KEY =        "ADDSUBJECT";
    public static final String SUBJECT_KEY =               "SUBJECT";

    // Key data for Grades
    public static final String ADD_GRADE_KEY =          "ADDGRADE";
    public static final String UPDATE_GRADE_KEY =       "UPDATEGRADE";
    public static final String GRADE_KEY =              "GRADE";

    // KEy data for Events
    public static final String ADD_EVENT_KEY =          "ADDEVENT";
    public static final String EVENT_KEY =              "EVENT";

    // Decimal Format to round data to 2 decimal places
    public static final DecimalFormat df = new DecimalFormat("###.##");
}
