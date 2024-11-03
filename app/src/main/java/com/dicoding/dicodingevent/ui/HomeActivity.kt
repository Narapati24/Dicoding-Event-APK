package com.dicoding.dicodingevent.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ActivityHomeBinding
import com.dicoding.dicodingevent.ui.setting.SettingPreferences
import com.dicoding.dicodingevent.ui.setting.SettingViewModel
import com.dicoding.dicodingevent.ui.setting.dataStore
import com.dicoding.dicodingevent.utils.ViewModelSettingFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingViewModel = obtainViewModel(this)

        settingViewModel.getThemeSettings().observe(this){isDarkModeActive: Boolean->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished, R.id.navigation_favorite, R.id.navigation_setting
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val layoutManager = GridLayoutManager(this@HomeActivity, 2)
        binding.rvSearchView.layoutManager = layoutManager
        val viewModel = ViewModelProvider(this@HomeActivity)[EventViewModel::class.java]
        var isToastShown = false

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)

                    viewModel.findEvent("find", data2 = searchView.text.toString())
                    viewModel.resetLoading()
                    viewModel.event.observe(this@HomeActivity){
                        if (it.isEmpty()){
                            if (!isToastShown){
                                Toast.makeText(this@HomeActivity, "Data Tidak Ada", Toast.LENGTH_LONG).show()
                                isToastShown = true
                            }
                        } else {
                            setEventData(it)
                            isToastShown = false
                        }
                    }

                    viewModel.isLoading.observe(this@HomeActivity){
                        showLoading(it)
                    }
                    false
                }

        }
        viewModel.event.observe(this@HomeActivity){
            if (it.isEmpty()){
                if (!isToastShown){
                    Toast.makeText(this@HomeActivity, "Data Tidak Ada", Toast.LENGTH_LONG).show()
                    isToastShown = true
                }
            } else {
                setEventData(it)
                isToastShown = false
            }
        }
    }

    private fun setEventData(event: List<ListEventsItem>?){
        val adapter = EventAdapter()
        adapter.submitList(event)
        binding.rvSearchView.adapter = adapter
    }

    fun obtainViewModel(activity: FragmentActivity): SettingViewModel {
        val pref = SettingPreferences.getInstance(activity.application.dataStore)
        val factory = ViewModelSettingFactory.getInstance(pref)
        return ViewModelProvider(activity, factory).get(SettingViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}