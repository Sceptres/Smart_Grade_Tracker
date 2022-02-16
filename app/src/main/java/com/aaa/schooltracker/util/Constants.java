/**
  Name: Constants.java
  Date: 31/10/2021
  @author: Abdallah Alqashqish
 * Contains all the constants required by the app
 */

package com.aaa.schooltracker.util;

import java.text.DecimalFormat;

public class Constants {

    // Key data for YearAddPopup Dialog
    public static final String ADD_YEAR_KEY =           "ADDYEAR";
    public static final String YEAR_KEY =               "YEAR";

    // Key data for AddSubjectPopup
    public static final String ADD_SUBJECT_KEY =        "ADDSUBJECT";
    public static final String SUBJECT_KEY =               "SUBJECT";

    // Key data for AddGradePopup
    public static final String ADD_GRADE_KEY =          "ADDGRADE";
    public static final String GRADE_KEY =              "GRADE";

    // Decimal Format to round data to 2 decimal places
    public static final DecimalFormat df = new DecimalFormat("###.##");
}
