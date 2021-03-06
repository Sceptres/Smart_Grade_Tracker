package com.aaa.schooltracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.aaa.schooltracker.DatabaseHelper
import com.aaa.schooltracker.databinding.EventFragmentBinding
import com.aaa.schooltracker.ui.customgrid.EventCustomGrid
import com.aaa.schooltracker.ui.dialog.EventAddPopup
import com.aaa.schooltracker.util.Constants
import com.aaa.schooltracker.util.data.event.Event
import java.text.SimpleDateFormat
import java.util.*

/**
 * The fragment that is accessed by pressing the event tab on the bottom navigation.
 * Displays all events in the database
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
class EventFragment : Fragment() {

    lateinit var db: DatabaseHelper

    //Events
    private val eventArray: ArrayList<Event> = ArrayList()

    //Event grid adapter
    private lateinit var eventGridAdapter: EventCustomGrid


    private var _binding: EventFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = EventFragmentBinding.inflate(inflater, container, false)

        this.db = DatabaseHelper(requireContext())
        this.eventGridAdapter = EventCustomGrid(requireContext(), eventArray)

        //Refresh the activity
        refresh()

        //Check the events status
        checkIfDone()

        //Add event button click listener
        binding.evAddEvent.setOnClickListener {
            EventAddPopup().show(requireActivity().supportFragmentManager, "Add Event")
        }

        //Clear events button clicklistener
        binding.clearEventsBTN.setOnClickListener {
            db.clearEvents()
            refresh()
        }

        //Event long clicklistener
        binding.eventGridView.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this event?")
                .setPositiveButton("Delete") { _, _ ->
                    //Delete and refresh
                    db.deleteEvent(eventArray[position])
                    refresh()
                }
                .setNegativeButton("Cancel", null).show()
            true
        }

        this.activity?.apply {
            supportFragmentManager.setFragmentResultListener(Constants.ADD_EVENT_KEY, this) {_, event ->

                // Add the grade to the database
                db.insertEvent(event.getParcelable(Constants.EVENT_KEY)!!)

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
     * Get all the Events from the database and update view
     */
    private fun refresh() {
        eventArray.clear()

        eventArray.addAll(db.events)

        binding.eventGridView.adapter = eventGridAdapter
    }

    /**
     * Goes through each event and checks if that event is done
     */
    private fun checkIfDone() {
        val df = SimpleDateFormat("dd")
        val dfm = SimpleDateFormat("MM")

        //Get the current date
        val today: Int = df.format(Date()).toInt()
        val monthNow: Int = dfm.format(Date()).toInt()

        //Check status
        eventArray.forEach {
            if (it.day.toInt() <= today && it.month.toInt() <= monthNow) {
                it.status = 1
                db.updateEvent(it)
            }
        }

        refresh()
    }
}