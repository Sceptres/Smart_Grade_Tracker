package com.aaa.schooltracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.databinding.SubjectFragmentBinding
import com.aaa.schooltracker.ui.customgrid.GradeCustomGrid
import com.aaa.schooltracker.ui.dialog.AddGradePopup
import com.aaa.schooltracker.ui.dialog.UpdatePopup
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.Grade
import com.aaa.schooltracker.util.data.Subject
import java.text.DecimalFormat

class SubjectFragment() : Fragment() {

    lateinit var db: DatabaseHelper

    //Subject information
    private val gradeArray: ArrayList<Grade> = ArrayList()

    //Grid adapter
    private lateinit var gradeGridAdapter: GradeCustomGrid

    //Activity data
    private lateinit var subject: Subject

    private var _binding: SubjectFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subject = it.getParcelable(Constants.SUBJECT_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SubjectFragmentBinding.inflate(inflater, container, false)

        this.db = DatabaseHelper(requireContext())
        this.gradeGridAdapter = GradeCustomGrid(requireContext(), gradeArray)

        //Display titles
        binding.subSubjectView.text = subject.name
        binding.subjectTitle.text = "G$"

        //Refresh the grid
        refresh()

        //Add grade button clicklistener
        binding.addGradeBTN.setOnClickListener {
            AddGradePopup(db).apply {
                show(requireActivity().supportFragmentManager, "Add Grade")
            }
        }

        //Item clicklistener
        binding.gradeGrid.setOnItemClickListener { _, _, position, _ ->
            UpdatePopup(db, gradeArray[position]).apply {
                show(requireActivity().supportFragmentManager, "Update")
            }
        }

        //Longitemclicklistener for grades
        binding.gradeGrid.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this grade record?")
                .setPositiveButton("Delete") { _, _ ->
                    //Delete and refresh
                    db.deleteGrade(gradeArray[position])
                    refresh()
                }
                .setNegativeButton("Cancel", null).show()
            true
        }

        return binding.root

    }

    /**
     * Removes resources once the view is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Name: getSubjectAverage
     * Date: 5/11/2020
     * Functionality: Gets the average for the subject
     * @return The average of the subject as a string
     */
    private fun getSubjectAverage(): String {
        var average = 0.0
        var gradeSum = 0.0
        var fullGradeSum = 0.0
        if (gradeArray.isNotEmpty()) { //Do the grade Arrays contain info?
            //Get the totals
            gradeSum += gradeArray.sumOf { it.grade }
            fullGradeSum += gradeArray.sumOf { it.maxGrade }

            //Calculate the average
            average = gradeSum / fullGradeSum * 100
        }
        return DecimalFormat("###.##").format(average)
    }

    /**
     * Gets all grades from the database and update view
     */
    private fun refresh() {
        //Clear previous data and get current data
        gradeArray.clear()

        // Get the grades of this subject
        gradeArray.addAll(db.getGrades(subject))

        //Display main data
        binding.subGradeView.text = java.lang.String.format("%s%%", getSubjectAverage())

        binding.gradeGrid.adapter = gradeGridAdapter
    }

    //Insert the grade to the database and refresh the gradeGrid
//    override fun addGrade(GradeName: String?, Grade: Double, fullGrade: Double) {
//        db.insertGrade(Grade(null, null, null, GradeName, Grade, fullGrade))
//        refresh()
//    }

    //Upgrade the grade in the database
//    override fun update(grade: Grade) {
//        db.updateGrade(grade)
//        refresh()
//    }

}