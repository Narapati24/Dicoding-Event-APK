package com.dicoding.dicodingevent.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingevent.ui.EventAdapter
import com.dicoding.dicodingevent.ui.FavoriteEventViewModel
import com.dicoding.dicodingevent.utils.ViewModelFactory

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        binding.rvEvent.addItemDecoration(itemDecoration)

        val favoriteEventViewModel = obtainViewModel(requireActivity()).getAllFavoriteEvent()

        val favoriteViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FavoriteViewModel::class.java]
        favoriteEventViewModel.observe(viewLifecycleOwner){
            val items = arrayListOf<ListEventsItem>()
            it.map {
                val item = ListEventsItem(
                    id = it.id.toInt(),
                    name = it.name,
                    imageLogo = "",
                    summary = "",
                    mediaCover = it.mediaCover.toString(),
                    registrants = 0,
                    link = "",
                    description = "",
                    ownerName = "",
                    cityName = "",
                    quota = 0,
                    beginTime = "",
                    endTime = "",
                    category = ""
                )
                items.add(item)
            }
            setEventData(items)
        }

        favoriteViewModel.isLoading.observe(viewLifecycleOwner){
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

    fun obtainViewModel(activity: FragmentActivity): FavoriteEventViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteEventViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}