package com.aaa.schooltracker.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.R
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.Subject
import com.aaa.schooltracker.util.data.Year

/**
 * A dialog used to add a new subject
 * @author Abdallah Alqashqish
 */
class AddSubjectPopup constructor(private val db: DatabaseHelper, private val year: Year) : AppCompatDialogFragment() {

    private val subjectNames: ArrayList<String> = ArrayList()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mBuilder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater
        val mView = inflater?.inflate(R.layout.add_subject_popup, null)

        val subjectNameET: EditText = mView!!.findViewById(R.id.subject_name)

        //Set up the alert dialog builder
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

        //Get the existing subjects
        refresh()

        //ClickListener for the alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            //Format the user input   math --> Math
            val subjectsName: String = subjectNameET.text.toString()
            val firstLetter: Char = subjectsName[0].toUpperCase()
            val subjectName = StringBuilder()
            subjectName.append(firstLetter)
            subjectName.append(subjectsName.substring(1))

            when{
                //Did the user not input anything
                subjectNameET.text.isEmpty() -> Toast.makeText(context, "Can not have an empty field", Toast.LENGTH_LONG).show()

                //Does this subject already exist?
                subjectNames.contains(subjectName.toString()) -> Toast.makeText(context, "Subject already exists", Toast.LENGTH_LONG).show()

                else -> {
                    //Send the subjects name
                    this.activity?.apply {
                        // Save new subject
                        val bundle = Bundle()
                        bundle.putParcelable(Constants.SUBJECT_KEY, Subject(year.id, subjectName.toString(), 0.0))

                        // Send data to Fragment
                        supportFragmentManager.setFragmentResult(Constants.ADD_SUBJECT_KEY, bundle)
                    }

                    //Dismiss the dialog
                    alertDialog.dismiss()
                }
            }
        }

        return alertDialog
    }

    /**
     * Gets all the subjects names for the corresponding year from the database
     */
    private fun refresh() {
        subjectNames.addAll(db.getSubjectNames(year))
    }
}