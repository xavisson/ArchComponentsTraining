package com.xavisson.archcomponentstraining.data.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface RoomCurrencyDao {
	@Insert
	fun insertAll(currencies: List<CurrencyEntity>)

	@Query(RoomContract.SELECT_CURRENCIES)
	fun getAllCurrencies(): LiveData<List<CurrencyEntity>>
}
