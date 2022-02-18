package com.aaa.schooltracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.R
import com.aaa.schooltracker.databinding.YearFragmentBinding
import com.aaa.schooltracker.ui.customgrid.SubjectCustomGrid
import com.aaa.schooltracker.ui.dialog.AddSubjectPopup
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.Subject
import com.aaa.schooltracker.util.data.Year

/**
 * The fragment that displays all subject for a specific year. Accessed by
 * pressing a year in the years fragment
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
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
        _binding = null
    }

    /**
     * @return The average the student has achieved so far throughout this year
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