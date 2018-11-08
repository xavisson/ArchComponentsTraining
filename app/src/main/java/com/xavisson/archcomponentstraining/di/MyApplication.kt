package com.xavisson.archcomponentstraining.di

import android.app.Application

class MyApplication : Application() {

	companion object {
		lateinit var appComponent: AppComponent
	}

	override fun onCreate() {
		super.onCreate()
		initializeDagger()
	}

	private fun initializeDagger() {
		appComponent = DaggerAppComponent.builder()
				.appModule(AppModule(this))
				.dataModule(DataModule()).build()
	}
}

