package com.xavisson.archcomponentstraining.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.xavisson.archcomponentstraining.data.repository.CurrencyRepository
import com.xavisson.archcomponentstraining.di.MyApplication
import com.xavisson.archcomponentstraining.domain.Currency
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyViewModel : ViewModel() {

    @Inject
    lateinit var currencyRepository: CurrencyRepository

    private val compositeDisposable = CompositeDisposable()
    private var liveCurrencyData: LiveData<List<Currency>>? = null

    init {
        initializeDagger()
    }

    private fun initializeDagger() = MyApplication.appComponent.inject(this)

    fun loadCurrencyList(): LiveData<List<Currency>>? {
        if (liveCurrencyData == null) {
            liveCurrencyData = MutableLiveData<List<Currency>>()
            liveCurrencyData = currencyRepository.getCurrencyList()
        }
        return liveCurrencyData
    }

    fun getCurrencyList(): LiveData<List<Currency>>? {
        return liveCurrencyData
    }

    fun initLocalCurrencies() {
        val disposable = currencyRepository.getTotalCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (isRoomEmpty(it)) {
                        populate()
                    } else {
                        Log.i(CurrencyRepository::class.java.simpleName, "DataSource has been already Populated")
                    }
                }
        compositeDisposable.add(disposable)
    }

    fun unSubscribeViewModel() {
        for (disposable in currencyRepository.allCompositeDisposable) {
            compositeDisposable.addAll(disposable)
        }
        compositeDisposable.clear()
    }


    private fun isRoomEmpty(currenciesTotal: Int) = currenciesTotal == 0

    private fun populate() {
        Completable.fromAction { currencyRepository.addCurrencies() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        Log.i(CurrencyRepository::class.java.simpleName, "DataSource has been Populated")
                    }

                    override fun onError(@NonNull e: Throwable) {
                        e.printStackTrace()
                        Log.e(CurrencyRepository::class.java.simpleName, "DataSource hasn't been Populated")
                    }
                })
    }
}
