package com.xavisson.archcomponentstraining.data.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.xavisson.archcomponentstraining.data.room.CurrencyEntity
import com.xavisson.archcomponentstraining.data.room.RoomCurrencyDataSource
import com.xavisson.archcomponentstraining.domain.Currency
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyRepository @Inject constructor(roomCurrencyDataSource: RoomCurrencyDataSource) : Repository {

	init {
		initRoomDataSource(roomCurrencyDataSource)
	}

	private val roomCurrencyDao = roomCurrencyDataSource.currencyDao()

	override fun getCurrencyList(): LiveData<List<Currency>> {
		return transform(roomCurrencyDao.getAllCurrencies())
	}

	fun transform(liveCurrencyEntity: LiveData<List<CurrencyEntity>>): LiveData<List<Currency>> {
		return Transformations.map(liveCurrencyEntity) { currencyEntities ->
			val currencyList = ArrayList<Currency>()
			currencyEntities.forEach {
				currencyList.add(Currency(it.countryCode, it.countryName))
			}
			currencyList
		}
	}

	fun initRoomDataSource(roomCurrencyDataSource: RoomCurrencyDataSource) {
		val currencyDao = roomCurrencyDataSource.currencyDao()
		val currencyEntityList = RoomCurrencyDataSource.getAllCurrencies()
		Completable.create { currencyDao.insertAll(currencyEntityList) }
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { }
	}
}
