package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.local.dao.TrackRecordDao
import com.example.mobilepresence.model.local.entity.TrackRecord
import com.example.mobilepresence.model.persistablenetworkresourcecall.PersistableNetworkResourceCall
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.model.response.RecordObject
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*

class TrackRecordRepository(val apiService : ApiService, val trackRecordDao: TrackRecordDao) {
    fun getListTrackRecord(id_user : Int, date_one : String, date_two : String) : Observable<Resource<List<TrackRecord>>>{
        return object : PersistableNetworkResourceCall<RecordObject.TrackrecordResponse, List<TrackRecord>>(){
            override fun loadFromDatabase(): Maybe<List<TrackRecord>> {
                return trackRecordDao.findAllTrackRecor()
            }

            override fun createNetworkCall(): Single<RecordObject.TrackrecordResponse> {
                return apiService.trackRecord(id_user, date_one, date_two)
            }

            override fun onNetworkCallSuccess(
                emitter: ObservableEmitter<Resource<List<TrackRecord>>>,
                response: RecordObject.TrackrecordResponse
            ) {
                if(response.Trackrecord.isEmpty()){
                    emitter.onNext(Resource.Error("data is empty", null))
                }else{
                    response.Trackrecord.let {
                        Timber.d("input data" + it)
                        trackRecordDao.deleteAll()

                        val allData = it.map { data -> TrackRecord.from(data)}
                        trackRecordDao.insert(allData)
                    }
                    emitter.setDisposable(trackRecordDao.streamAll()
                        .subscribeBy(
                            onNext = {
                                emitter.onNext(Resource.Success(it))
                                Timber.d("cek data -> " + it)
                            }
                        ))
                }
            }
        }.resourceObservable
    }
}