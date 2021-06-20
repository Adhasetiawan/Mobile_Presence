package com.example.mobilepresence.view.trackrecord

import android.view.View
import com.example.mobilepresence.R
import com.example.mobilepresence.databinding.ItemTrackrecordRowBinding
import com.example.mobilepresence.model.local.entity.TrackRecord
import com.xwray.groupie.viewbinding.BindableItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TrackRecordItem(val trackRecord : TrackRecord) : BindableItem<ItemTrackrecordRowBinding>() {
    val inputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val outDate = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
    val localDate = LocalDate.parse(trackRecord.date, inputDate)
    var date: String = outDate.format(localDate)

    override fun bind(viewBinding: ItemTrackrecordRowBinding, position: Int) {
        viewBinding.txtDaterecord.text = date
        viewBinding.txtLocationrecord.text = trackRecord.location
    }

    override fun getLayout(): Int {
        return R.layout.item_trackrecord_row
    }

    override fun initializeViewBinding(view: View): ItemTrackrecordRowBinding {
        return ItemTrackrecordRowBinding.bind(view)
    }
}