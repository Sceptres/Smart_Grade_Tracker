/**
 * Name: GradeCustomGrid.kt
 * Date: 5/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the grade custom grid
 */

package com.aaa.schooltracker.ui.customgrid

import com.aaa.schooltracker.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aaa.schooltracker.util.data.Grade


class GradeCustomGrid constructor(private val c: Context,
                                  private val gradeArray: ArrayList<Grade>) : BaseAdapter() {
    override fun getCount(): Int {
        return gradeArray.size
    }

    override fun getItem(position: Int): Any {
        return gradeArray[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    //The getView method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //Set up the inflater
        val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //Set up the view
        val view = inflater.inflate(R.layout.grade_custom_grid, null)


        //The TextViews that will display the info to the user
        val gradeNameTextView: TextView = view.findViewById(R.id.gradeNameTextView)
        val fullGradeTextView: TextView = view.findViewById(R.id.gGradeText)
        val percentageTextView: TextView = view.findViewById(R.id.percentageTextView)

        val grade: Grade = gradeArray[position]

        //Display the info to the user
        gradeNameTextView.text = grade.name
        fullGradeTextView.text = "${grade.grade}/${grade.maxGrade}"
        val fullPercentageText: String = grade.average.toString() + "%"
        percentageTextView.text = fullPercentageText

        return view
    }
}