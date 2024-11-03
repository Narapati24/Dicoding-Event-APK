package com.dicoding.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.EventAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.rvEvent.addItemDecoration(itemDecoration)

        val finishedViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FinishedViewModel::class.java]
        finishedViewModel.event.observe(viewLifecycleOwner){
            setEventData(it)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner){
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