package com.xavisson.archcomponentstraining.di

import android.content.Context
import com.xavisson.archcomponentstraining.data.room.RoomCurrencyDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
	@Provides
	@Singleton
	fun provideRoomCurrencyDataSource(context: Context) =
			RoomCurrencyDataSource.buildPersistentCurrency(context)
}
