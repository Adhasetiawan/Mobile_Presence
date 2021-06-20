package com.example.mobilepresence.view.trackrecord

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilepresence.databinding.FragmentTrackRecordBinding
import com.example.mobilepresence.model.local.entity.TrackRecord
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.viewmodel.TrackRecordViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.android.ext.android.inject
import timber.log.Timber

class TrackRecordFragment : Fragment() {

    private var _binding : FragmentTrackRecordBinding? = null
    private val binding get() = _binding!!
    private var groupadapter : GroupAdapter<GroupieViewHolder> = GroupAdapter()

    private var listtr : MutableList<TrackRecord> = mutableListOf()
    private val viewmodel : TrackRecordViewModel by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id_user = viewmodel.id_user()

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

                        record!!.forEach {
                            groupadapter.add(TrackRecordItem(it))
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

        viewmodel.getTrackRecord(id_user!!, "2021-06-01", "2021-06-07", 1)

        initrv()
    }

    private fun initrv(){
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