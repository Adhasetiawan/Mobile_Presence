package com.example.mobilepresence.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mobilepresence.BuildConfig
import com.example.mobilepresence.databinding.FragmentProfileBinding
import com.example.mobilepresence.util.loadImageFromUrl
import com.example.mobilepresence.viewmodel.ProfileViewModel
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val vm : ProfileViewModel by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.profilePictPlacer.loadImageFromUrl(BuildConfig.BASE_URL+"uploads/"+vm.getPict())
        binding.txtName.text = "Nama : " + vm.getName()
        binding.txtDivisi.text = "Divisi : " + vm.getDivision()
        binding.txtIDuser.text = "ID User : " + vm.getIdUser().toString()
        binding.txtTipeuser.text = "Tipe : " + vm.getRole()

        binding.btnChangePass.setOnClickListener {
            Toast.makeText(context, "This page is working fine!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}