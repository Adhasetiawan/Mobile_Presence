package com.example.mobilepresence.view.detailtrackrecord

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.mobilepresence.databinding.ActivityDetailTrackRecordBinding
import com.example.mobilepresence.viewmodel.DetailTrackRecordViewModel
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.lifecycle.Observer
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import java.util.*

class DetailTrackRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTrackRecordBinding

    private val viewmodel : DetailTrackRecordViewModel by viewModel()

    private var lat = 0.0
    private var lng = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTrackRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loading = ProgressDialog(this)
        loading.setMessage("Loading...")

        val intent = intent

        val dateinput = intent.getStringExtra("date")
        val inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val outDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val localDate = LocalDate.parse(dateinput, inputDate).plusDays(1)
        var dateoutput = outDate.format(localDate)

        viewmodel.getResponseDetail().observe(this, Observer {
            when(it){
                is Resource.Loading -> {
                    loading.show()
                }
                is Resource.Success -> {
                    loading.dismiss()
                    it.successData.let {
                        lat = it!!.detail.latitude
                        lng = it!!.detail.longitude
                    }
                }
                is Resource.Error -> {}
            }
        })

        viewmodel.getResponseTrackRecord(dateoutput, viewmodel.id_user()!!)

        binding.btnTest.setOnClickListener {
            toast("data kiriman yang diterima adalah $dateoutput dan lokasi anda lat $lat lng $lng")
        }
    }
}