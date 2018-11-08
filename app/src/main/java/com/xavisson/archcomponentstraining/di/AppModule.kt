package com.xavisson.archcomponentstraining.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val currencyApplication: MyApplication) {
	@Provides
	@Singleton
	fun provideContext(): Context = currencyApplication
}
