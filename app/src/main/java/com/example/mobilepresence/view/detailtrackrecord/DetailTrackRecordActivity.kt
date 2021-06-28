package com.example.mobilepresence.view.detailtrackrecord

import android.app.ProgressDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mobilepresence.databinding.ActivityDetailTrackRecordBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.view.bottomnav.BottomNavActivity
import com.example.mobilepresence.viewmodel.DetailTrackRecordViewModel
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

        binding.txtAct.setMovementMethod(ScrollingMovementMethod())

        val loading = ProgressDialog(this)
        loading.setMessage("Loading...")

        val intent = intent

        val dateinput = intent.getStringExtra("date")
        val inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val outDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val localDate = LocalDate.parse(dateinput, inputDate)
        var dateoutput = outDate.format(localDate)

        val current = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var checkDate = current.format(format)

        viewmodel.getResponseDetail().observe(this, Observer {
            when(it){
                is Resource.Loading -> {
                    loading.show()
                }
                is Resource.Success -> {
                    loading.dismiss()
                    it.successData.let {

                        Timber.d("datanya $it")

                        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                        val input = LocalDate.parse(it?.detail?.date, inputFormat)
                        val output = outputFormat.format(input)

                        if (output != checkDate){
                            binding.btnChangeact.visibility = View.GONE
                        }else{
                            binding.btnChangeact.visibility = View.VISIBLE
                        }

                        binding.txtAct.text = it?.detail?.post
                        binding.txtDate.text = output
                        binding.txtArrivetime.text = it?.detail?.arrivetime
                        binding.txtLeavingtime.text = it?.detail?.leavingtime
                        binding.txtDetailLat.text = it?.detail?.latitude.toString()
                        binding.txtDetailLng.text = it?.detail?.longitude.toString()
                        binding.txtLokasi.text = it?.detail?.location
                        binding.txtIduser.text = viewmodel.id_user().toString()
                    }
                }
                is Resource.Error -> {
                    loading.dismiss()
                    Timber.e(it.msg)
                }
            }
        })

        viewmodel.getResponseTrackRecord(dateoutput, viewmodel.id_user()!!)

        viewmodel.getEditResponse().observe(this, Observer {
            when(it){
                is UiState.Loading -> {
                    loading.show()
                }
                is UiState.Success -> {
                    loading.dismiss()
                    Timber.d("check data -> ${it.data.Success} and ${it.data.Message}")
                }
                is UiState.Error -> {
                    loading.dismiss()
                    Timber.e(it.throwable)
                }
            }
        })

        binding.btnChangeact.setOnClickListener {
            if(binding.edtNewact.text.isEmpty()){
                toast("Masukan data yang ingin diubah")
            }else{
                viewmodel.EditAct(dateoutput,binding.edtNewact.text.toString(), viewmodel.id_user()!!)
                toast("Silahkan cek data anda kembali")
                startActivity<BottomNavActivity>()
                finish()
            }
        }
    }
}