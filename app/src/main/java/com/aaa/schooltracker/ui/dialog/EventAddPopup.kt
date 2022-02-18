package com.aaa.schooltracker.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import com.aaa.schooltracker.R
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.event.Event
import com.aaa.schooltracker.util.data.event.EventType
import java.util.*
import kotlin.collections.ArrayList

/**
 * A dialog used to add new events
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
class EventAddPopup : AppCompatDialogFragment() {

    private var selectedDate: ArrayList<Int> = getCurrentDate()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mBuilder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater
        val mView = inflater?.inflate(R.layout.add_event_popup, null)

        //The subjects name EditText
        val subjectEditText: EditText = mView!!.findViewById(R.id.evSubjectEditText)

        //Date textview
        val dateView: TextView = mView.findViewById(R.id.selectedDateView)
        dateView.text = "${selectedDate[0]}/${selectedDate[1]+1}/${selectedDate[2]}"

        //Date selection button
        val dateBtn: Button = mView.findViewById(R.id.dateSelectionBtn)
        dateBtn.setOnClickListener {
            selectedDate = datePicker(getCurrentDate(), dateView)
        }

        //The typeSpinner. Consists of the types of events.
        val typeSpinner: Spinner = mView.findViewById(R.id.eveTypeSpinner)
        val typeAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.types, R.layout.myspinner)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter

        //Set up the alert dialog builder
        mBuilder.apply {
            setView(mView)
            setPositiveButton("Add") { _, _ -> }
            setNegativeButton("Cancel", null)
        }

        //Set up the alert dialog
        val alertDialog = mBuilder.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }

        //ClickListener for the positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            //Get the info from the user
            val subjectName = subjectEditText.text.toString()
            val type = EventType.getEnum(typeSpinner.selectedItem.toString())

            val today = Calendar.getInstance().apply {
                val day = getCurrentDate()
                set(Calendar.DAY_OF_MONTH, day[0])
                set(Calendar.MONTH, day[1]+1)
                set(Calendar.YEAR, day[2])
            }

            val selectDate = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, selectedDate[0])
                set(Calendar.MONTH, selectedDate[1])
                set(Calendar.YEAR, selectedDate[2])
            }

            when{
                subjectEditText.text.isEmpty() -> Toast.makeText(context, "Can not have an empty field.", Toast.LENGTH_LONG).show()

                selectDate.before(today) || isDateEquals(today, selectDate) ->
                    Toast.makeText(context, "You can only add events for the future.", Toast.LENGTH_LONG).show()

                else -> {
                    this.activity?.apply {
                        val bundle = Bundle()
                        val event = Event(selectedDate[0].toString(), selectedDate[1].toString(), subjectName, type, 0)
                        bundle.putParcelable(Constants.EVENT_KEY, event)

                        // Send the event to the fragment
                        supportFragmentManager.setFragmentResult(Constants.ADD_EVENT_KEY, bundle)
                    }

                    //Dismiss the dialog
                    alertDialog.dismiss()
                }
            }
        }

        return alertDialog
    }

    /**
     * @return A list with the current day, month, and year in that order
     * */
    private fun getCurrentDate(): ArrayList<Int>{
        val myCalendar = Calendar.getInstance()
        //Get the current date
        return ArrayList<Int>().apply {
            add(myCalendar.get(Calendar.DAY_OF_MONTH))
            add(myCalendar.get(Calendar.MONTH))
            add(myCalendar.get(Calendar.YEAR))
        }
    }

    /**
     * @param dmyList: A list of the date.
     *                 0 -> Day
     *                 1 -> Month
     *                 2 -> Year
     * @return: An array list with the selected date
     * */
    private fun datePicker(dmyList: ArrayList<Int>, view: TextView): ArrayList<Int> {
        val dt: ArrayList<Int> = ArrayList()

        //Display the DatePickerDialog
        context?.let {
            DatePickerDialog(
                    it,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                        view.text = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"

                        dt.apply {
                            add(selectedDayOfMonth)
                            add(selectedMonth+1)
                            add(selectedYear)
                        }
                    }, dmyList[2],
                    dmyList[1],
                    dmyList[0]
            ).show()
        }

        return dt
    }

    /**
     * @return: true -> dates are equal
     *          false -> dates are not equal
     */
    private fun isDateEquals(d1: Calendar, d2: Calendar): Boolean{
        return d1.get(Calendar.DAY_OF_MONTH) == d2.get(Calendar.DAY_OF_MONTH) &&
               d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH) &&
               d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)
    }
}