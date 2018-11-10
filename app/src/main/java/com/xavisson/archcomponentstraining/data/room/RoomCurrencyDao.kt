package com.xavisson.archcomponentstraining.data.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Observable

@Dao
interface RoomCurrencyDao {
	@Insert
	fun insertAll(currencies: List<CurrencyEntity>)

	@Query(RoomContract.SELECT_CURRENCIES)
	fun getAllCurrencies(): Observable<List<CurrencyEntity>>
}
