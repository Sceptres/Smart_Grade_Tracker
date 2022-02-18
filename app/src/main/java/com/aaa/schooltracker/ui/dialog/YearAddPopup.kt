/**
 * Name: YearAddPopup.kt
 * Date: 2/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the add year popup
 */

package com.aaa.schooltracker.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.R
import com.aaa.schooltracker.util.data.Year

/**
 * A dialog used to add a new year entry
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
class YearAddPopup constructor(private val db: DatabaseHelper) : AppCompatDialogFragment(), AdapterView.OnItemSelectedListener {

    private val years: ArrayList<Year> = ArrayList()

    private var nGrade = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mBuilder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater
        val mView = inflater?.inflate(R.layout.add_year_popup, null)

        //The input of the popup
        val schoolNameEditText: EditText = mView!!.findViewById(R.id.schoolNameEditText)
        val beginningEditText: EditText = mView.findViewById(R.id.beginingEditText)
        val endEditText: EditText = mView.findViewById(R.id.endEditText)

        //Set up the grade spinner
        val gradeSpinner: Spinner = mView.findViewById(R.id.gradeSpinner)
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.gradeNums, R.layout.myspinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gradeSpinner.adapter = adapter
        gradeSpinner.onItemSelectedListener = this

        //Get the existing years
        refresh()

        //Set up the alert dialog builder
        mBuilder.apply {
            setView(mView)
            setPositiveButton("Add") { _, _ ->}
            setNegativeButton("Cancel", null)
        }

        //Set up the alert dialog
        val alertDialog = mBuilder.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }

        //ClickListener for the positive button of dialog
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            try {
                //Get input
                val nSchoolName = schoolNameEditText.text.toString()
                val nBegin = beginningEditText.text.toString().toInt()
                val nEnd = endEditText.text.toString().toInt()

                if (schoolNameEditText.text.isEmpty()) //Did th user not add the schools name?
                    Toast.makeText(context, "Can not have an empty field!", Toast.LENGTH_LONG).show()
                //Does the year already exist
                else if (years.filter {year: Year -> year.begin == nBegin}.count() > 0 &&
                         years.filter {year: Year -> year.end == nEnd}.count() > 0)
                    Toast.makeText(context, "This year already exists!", Toast.LENGTH_LONG).show()
                // Are the years invalid? (i.e. negative of in wrong chronological order)
                else if ((nBegin >= nEnd) || (nBegin < 0) || (nEnd < 0)) {
                    Toast.makeText(context, "Invalid year input!", Toast.LENGTH_LONG).show()
                } else {
                    // Create year
                    val year = Bundle()
                    year.putParcelable(Constants.YEAR_KEY, Year(nSchoolName,
                                                                nGrade,
                                                                nBegin,
                                                                nEnd)
                    )

                    this.activity?.apply {
                        // Send data to Fragment
                        supportFragmentManager.setFragmentResult(Constants.ADD_YEAR_KEY, year)
                    }

                    //Dismiss the dialog
                    alertDialog.dismiss()
                }
            } catch (e: NumberFormatException) {
                //Did the user not add the years
                if (beginningEditText.text.isEmpty() || endEditText.text.isEmpty())
                    Toast.makeText(context, "Can not have an empty field", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Can not have Letters for a year", Toast.LENGTH_LONG).show()
            }
        }

        return alertDialog
    }

    /**
     * Gets the data from te database so we can check if the year exists
     */
    private fun refresh() {
        years.addAll(db.years)
   }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        nGrade = when (val grade = parent.getItemAtPosition(position).toString()) {
            "University First Year" -> "U1"
            "University Second Year" -> "U2"
            "University Third Year" -> "U3"
            "University Fourth Year" -> "U4"
            "College First Year" -> "C1"
            "College Second Year" -> "C2"
            else -> grade
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}