package com.example.mobilepresence.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobilepresence.BuildConfig
import com.example.mobilepresence.databinding.FragmentProfileBinding
import com.example.mobilepresence.util.loadImageFromUrl
import com.example.mobilepresence.view.passchange.PassChangeActivity
import com.example.mobilepresence.viewmodel.ProfileViewModel
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val vm : ProfileViewModel by inject()

    //this is where i put the codes
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val intent = Intent (requireContext(), PassChangeActivity::class.java)

        binding.profilePictPlacer.loadImageFromUrl(BuildConfig.BASE_URL+"uploads/"+vm.getPict())
        binding.txtName.text = vm.getName()
        binding.txtDivisi.text = vm.getDivision()
        binding.txtIDuser.text = vm.getIdUser()?.toString()
        binding.txtTipeuser.text = vm.getRole()

        binding.btnChangePass.setOnClickListener {
            intent.putExtra("id_user", binding.txtIDuser.text)
            intent.putExtra("image", vm.getPict())
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}