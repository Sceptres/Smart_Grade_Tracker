package com.aaa.schooltracker.ui.customgrid

import com.aaa.schooltracker.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aaa.schooltracker.util.data.Year

/**
 * A custom grid to display years to the user
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
class YearCustomGrid constructor(
    private val context: Context,
    private val years: ArrayList<Year>,
) : BaseAdapter() {

    override fun getCount(): Int {
        return years.size
    }

    override fun getItem(position: Int): Any {
        return years[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //Set up the layoutInflater
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //Set up the view
        val view = layoutInflater.inflate(R.layout.year_custom_grid, null)


        //The TextViews that will display data to the user
        val schoolNameTextView: TextView = view!!.findViewById(R.id.schoolNameTextView)
        val gradeTextView: TextView = view.findViewById(R.id.GRADETEXTVIEW)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)

        val year: Year = years[position]

        //Display the year data to the user
        schoolNameTextView.text = year.schoolName
        gradeTextView.text = year.grade
        val beginString: String = year.begin.toString()
        val endString: String = year.end.toString()
        timeTextView.text = "$beginString-$endString"

        return view
    }
}