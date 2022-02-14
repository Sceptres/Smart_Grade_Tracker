/**
 * Name: SubjectCustomGrid.kt
 * Date: 4/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the Subject grid
 */

package com.aaa.schooltracker.ui.customgrid

import com.aaa.schooltracker.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.aaa.schooltracker.util.data.Subject


class SubjectCustomGrid constructor(private val context: Context,
                                    private val subjectArray: ArrayList<Subject>) : BaseAdapter() {
    override fun getCount(): Int {
        return subjectArray.size
    }

    override fun getItem(position: Int): Any {
        return subjectArray[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    //The getView method
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //Get the inflater
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //Set the view
        val view = inflater.inflate(R.layout.subject_custom_grid, null)


        //The TextViews that will display the info to the user
        val letter: TextView = view.findViewById(R.id.letterView)
        val subName: TextView = view.findViewById(R.id.gridNameView)
        val averageView: TextView = view.findViewById(R.id.gridGradeView)

        val subject: Subject = subjectArray[position]

        //Display the info to the user
        letter.text = subject.char.toString()
        subName.text = subject.name
        averageView.text = subject.average.toString()

        return view
    }
}