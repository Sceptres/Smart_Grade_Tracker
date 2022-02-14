/**
 * Name: EventCustomGrid.kt
 * Date: 7/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the event grid
 */

package com.aaa.schooltracker.ui.customgrid

import com.aaa.schooltracker.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aaa.schooltracker.util.data.event.Event
import java.lang.String.format


class EventCustomGrid constructor(private val c: Context,
                                  private val eventArray: ArrayList<Event>) : BaseAdapter() {
    override fun getCount(): Int {
        return eventArray.size
    }

    override fun getItem(position: Int): Any {
        return eventArray[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    //The getView method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //Set up the layout inflater
        val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //Set up the view
        val view = inflater.inflate(R.layout.eventcustomgrid, null)

        //All the TextViews that will display the data to the user
        val dayView: TextView = view.findViewById(R.id.dayView)
        val monthView: TextView = view.findViewById(R.id.monthView)
        val doneView: TextView = view.findViewById(R.id.doneView)
        val assignmentView: TextView = view.findViewById(R.id.assignmentView)

        val event: Event = eventArray[position]

        //Set the dayView to the day of the event
        dayView.text = event.day

        //Checks to which month and sets the monthView accordingly
        when (event.month) {
            "1" -> monthView.text = "January"
            "2" -> monthView.text = "February"
            "3" -> monthView.text = "March"
            "4" -> monthView.text = "April"
            "5" -> monthView.text = "May"
            "6" -> monthView.text = "June"
            "7" -> monthView.text = "July"
            "8" -> monthView.text = "August"
            "9" -> monthView.text = "September"
            "10" -> monthView.text = "October"
            "11" -> monthView.text = "November"
            "12" -> monthView.text = "December"
        }

        //Is the event upcoming
        if (event.status == 0)
            doneView.text = "Upcoming"
        else if (event.status == 1) //Is the event over
            doneView.text = "Completed"

        //Sets the assignmentView to subjectName + type
        assignmentView.text = format("%s %s", event.subject, event.type)

        return view
    }
}