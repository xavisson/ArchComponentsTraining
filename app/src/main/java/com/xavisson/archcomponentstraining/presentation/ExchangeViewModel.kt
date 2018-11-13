package com.xavisson.archcomponentstraining.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.xavisson.archcomponentstraining.data.repository.CurrencyRepository
import com.xavisson.archcomponentstraining.di.MyApplication
import com.xavisson.archcomponentstraining.domain.AvailableExchange
import javax.inject.Inject

class ExchangeViewModel : ViewModel() {

    @Inject
    lateinit var currencyRepository: CurrencyRepository

    private var mutableAvailableExchange: LiveData<AvailableExchange>? = null

    init {
        initializeDagger()
    }

    fun getAvailableExchange(currencies: String): LiveData<AvailableExchange>? {
        mutableAvailableExchange == null
        mutableAvailableExchange = MutableLiveData<AvailableExchange>()
        mutableAvailableExchange = currencyRepository.getAvailableExchange(currencies)

        return mutableAvailableExchange
    }

    private fun initializeDagger() = MyApplication.appComponent.inject(this)
}