package com.example.mobilepresence.view.home

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.mobilepresence.databinding.FragmentHomeBinding
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.viewmodel.PostViewmodel
import com.google.android.gms.location.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomeFragment : Fragment() {

    //viewmodel
    private val viewmodel : PostViewmodel by inject()

    //location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    //defult location value
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var endlat = 0.0
    private var endlng = 0.0


    //layout
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //this is where i put the codes
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        //ambil data lokasi yang dituju
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
        viewmodel.getLocation(1)

        binding.btnPreesnce.setOnClickListener {
            if (binding.radioOffice.isChecked){
                //inisiasi tujuan awal dan akhir
                val start = Location("locationStart")
                start.latitude = lat
                start.longitude = lng
                val end = Location("locationEnd")
                end.latitude = endlat
                end.longitude = endlng

                //perhitungan jarak
                var distance = start.distanceTo(end)
                if (distance <= 25.0 && lat == 0.0 && lng == 0.0 || distance <= 25.0 || lat == 0.0 || lng == 0.0){
                    Toast.makeText(requireContext(), "Presence is enable", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Presence is unable", Toast.LENGTH_SHORT).show()
                }

            }else if(binding.radioWfh.isChecked){
                Toast.makeText(requireContext(), "your location mow at lat : " + lat + " lng : " + lng, Toast.LENGTH_SHORT).show()
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
