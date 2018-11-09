package com.xavisson.archcomponentstraining.data.repository

import android.arch.lifecycle.LiveData
import com.xavisson.archcomponentstraining.domain.AvailableExchange
import com.xavisson.archcomponentstraining.domain.Currency

interface Repository {

    fun getCurrencyList(): LiveData<List<Currency>>

    fun getAvailableExchange(currencies: String): LiveData<AvailableExchange>
}
