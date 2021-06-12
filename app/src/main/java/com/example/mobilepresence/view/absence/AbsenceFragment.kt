package com.example.mobilepresence.view.absence

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mobilepresence.databinding.FragmentAbsenceBinding
import com.example.mobilepresence.viewmodel.AbsenceViewmodel
import org.koin.android.ext.android.inject
import androidx.lifecycle.Observer
import com.example.mobilepresence.model.UiState
import org.koin.androidx.scope.currentScope
import java.sql.Time
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AbsenceFragment : Fragment() {

    private val viewmodel : AbsenceViewmodel by inject()

    private var _binding : FragmentAbsenceBinding? = null
    private val binding get() = _binding!!

    lateinit var presenceTime : LocalDateTime

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id_user = viewmodel.getIduser()!!.toInt()

        //date
        val absence = LocalDateTime.now()
        val absenceDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var date = absence.format(absenceDate)

        var time = binding.txtPresencetime.text.toString()

        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading...")

        viewmodel.getAbsenceResponse().observe(requireActivity(), Observer{
            when(it){
                is UiState.Loading -> {
                    loading.show()
                }
                is UiState.Success -> {
                    loading.dismiss()
                    binding.placerAbsence.visibility = View.VISIBLE
                    binding.txtName.visibility = View.VISIBLE

                    binding.txtName.text = "Selamat jalan, ${viewmodel.getName()}"
                    binding.txtPresencetime.text = time
                }
                is UiState.Error -> {
                    loading.dismiss()
                }
            }
        })

        binding.btnAbsence.setOnClickListener {
            viewmodel.absence(time, date, id_user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAbsenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}