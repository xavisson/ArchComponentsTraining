package com.xavisson.archcomponentstraining.di

import com.xavisson.archcomponentstraining.presentation.CurrencyViewModel
import com.xavisson.archcomponentstraining.presentation.ExchangeViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class, RoomModule::class, RemoteModule::class))
@Singleton
interface AppComponent {
    fun inject(currencyViewModel: CurrencyViewModel)

    fun inject(exchangeViewModel: ExchangeViewModel)
}
