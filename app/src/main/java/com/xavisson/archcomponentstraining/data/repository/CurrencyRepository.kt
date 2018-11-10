package com.xavisson.archcomponentstraining.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.xavisson.archcomponentstraining.data.remote.CurrencyResponse
import com.xavisson.archcomponentstraining.data.remote.RemoteCurrencyDataSource
import com.xavisson.archcomponentstraining.data.room.CurrencyEntity
import com.xavisson.archcomponentstraining.data.room.RoomCurrencyDataSource
import com.xavisson.archcomponentstraining.domain.AvailableExchange
import com.xavisson.archcomponentstraining.domain.Currency
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

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

    private fun populateRoomDataSource(roomCurrencyDataSource: RoomCurrencyDataSource) {
        val currencyDao = roomCurrencyDataSource.currencyDao()
        val currencyEntityList = RoomCurrencyDataSource.getAllCurrencies()
        Completable.create { currencyDao.insertAll(currencyEntityList) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
    }

    private fun transform(liveCurrencyEntity: LiveData<List<CurrencyEntity>>): LiveData<List<Currency>> {
        return Transformations.map(liveCurrencyEntity) { currencyEntities ->
            val currencyList = ArrayList<Currency>()
            currencyEntities.forEach {
                currencyList.add(Currency(it.countryCode, it.countryName))
            }
            currencyList
        }
    }

    private fun transform(exchangeMap: CurrencyResponse): AvailableExchange {
        return AvailableExchange(exchangeMap.currencyQuotes)
    }
}
