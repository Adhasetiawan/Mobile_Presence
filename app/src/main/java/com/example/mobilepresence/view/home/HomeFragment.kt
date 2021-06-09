package com.example.mobilepresence.view.home

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.mobilepresence.databinding.FragmentHomeBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.viewmodel.PostViewmodel
import com.google.android.gms.location.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class HomeFragment : Fragment() {

    //viewmodel
    private val viewmodel : PostViewmodel by inject()

    //location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    //defult location value
    private var lat = 0.0
    private var lng = 0.0
    private var endlat = 0.0
    private var endlng = 0.0

    //default dateTime value
    var days = "1"
    var month = "1"
    var year = "0"
    var hour = "0"
    var minute = "0"

    //layout
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //this is where i put the codes
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //loading
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")

        //observasi data lokasi pada repository
        viewmodel.getLocationResponse().observe(requireActivity(), Observer {
            when(it){
                is Resource.Loading -> {}
                is Resource.Success -> {
                    it.data!!.location.forEach{
                        endlat = it.latitude
                        endlng = it.longitude
                    }
                }
                is Resource.Error -> {
                    Timber.tag("cek pada ->").e("error pada => " + it)
                    Toast.makeText(requireContext(), "Something wrong with the end point", Toast.LENGTH_SHORT).show()
                }
            }
        })
        //ambil data lokasi yang dituju
        viewmodel.getLocation(1)

        //observasi respon pada API presence
        viewmodel.getPostResponse().observe(requireActivity(), Observer {
            when(it){
                is UiState.Loading->{
                    loading.show()
                }
                is UiState.Success->{
                    loading.dismiss()
                    Toast.makeText(requireContext(), "Presence has been made", Toast.LENGTH_SHORT).show()
                }
                is UiState.Error->{
                    loading.dismiss()
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    Timber.tag("Error pada ->").e(it.toString())
                }
            }
        })

        //observasi respon pada API absence
        viewmodel.getAbsenceResponse().observe(requireActivity(), Observer {
            when(it){
                is UiState.Loading ->{
                    loading.show()
                }
                is UiState.Success ->{
                    loading.dismiss()
                    Toast.makeText(requireContext(), "Absence has been made", Toast.LENGTH_SHORT).show()
                }
                is UiState.Error ->{
                    loading.dismiss()
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    Timber.tag("Error pada ->").e(it.toString())
                }
            }
        })

        //inisialisasi hari dan waktu
        val dateTime = Calendar.getInstance()

        //date
        val getDays = dateTime.get(Calendar.DAY_OF_MONTH)
        val getMonth = dateTime.get(Calendar.MONTH).plus(1)
        val getYear = dateTime.get(Calendar.YEAR)
        year = getYear.toString()
        if(getDays < 10 && getMonth < 10 || getDays < 10 || getMonth < 10){
            days = getDays.toString().padStart(2, '0')
            month = getMonth.toString().padStart(2, '0')
        } else {
            days = getDays.toString()
            month = getMonth.toString()
        }

        //time
        val getHour = dateTime.get(Calendar.HOUR_OF_DAY)
        val getMinute = dateTime.get(Calendar.MINUTE)
        if (getHour < 10 && getMinute < 10 || getHour < 10 || getMinute < 10){
            hour = getHour.toString().padStart(2, '0')
            minute = getMinute.toString().padStart(2, '0')
        }else{
            hour = getHour.toString()
            minute = getMinute.toString()
        }

        autolocate()
        relocate()
        setact()
    }

    //mendekteksi lokasi secara otomatis dan terus menerus
    private fun autolocate(){
        //permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        } else {
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            //trigger
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1000
                )
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    //mendekteksi ulang lokasi user
    private fun relocate(){
        binding.btnLocate.setOnClickListener {
            autolocate()
        }
    }

    //izin penggunaan fitur pada manifest
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //pemanggilan lokasi terakhir
    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                val location = p0.locations.get(p0.locations.size - 1)
                lat = location.latitude
                lng = location.longitude
            }
        }
    }

    //fitur observasi lokasi user secara terus - menerus
    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    //fungsi utama pada fitur prensece dan absence
    private fun setact(){
        binding.btnPreesnce.setOnClickListener {
            if (binding.radioOffice.isChecked){
                //inisiasi tujuan awal dan akhir
                val start = Location("locationStart")
                start.latitude = lat
                start.longitude = lng
                val end = Location("locationEnd")
                end.latitude = endlat
                end.longitude = endlng

                val date = year + "-" + month + "-" + days
                val arrivetime = hour + ":" + minute

                //perhitungan jarak
                var distance = start.distanceTo(end)
                if (distance >= 25.0 && lat == 0.0 && lng == 0.0 && binding.edtPost.text.isEmpty()|| distance >= 25.0 || lat == 0.0 || lng == 0.0 || binding.edtPost.text.isEmpty()){
                    Toast.makeText(requireContext(), "Plase check the requirement input and relocate your position", Toast.LENGTH_SHORT).show()
                } else {
                    viewmodel.Post(
                        binding.edtPost.text.toString(),
                        date,
                        arrivetime,
                        leavingtime = "00:00",
                        lat,
                        lng,
                        location = "Office",
                        viewmodel.getIdUser()!!.toInt()
                    )
                }

            }else if(binding.radioWfh.isChecked){
                val wfhDate = year + "-" + month + "-" + days
                val wfhTime = hour + ":" + minute

                if (lat == 0.0 && lng == 0.0 && binding.edtPost.text.isEmpty()|| lat == 0.0 || lng == 0.0 || binding.edtPost.text.isEmpty()){
                    Toast.makeText(requireContext(), "Plase check the requirement input and relocate your position", Toast.LENGTH_SHORT).show()
                } else {
                    viewmodel.Post(
                        binding.edtPost.text.toString(),
                        wfhDate,
                        wfhTime,
                        leavingtime = "00:00",
                        lat,
                        lng,
                        location = "Office",
                        viewmodel.getIdUser()!!.toInt()
                    )
                }
            }
        }

        binding.btnAbsence.setOnClickListener {
            val leavingTime = hour + ":" + minute
            val leavingDate = year + "-" + month + "-" + days
            if (leavingDate.isEmpty() && leavingTime.isEmpty() && viewmodel.getIdUser()!!.toString().isEmpty() || leavingDate.isEmpty() || leavingTime.isEmpty() || viewmodel.getIdUser()!!.toString().isEmpty()){
                Toast.makeText(requireContext(), "Absence can't be made", Toast.LENGTH_SHORT).show()
            }else{
                viewmodel.absence(viewmodel.getIdUser()!!, leavingDate, leavingTime, )
            }
        }
    }



    //---fragment set up just ignore this---//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
