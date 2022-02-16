/**
 * Name: UpdatePopup.kt
 * Date: 3/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the update grade popup
 */

package com.aaa.schooltracker.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.R
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.Grade


class UpdatePopup constructor(private val grade: Grade) : AppCompatDialogFragment() {

    //The onCreateDialog method
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mBuilder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater
        val mView = inflater?.inflate(R.layout.activity_update_popup, null)

        //Get input edit texts
        val gradeNameEditText: EditText = mView!!.findViewById(R.id.GradeNameEditText)
        val gradeEditText: EditText = mView.findViewById(R.id.GradeEditText)
        val fullGradeEditText: EditText = mView.findViewById(R.id.fullGradeEditText)
        val gnameTextView: TextView = mView.findViewById(R.id.GradesNameTextView)

        //Display the grades data
        gnameTextView.text = grade.name
        gradeNameEditText.setText(grade.name)
        gradeEditText.setText(grade.grade.toString())
        fullGradeEditText.setText(grade.maxGrade.toString())

        //Set up the mBuilder
        mBuilder.apply {
            setView(mView)
            setPositiveButton("Update") { _, _ ->}
            setNegativeButton("Cancel", null)
        }

        //Set up the alert dialog
        val alertDialog = mBuilder.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            try {
                if (gradeNameEditText.text.isEmpty()) //Did the user keep the GradeNameEditText empty?
                    Toast.makeText(context, "Can not have an empty field", Toast.LENGTH_LONG).show()
                else {
                    //Get the data inserted by the user
                    val newGradeName: String = gradeNameEditText.text.toString()
                    val newGrade: Double = gradeEditText.text.toString().toDouble()
                    val newFullGrade: Double = fullGradeEditText.text.toString().toDouble()

                    this.activity?.supportFragmentManager?.apply {
                        // Update the grade
                        grade.apply {
                            name = newGradeName
                            grade = newGrade
                            maxGrade = newFullGrade;
                        }

                        val bundle = Bundle()
                        bundle.putParcelable(Constants.GRADE_KEY, grade)

                        // Send updated grade to fragment
                        setFragmentResult(Constants.UPDATE_GRADE_KEY, bundle)
                    }

                    //Dismiss the dialog
                    alertDialog.dismiss()
                }
            } catch (e: NumberFormatException) {
                //Did the user keep the grade EditTexts empty?
                if (gradeEditText.text.isEmpty() || fullGradeEditText.text.isEmpty())
                    Toast.makeText(context, "Can not have an empty field", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Can not have letters for a grade", Toast.LENGTH_LONG).show()
            }
        }

        return alertDialog
    }
}