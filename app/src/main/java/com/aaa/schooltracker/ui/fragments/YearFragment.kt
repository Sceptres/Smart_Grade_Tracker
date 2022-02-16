/**
 * Name: YearFragment.kt
 * Date: 01/11/2021
 * @author: Abdallah Alqashqish
 * The fragment that will show the different subjects of every year
 */

package com.aaa.schooltracker.ui.fragments

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.R
import com.aaa.schooltracker.databinding.YearFragmentBinding
import com.aaa.schooltracker.ui.activity.MainActivity
import com.aaa.schooltracker.ui.customgrid.SubjectCustomGrid
import com.aaa.schooltracker.ui.dialog.AddSubjectPopup
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.Subject
import com.aaa.schooltracker.util.data.Year
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.text.DecimalFormat

class YearFragment : Fragment() {

    private lateinit var db: DatabaseHelper
    private lateinit var mainAdapter: SubjectCustomGrid

    private lateinit var year: Year

    private val subjectArray: ArrayList<Subject> = ArrayList()

    private var _binding: YearFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //Get the fragment arguments
            year =  it.getParcelable(Constants.YEAR_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = YearFragmentBinding.inflate(inflater, container, false)

        this.db = DatabaseHelper(requireContext())
        this.mainAdapter = SubjectCustomGrid(requireContext(), subjectArray)

        //Refresh the grid view
        refresh()

        // Set title depending on grade level
        binding.levelTitle.text = if(year.grade.contains("U", true) || year.grade.contains("C", true)) year.grade else "G${year.grade}"

        //Clicklistener of the add button
        binding.addBTN.setOnClickListener {
            AddSubjectPopup(db, year).show(requireActivity().supportFragmentManager, "Add Subject")
        }

        //Clicklistener for item
        binding.subGrid.setOnItemClickListener { _, _, position, _ ->
            // TODO: Send to SubjectFragment with subject information
            val bundle = Bundle()
            bundle.putParcelable(Constants.YEAR_KEY, year)
            bundle.putParcelable(Constants.SUBJECT_KEY, subjectArray[position])

            requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.yearFragment_to_subjectFragment, bundle)
        }

        //Long click listener for item
        binding.subGrid.setOnItemLongClickListener{ _, _, position, _ ->
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this subject?")
                .setPositiveButton("Delete") { _, _ ->
                    //Delete and refresh
                    db.deleteSubject(subjectArray[position])
                    refresh()
                }
                .setNegativeButton("Cancel", null).show()
            true
        }

        //Receive data from AddSubjectPopup
        this.activity?.apply {
            supportFragmentManager.setFragmentResultListener(Constants.ADD_SUBJECT_KEY, this) { _, subject ->

                //Add to database
                db.insertSubject(subject.getParcelable(Constants.SUBJECT_KEY)!!)

                refresh()

            }
        }

        return binding.root
    }

    /**
     * Removes resources once the view is destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    /**
     * Calculates the average of all the subjects and displays them to the user
     */
    private fun calculateTotalAverage(): Double {
        val numOfZeros: Int = subjectArray.filter { it.average == 0.0 }.count()

        if (numOfZeros == subjectArray.size || subjectArray.isEmpty()) //Is it all zeros?
            return 0.0

        //Get the total
        val totalOfAVG = subjectArray.sumOf { it.average }


        //Return total average
        return totalOfAVG / (subjectArray.size - numOfZeros)
    }

    /**
     * Get the subjects from the database and refresh the view
     */
    private fun refresh() {
        // Clear subjects
        subjectArray.clear()

        // Read subjects from database
        subjectArray.addAll(db.getSubjects(year))

        //Display the average
        binding.gradeView.text =
            java.lang.String.format("%s%%", Constants.df.format(calculateTotalAverage()))

        //Display subjects
        binding.subGrid.adapter = mainAdapter
    }
}