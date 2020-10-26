package com.example.mobilepresence.model.persistablenetworkresourcecall

import android.annotation.SuppressLint
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import timber.log.Timber

abstract class NetworkCall<ResponseType, EmissionType> {
    protected abstract fun createNetworkCall(): Single<ResponseType>

    protected abstract fun onNetworkCallSuccess(emitter: ObservableEmitter<EmissionType>, response: ResponseType)

    protected abstract fun onNetworkCallError(emitter: ObservableEmitter<EmissionType>, error: Throwable)

    @SuppressLint("CheckResult") // The Single's result is disposed in the onSuccess or onError
    protected open fun fetchFromNetwork(emitter: ObservableEmitter<EmissionType>) {
        createNetworkCall()
            .subscribe({ response ->
                onNetworkCallSuccess(emitter, response)
            }, { error ->
                if (error is Error) {
                    onNetworkCallError(emitter, error)
                } else {
                    // This should never happen since all network exceptions are caught and typecasted in
                    // RxErrorHandlingCallAdapterFactory
                    Timber.wtf("fetchFromNetwork() returned unknown error: $error")
                }
            })
    }
}