package com.dicoding.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.ui.EventAdapter
class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.rvEvent.addItemDecoration(itemDecoration)

        val upcomingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UpcomingViewModel::class.java]
        upcomingViewModel.event.observe(viewLifecycleOwner){
            setEventData(it)
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEventData(event: List<ListEventsItem>){
        val adapter = EventAdapter()
        adapter.submitList(event)
        binding.rvEvent.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}