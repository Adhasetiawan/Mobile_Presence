package com.example.mobilepresence.view.home

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mobilepresence.databinding.FragmentHomeBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.viewmodel.PostViewmodel
import com.google.android.gms.location.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    //viewmodel
    private val viewmodel: PostViewmodel by inject()

    //inisialisasi metode getLocation untuk user
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    //inisialisasi variabel lokasi user
    private var lat = 0.0
    private var lng = 0.0
    private var endlat = 0.0
    private var endlng = 0.0

    //inisialisasi variabel tanggal dan waktu
    var Date = ""
    var Time = ""

    //layout
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")

        val toast = Toast.makeText(requireContext(), "Request is success", Toast.LENGTH_SHORT)

        //observasi data lokasi pada repository
        viewmodel.getLocationResponse().observe(requireActivity(), Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.segaran.isRefreshing = false
                }
                is Resource.Success -> {
                    it.data!!.location.forEach {
                        endlat = it.latitude
                        endlng = it.longitude
//                        binding.txtDisatance.text = "lat : $endlat and lng : $endlng"
                    }
                    binding.segaran.isRefreshing = false
                }
                is Resource.Error -> {
                    Timber.tag("cek pada ->").e("error pada => " + it)
                    Toast.makeText(requireContext(), "Something wrong with the end point", Toast.LENGTH_SHORT).show()
                }
            }
        })
        //ambil data lokasi yang dituju
        viewmodel.getLocation(1)

        viewmodel.getPostResponse().observe(requireActivity(), Observer{
            when(it){
                is UiState.Loading -> {
                    loading.show()
                }
                is UiState.Success -> {
                    loading.dismiss()
                }
                is UiState.Error -> {
                    loading.dismiss()
                    Timber.tag("Erorr pada ->").e(it.toString())
                }
            }
        })

        binding.btnPreesnce.setOnClickListener {
            val dateTime = LocalDateTime.now()
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var presenceTime = dateTime.format(timeFormatter)
            var presenceDate = dateTime.format(dateFormatter)

            val start = Location("Start")
            start.latitude = endlat
            start.longitude = endlng

            val end = Location("End")
            end.latitude = lat
            end.longitude = lng

            var distance = start.distanceTo(end)
            binding.txtDisatance.text = distance.toString() + " Meter"

            if(binding.radioOffice.isChecked){
                if (distance > 20.0 && binding.edtPost.text.isEmpty() || distance > 20.0 || binding.edtPost.text.isEmpty()){
                    Toast.makeText(requireContext(), "Perhatikan data yang anda berikan dan jarak anda", Toast.LENGTH_SHORT).show()
                }else{
                    viewmodel.Post(binding.edtPost.text.toString(), presenceDate, presenceTime, "00:00", lat, lng, "Office", viewmodel.getIdUser()!!.toInt())
                    binding.edtPost.text.clear()
                }
            }

            if (binding.radioWfh.isChecked){
                if (binding.edtPost.text.isEmpty() && lat == 0.0 && lng == 0.0 || binding.edtPost.text.isEmpty() || lat == 0.0 || lng == 0.0){
                    Toast.makeText(requireContext(), "Perhatikan data yang anda berikan", Toast.LENGTH_SHORT).show()
                }else{
                    viewmodel.Post(binding.edtPost.text.toString(), presenceDate, presenceTime, "00:00", lat, lng, "Outstation", viewmodel.getIdUser()!!.toInt())
                }
            }
        }
        binding.txtName.text = "Hai,\n"  + viewmodel.getName()

        autolocate()
        relocate()
    }

    //mendekteksi lokasi secara otomatis dan terus menerus
    private fun autolocate() {
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

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())

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

    //izin penggunaan fitur pada manifest
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show()
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

    //mendekteksi ulang lokasi user
    private fun relocate() {
        binding.segaran.setOnRefreshListener {
            viewmodel.getLocation(1)
            autolocate()
            lat
            lng
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
