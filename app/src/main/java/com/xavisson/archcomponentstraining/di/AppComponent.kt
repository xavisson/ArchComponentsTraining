package com.xavisson.archcomponentstraining.di

import com.xavisson.archcomponentstraining.presentation.CurrencyViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class, DataModule::class))
@Singleton
interface AppComponent {
	fun inject(currencyViewModel: CurrencyViewModel)
}
