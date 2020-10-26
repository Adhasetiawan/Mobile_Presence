package com.example.mobilepresence.model.persistablenetworkresourcecall

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import timber.log.Timber

abstract class PersistableNetworkResourceCall<ResponseType, ResourceType> :
    NetworkCall<ResponseType, Resource<ResourceType>>() {

    val resourceObservable: Observable<Resource<ResourceType>>

    init {
        resourceObservable = Observable.create<Resource<ResourceType>> { emitter ->
            emitter.onNext(Resource.Loading(null))
            loadFromDatabase()
                .subscribe(
                    { queryResult ->
                        emitter.onNext(Resource.Loading(queryResult))
                        fetchFromNetwork(emitter)
                    }, { error ->
                        Timber.e(error)
                        fetchFromNetwork(emitter)
                    }, {
                        fetchFromNetwork(emitter)
                    })
        }
    }

    protected abstract fun loadFromDatabase(): Maybe<ResourceType>

    override fun onNetworkCallError(emitter: ObservableEmitter<Resource<ResourceType>>, error: Throwable) {
        emitter.onNext(Resource.Error(error.message.toString(), null))
    }
}