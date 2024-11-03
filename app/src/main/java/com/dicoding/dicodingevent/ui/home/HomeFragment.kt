package com.dicoding.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManagerUpcoming = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEventUpcoming.layoutManager = layoutManagerUpcoming

        val layoutManagerFinised = LinearLayoutManager(requireActivity())
        binding.rvEventFinished.layoutManager = layoutManagerFinised

        val homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]
        homeViewModel.upcomingEvent.observe(viewLifecycleOwner){
            setEventUpcomingData(it.take(5))
        }

        homeViewModel.finishedEvent.observe(viewLifecycleOwner){
            setEventFinishedData(it.take(5))
        }

        homeViewModel.isLoadingUpcoming.observe(viewLifecycleOwner){
            showLoading1(it)
        }

        homeViewModel.isLoadingFinished.observe(viewLifecycleOwner){
            showLoading2(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEventUpcomingData(event: List<ListEventsItem>){
        val adapter = EventAdapter()
        adapter.submitList(event)
        binding.rvEventUpcoming.adapter = adapter
    }
    private fun setEventFinishedData(event: List<ListEventsItem>){
        val adapter = EventAdapter()
        adapter.submitList(event)
        binding.rvEventFinished.adapter = adapter
    }

    private fun showLoading1(isLoading: Boolean){
        binding.progressBar1.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showLoading2(isLoading: Boolean){
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}