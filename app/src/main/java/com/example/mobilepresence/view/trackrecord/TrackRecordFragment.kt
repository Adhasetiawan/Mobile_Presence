package com.example.mobilepresence.view.trackrecord

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilepresence.databinding.FragmentTrackRecordBinding
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.local.entity.TrackRecord
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.viewmodel.TrackRecordViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TrackRecordFragment : Fragment() {

    private var _binding : FragmentTrackRecordBinding? = null
    private val binding get() = _binding!!
    private var groupadapter : GroupAdapter<GroupieViewHolder> = GroupAdapter()

    private var listtr : MutableList<TrackRecord> = mutableListOf()
    private val viewmodel : TrackRecordViewModel by viewModel()

    private var calendar = Calendar.getInstance()
    private var date_one = ""
    private var date_two = ""

    private var page = 1
    private var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.proses.visibility = View.GONE

        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading")

        viewmodel.observeListTrackRecord().observe(requireActivity(), Observer {
            when(it){
                is Resource.Loading -> {
                    it.loadingData.let { record->
                        loading.show()
                        binding.segaran.isRefreshing = false
                    }
                }
                is Resource.Success -> {
                    it.successData.let { record->
                        if (listtr.isEmpty()) listtr.clear()
                        loading.dismiss()
                        groupadapter.clear()

                        if (record != null) {
                            record.forEach {
                                groupadapter.add(TrackRecordItem(it))
                            }
                        }
                        groupadapter.notifyDataSetChanged()
                        binding.segaran.isRefreshing = false
                    }
                }
                is Resource.Error -> {
                    loading.dismiss()
                    Toast.makeText(requireContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                    Timber.e(it.msg)
                }
            }
        })

        //get date_one
        val dateone = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateformat = "yyyy-MM-dd"
                val formatter = SimpleDateFormat(dateformat, Locale.ENGLISH)
                date_one = formatter.format(calendar.time)
                binding.txtDateone.text = date_one
            }
        }

        //get date_two
        val datetwo = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateformat = "yyyy-MM-dd"
                val formatter = SimpleDateFormat(dateformat, Locale.ENGLISH)
                date_two = formatter.format(calendar.time)
                binding.txtDatetwo.text = date_two
            }
        }

        binding.txtDateone.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateone,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.txtDatetwo.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datetwo,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSearch.setOnClickListener {
            if (date_one.isEmpty() && date_two.isEmpty() || date_one.isEmpty() || date_two.isEmpty()){
                Toast.makeText(requireContext(), "No date has been selected", Toast.LENGTH_SHORT).show()
            }else{
                viewmodel.getTrackRecord(viewmodel.id_user()!!, date_one, date_two, page)
            }
        }

        binding.rvTr.addOnScrollListener(object  : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItemCount = LinearLayoutManager(requireContext()).childCount
                val passItemVisible = LinearLayoutManager(requireContext()).findFirstCompletelyVisibleItemPosition()
                var total = groupadapter.itemCount

                Timber.d("jumlah $currentItemCount")

                if (currentItemCount + passItemVisible >= total){
                    viewmodel.getTrackRecord(viewmodel.id_user()!!, date_one, date_two, page++)
                }
            }
        })

        initrv()
        funsegaran()
    }

    fun funsegaran(){
        binding.segaran.setOnRefreshListener {
            viewmodel.getTrackRecord(viewmodel.id_user()!!, date_one, date_two, 1)
        }
    }

    fun initrv(){
        binding.rvTr.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupadapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTrackRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}