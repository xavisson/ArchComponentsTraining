package com.xavisson.archcomponentstraining.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.xavisson.archcomponentstraining.data.remote.CurrencyResponse
import com.xavisson.archcomponentstraining.data.remote.RemoteCurrencyDataSource
import com.xavisson.archcomponentstraining.data.room.CurrencyEntity
import com.xavisson.archcomponentstraining.data.room.RoomCurrencyDao
import com.xavisson.archcomponentstraining.data.room.RoomCurrencyDataSource
import com.xavisson.archcomponentstraining.domain.AvailableExchange
import com.xavisson.archcomponentstraining.domain.Currency
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
        val roomCurrencyDataSource: RoomCurrencyDataSource,
        val remoteCurrencyDataSource: RemoteCurrencyDataSource
) : Repository {

    init {
        populateRoomDataSource(roomCurrencyDataSource)
    }

    override fun getCurrencyList(): LiveData<List<Currency>> {
        val roomCurrencyDao = roomCurrencyDataSource.currencyDao()
        val mutableLiveData = MutableLiveData<List<Currency>>()
        roomCurrencyDao.getAllCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { currencyList ->
                    mutableLiveData.value = transform(currencyList)
                }

        return mutableLiveData
    }

    private fun transform(currencies: List<CurrencyEntity>): List<Currency> {
        val currencyList = ArrayList<Currency>()
        currencies.forEach {
            currencyList.add(Currency(it.countryCode, it.countryName))
        }
        return currencyList
    }

    private fun populateRoomDataSource(roomCurrencyDataSource: RoomCurrencyDataSource) {
        val currencyDao = roomCurrencyDataSource.currencyDao()
        currencyDao.getCurrenciesTotal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (isRoomEmpty(it)) {
                        populate(currencyDao)
                    } else {
                        Log.i(CurrencyRepository::class.java.simpleName, "DataSource has been already Populated")
                    }
                }
    }

    private fun isRoomEmpty(currenciesTotal: Int) = currenciesTotal == 0

    private fun populate(currencyDao: RoomCurrencyDao) {
        val currencyEntityList = RoomCurrencyDataSource.getAllCurrencies()
        Completable.fromAction { currencyDao.insertAll(currencyEntityList) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(@NonNull d: Disposable) {

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

    override fun getAvailableExchange(currencies: String): LiveData<AvailableExchange> {
        val mutableLiveData = MutableLiveData<AvailableExchange>()
        remoteCurrencyDataSource.requestAvailableExchange(currencies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { currencyResponse ->
                    if (currencyResponse.isSuccess) {
                        mutableLiveData.value = transform(currencyResponse)
                    } else {
                        throw Throwable("CurrencyRepository -> on Error occurred")
                    }
                }
        return mutableLiveData
    }

    private fun transform(exchangeMap: CurrencyResponse): AvailableExchange {
        return AvailableExchange(exchangeMap.currencyQuotes)
    }
}
