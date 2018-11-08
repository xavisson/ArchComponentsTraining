package com.xavisson.archcomponentstraining.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.xavisson.archcomponentstraining.data.repository.CurrencyRepository
import com.xavisson.archcomponentstraining.di.MyApplication
import com.xavisson.archcomponentstraining.domain.Currency
import javax.inject.Inject

class CurrencyViewModel : ViewModel() {

	@Inject
	lateinit var currencyRepository: CurrencyRepository

	private var liveCurrencyData: LiveData<List<Currency>>? = null

	init {
		MyApplication.appComponent.inject(this)
		if (liveCurrencyData == null) {
			liveCurrencyData = MutableLiveData<List<Currency>>()
			liveCurrencyData = currencyRepository.getCurrencyList()
		}
	}

	fun getCurrencyList(): LiveData<List<Currency>>? {
		return liveCurrencyData
	}
}
