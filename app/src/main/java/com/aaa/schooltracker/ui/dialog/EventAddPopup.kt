/**
 * Name: EventAddPopup.kt
 * Date: 2/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the event add popup
 */

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
import java.util.*
import kotlin.collections.ArrayList


class EventAddPopup : AppCompatDialogFragment() {

    private var interfaces: AddEventInterface? = null
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
            val type = typeSpinner.selectedItem.toString()

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

            Log.d("Today", today.toString())
            Log.d("Selected", selectDate.toString())

            when{
                subjectEditText.text.isEmpty() -> Toast.makeText(context, "Can not have an empty field.", Toast.LENGTH_LONG).show();

                selectDate.before(today) || isDateEquals(today, selectDate) ->
                    Toast.makeText(context, "You can only add events for the future.", Toast.LENGTH_LONG).show();

                else -> {
                    //Send the event
                    interfaces?.sendEventInfo(selectedDate[0].toString(), selectedDate[1].toString(), subjectName, type)

                    //Dismiss the dialog
                    alertDialog.dismiss()
                }
            }

            /*//Did the user not insert a subject
            if(subjectEditText.text.isEmpty())
                Toast.makeText(context, "Can not have an empty field.", Toast.LENGTH_LONG).show();
            //Is the day in the past
            else if(selectDate.before(today) || isDateEquals(today, selectDate))
                Toast.makeText(context, "You can only add events for the future.", Toast.LENGTH_LONG).show();
            //Otherwise send the users input
            else{
                //Send the event
                interfaces?.sendEventInfo(selectedDate[0].toString(), selectedDate[1].toString(), subjectName, type)

                //Dismiss the dialog
                alertDialog.dismiss()
            }*/
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
     * Name: datePicker
     * Date: 11/9/2020
     * Functionality: Gets the date of the event
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
     * Name: isDateEquals
     * Date: 8/11/2020
     * Functionality: Checks if 2 dates are equal
     * @return: true -> dates are equal
     *          false -> dates are not equal
     */
    private fun isDateEquals(d1: Calendar, d2: Calendar): Boolean{
        return d1.get(Calendar.DAY_OF_MONTH) == d2.get(Calendar.DAY_OF_MONTH) &&
               d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH) &&
               d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)
    }

    /**
     * Name: onAttach
     * Date: 2/11/2020
     * Functionality: Sets up the interface
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)

        interfaces = context as AddEventInterface
    }

    //The interface
    interface AddEventInterface {
        fun sendEventInfo(day: String?, month: String?, subjectName: String?, type: String?)
    }

}