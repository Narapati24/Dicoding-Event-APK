package com.dicoding.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.FavoriteEvent
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import com.dicoding.dicodingevent.ui.DetailViewModel
import com.dicoding.dicodingevent.ui.FavoriteEventViewModel
import com.dicoding.dicodingevent.utils.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    companion object{
        const val EVENT = "event"
    }

    private lateinit var favoriteEventViewModel: FavoriteEventViewModel
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var event: ListEventsItem

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        event = intent.getParcelableExtra(EVENT)!!

        detailViewModel = obtainDetailViewModel(this@DetailActivity)
        val favoriteEvent = FavoriteEvent()
        detailViewModel.eventDetail.observe(this){
            it?.let {
                setData(it)
            }
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        favoriteEventViewModel = obtainViewModel(this)


        val isFavorite = favoriteEventViewModel.getFavoriteEventById(event.id.toString())

        isFavorite.observe(this){
            val isFavorite = it != null

            if (isFavorite){
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                binding.fabFavorite.setOnClickListener {
                    deleteFavorite(favoriteEvent)
                }
            } else{
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                binding.fabFavorite.setOnClickListener {
                    addFavorite(favoriteEvent)
                }
            }
        }
    }

    fun setData(eventItem: ListEventsItem){
        Log.d("data", "${eventItem.id} and ${event.id}")
        Glide.with(this)
            .load(eventItem.mediaCover)
            .into(binding.ivCoverEvent)
        binding.tvNameEvent.text = eventItem.name
        binding.tvOwnerName.text = eventItem.ownerName
        binding.tvBeginTime.text = eventItem.beginTime
        binding.tvQuota.text = "${getString(R.string.quota_quantity)} ${(eventItem.quota - eventItem.registrants).toString()}"
        binding.tvDescription.text = HtmlCompat.fromHtml(
            eventItem.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.btnLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(eventItem.link)
            }
            startActivity(intent)
        }
    }

    fun obtainViewModel(activity: AppCompatActivity): FavoriteEventViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteEventViewModel::class.java)
    }

    fun obtainDetailViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory(activity.application, eventId = event.id.toString())
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    fun addFavorite(favoriteEvent: FavoriteEvent){
        favoriteEvent.let { favoriteEvent ->
            favoriteEvent.name = event.name
            favoriteEvent.mediaCover = event.mediaCover
            favoriteEvent.id = event.id.toString()
        }
        favoriteEventViewModel.insert(favoriteEvent)
        showToast(getString(R.string.added_favorite))
    }

    fun deleteFavorite(favoriteEvent: FavoriteEvent){
        favoriteEvent.let { favoriteEvent ->
            favoriteEvent.name = event.name
            favoriteEvent.mediaCover = event.mediaCover
            favoriteEvent.id = event.id.toString()
        }
        favoriteEventViewModel.delete(favoriteEvent)
        showToast(getString(R.string.deleted_favorite))
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.fabFavorite.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.btnLink.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}