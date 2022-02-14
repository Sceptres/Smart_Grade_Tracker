/**
 * Name: YearsFragment.kt
 * Date: 28/10/2021
 * @author: Abdallah Alqashqish
 * The fragment that will show the different year information
 */

package com.aaa.schooltracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.R
import com.aaa.schooltracker.databinding.YearsFragmentBinding
import com.aaa.schooltracker.ui.activity.MainActivity
import com.aaa.schooltracker.ui.customgrid.YearCustomGrid
import com.aaa.schooltracker.ui.dialog.YearAddPopup
import com.aaa.schooltracker.util.data.Year
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class YearsFragment : Fragment() {

    lateinit var db: DatabaseHelper

    //The years
    private val years: ArrayList<Year> = ArrayList()

    //The grid adapter
    private var gridAdapter: YearCustomGrid? = null

    private var _binding: YearsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = YearsFragmentBinding.inflate(inflater, container, false)

        this.db = DatabaseHelper(requireContext())
        this.gridAdapter = YearCustomGrid(requireContext(), years)

        refresh()

        // Set the title
        binding.yearTitle.text = "Years"

        //Add year button ClickListener
        binding.addYearBTN.setOnClickListener{
            YearAddPopup(db).show(requireActivity().supportFragmentManager, "Add Year")
        }

        //Item clicklistener
        binding.yearGridView.setOnItemClickListener{ _, _, position, _ ->

            // Create arguments bundle
            val bundle = Bundle()
            bundle.putParcelable(Constants.YEAR_KEY, years[position])

            // Open YearFragment
            requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.yearsFragment_to_yearFragment, bundle)
        }

        //Item long clicklistener
        binding.yearGridView.setOnItemLongClickListener{ _, _, position, _ ->
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this year?")
                .setPositiveButton("Delete") { _, _ ->
                    //Delete and refresh
                    db.deleteYear(years[position])
                    refresh()
                }
                .setNegativeButton("Cancel", null).show()
            true
        }

        //Receive data from YearAddPopup
        this.activity?.apply {
            supportFragmentManager.setFragmentResultListener(Constants.ADD_YEAR_KEY, this) { _, year ->

                //Add to database
                db.insertYear(year.getParcelable(Constants.YEAR_KEY)!!)

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
     * Get all the years from the database and update the view
     */
    private fun refresh() {
        years.clear()
        years.addAll(db.getYears())
        binding.yearGridView.adapter = gridAdapter
    }
}