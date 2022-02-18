package com.aaa.schooltracker.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.R
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.Grade
import com.aaa.schooltracker.util.data.Subject

/**
 * A dialog used to add new grades to a specific subject
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
class AddGradePopup constructor(private val subject: Subject) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mBuilder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater
        val mView = inflater?.inflate(R.layout.activity_add_grade_popup, null)

        //Set the input EditTexts
        val gradeNameEditText: EditText = mView!!.findViewById(R.id.gradeName)
        val gradeEditText: EditText = mView.findViewById(R.id.grade)
        val fullGradeEditText: EditText = mView.findViewById(R.id.fullGrade)

        //Set up the builder
        mBuilder.apply {
            setView(mView)
            setPositiveButton("Add") {_, _ -> }
            setNegativeButton("Cancel", null)
        }

        //Set up the alert dialog
        val alertDialog = mBuilder.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }

        //ClickListener for the positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            try {
                if (gradeNameEditText.text.toString().isEmpty() || gradeEditText.text.isEmpty() || fullGradeEditText.text.isEmpty())
                    Toast.makeText(context, "Can not have an empty field", Toast.LENGTH_LONG).show()
                else {
                    //Get and send the info
                    val gradeName: String = gradeNameEditText.text.toString()
                    val grade: Double = gradeEditText.text.toString().toDouble()
                    val fullGrade: Double = fullGradeEditText.text.toString().toDouble()

                    this.activity?.apply {
                        // Save new grade
                        val bundle = Bundle()
                        bundle.putParcelable(Constants.GRADE_KEY, Grade(subject.id, subject.yearId, gradeName, grade, fullGrade))

                        // Send data to fragments
                        supportFragmentManager.setFragmentResult(Constants.ADD_GRADE_KEY, bundle)
                    }

                    //Dismiss the dialog
                    alertDialog.dismiss()
                }
            } catch (e: NumberFormatException) {
                //If the user didn't insert a grade or a fullGrade
                if (gradeEditText.text.isEmpty() || fullGradeEditText.text.isEmpty())
                    Toast.makeText(context, "Can not have an empty field", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Can not have letters for a mark", Toast.LENGTH_LONG).show()
            }
        }

        return alertDialog
    }
}