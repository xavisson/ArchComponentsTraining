package com.xavisson.archcomponentstraining.presentation

import android.arch.lifecycle.ViewModel
import com.xavisson.archcomponentstraining.data.repository.CurrencyRepository
import com.xavisson.archcomponentstraining.di.MyApplication
import javax.inject.Inject

class ExchangeViewModel : ViewModel() {

    @Inject
    lateinit var currencyRepository: CurrencyRepository

    init {
        initializeDagger()
    }

    fun getAvailableExchange(currencies: String) =
            currencyRepository.getAvailableExchange(currencies)

    private fun initializeDagger() = MyApplication.appComponent.inject(this)
}