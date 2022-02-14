/**
 * Name: AddGradePopup.kt
 * Date: 2/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the grade add popup
 */

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


class AddGradePopup constructor(private val db: DatabaseHelper) : AppCompatDialogFragment() {

    private var interfaces: AddGradeInterface? = null

    //The onCreateDialog method
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
                    val percent = grade / fullGrade * 100
                    interfaces?.addGrade(gradeName, grade, fullGrade, percent)

                    //Dismiss the dialog
                    alertDialog.dismiss()
                }
            } catch (e: NumberFormatException) {
                //If the user didn't insert a grade or a fullGrade
                if (gradeEditText.text.isEmpty() || fullGradeEditText.text.isEmpty())
                    Toast.makeText(context, "Can not have an empty field", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Can not have Letters for a grade", Toast.LENGTH_LONG).show()
            }
        }

        return alertDialog
    }

    /**
     * Name: onAttach
     * Date: 2/11/2020
     * Functionality: Sets up the interface using context
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        interfaces = context as AddGradeInterface
    }

    //The interface
    interface AddGradeInterface {
        fun addGrade(GradeName: String?, Grade: Double?, fullGrade: Double?, Percent: Double?)
    }

}